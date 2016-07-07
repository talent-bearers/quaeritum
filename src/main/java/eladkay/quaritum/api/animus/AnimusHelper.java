package eladkay.quaritum.api.animus;

import eladkay.quaritum.api.lib.LibMisc;
import eladkay.quaritum.client.core.TooltipHelper;
import eladkay.quaritum.common.Quartium;
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class AnimusHelper {

    public static void addInformation(ItemStack stack, List<String> tooltip, boolean advanced) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            if(Quartium.isDevEnv) {
                tooltip.add("Animus: " + getAnimus(stack));
                tooltip.add("Rarity: " + getRarity(stack));
            } else for(BlockAnimusFlower.Variants variant : BlockAnimusFlower.Variants.values())
                if(variant.rarity == getRarity(stack)) tooltip.add("Rarity: " + TooltipHelper.local(variant.getName()));
        });
    }

    public static ItemStack setAnimus(ItemStack stack, int animus) {
        ((ISoulstone) stack.getItem()).setAnimus(stack, animus);
        return stack;
    }

    public static ItemStack setRarity(ItemStack stack, int rarity) {
        ((ISoulstone) stack.getItem()).setRarity(stack, rarity);
        return stack;
    }

    public static ItemStack addAnimus(ItemStack stack, int animus) {
        return setAnimus(stack, getAnimus(stack) + animus);
    }

    public static ItemStack addRarity(ItemStack stack, int rarity) {
        return setRarity(stack, Math.max(getRarity(stack), rarity));
    }

    public static ItemStack minimizeRarity(ItemStack stack, int rarity) {
        return setRarity(stack, Math.min(getRarity(stack), rarity));
    }

    public static int getRarity(ItemStack stack) {
        return ((ISoulstone) stack.getItem()).getRarityLevel(stack);
    }

    public static int getAnimus(ItemStack stack) {
        return ((ISoulstone) stack.getItem()).getAnimusLevel(stack);
    }

    public static void damageItem(ItemStack stack, int damageToApply, EntityLivingBase entity, int animusPerDamage, int rarity) {
        int animusToDrain = damageToApply * animusPerDamage;
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode) return;
        boolean shouldDamage = !(entity instanceof EntityPlayer) || !Network.requestAnimus((EntityPlayer) entity, animusToDrain, rarity, true);
        if (shouldDamage)
            stack.damageItem(damageToApply, entity);
    }

    public static final class Network {
        private static final String KEY_ANIMUS_NETWORK = LibMisc.MOD_ID + "-AnimusNetwork";
        private static final String TAG_ANIMUS_LEVEL = "animusLevel";
        private static final String TAG_ANIMUS_RARITY = "animusRarity";
        private static final String TAG_LAST_KNOWN_USERNAME = "lastUsername";

        public static void addInformation(ItemStack stack, List<String> tooltip, boolean advanced) {
            UUID uuid = ((INetworkProvider) stack.getItem()).getPlayer(stack);
            if (uuid != null) {
                String username = getLastKnownUsername(uuid);
                if (username != null)
                    TooltipHelper.addToTooltip(tooltip, "misc." + LibMisc.MOD_ID + ".animusBound", username);
                else
                    TooltipHelper.addToTooltip(tooltip, "misc." + LibMisc.MOD_ID + ".notBound");
            } else
                TooltipHelper.addToTooltip(tooltip, "misc." + LibMisc.MOD_ID + ".notBound");
        }

        public static void setAnimus(EntityPlayer player, int animus) {
            setAnimus(player.getUniqueID(), animus);
        }

        public static void setAnimus(UUID uuid, int animus) {
            getPersistentCompound(uuid).setInteger(TAG_ANIMUS_LEVEL, animus);
            getSaveData().markDirty();

        }

        public static void setRarity(EntityPlayer player, int rarity) {
            setRarity(player.getUniqueID(), rarity);
        }

        public static void setRarity(UUID uuid, int rarity) {
            getPersistentCompound(uuid).setInteger(TAG_ANIMUS_RARITY, rarity);
            getSaveData().markDirty();
        }

        public static int getAnimus(EntityPlayer player) {
            return getAnimus(player.getUniqueID());
        }

        public static int getAnimus(UUID uuid) {
            return getIntegerSafe(getPersistentCompound(uuid), TAG_ANIMUS_LEVEL, 0);
        }

        public static int getRarity(EntityPlayer player) {
            return getRarity(player.getUniqueID());
        }

        public static int getRarity(UUID uuid) {
            return getIntegerSafe(getPersistentCompound(uuid), TAG_ANIMUS_RARITY, 0);
        }

        public static void addAnimus(EntityPlayer player, int animus) {
            addAnimus(player.getUniqueID(), animus);
        }

        public static void addAnimus(UUID uuid, int animus) {
            setAnimus(uuid, animus + getAnimus(uuid));
        }

        public static void addRarity(EntityPlayer player, int rarity) {
            addRarity(player.getUniqueID(), rarity);
        }

        public static void addRarity(UUID uuid, int rarity) {
            setRarity(uuid, Math.max(rarity, getRarity(uuid)));
        }

        public static boolean requestAnimus(EntityPlayer player, int animus, int rarity, boolean drain) {
            return requestAnimus(player.getUniqueID(), animus, rarity, drain);
        }

        public static boolean requestAnimus(UUID uuid, int animus, int rarity, boolean drain) {
            if (getAnimus(uuid) < animus) return false;
            if (getRarity(uuid) < rarity) return false;
            if (drain) addAnimus(uuid, -animus);
            return true;
        }

        public static void updatePlayerName(EntityPlayer player) {
            NBTTagCompound compound = getPersistentCompound(player.getUniqueID());
            if (!player.getDisplayNameString().equals(getStringSafe(compound, TAG_LAST_KNOWN_USERNAME, null))) {
                compound.setString(TAG_LAST_KNOWN_USERNAME, player.getDisplayNameString());
                getSaveData().markDirty();
            }
        }

        public static String getLastKnownUsername(UUID uuid) {
            return getStringSafe(getPersistentCompound(uuid), TAG_LAST_KNOWN_USERNAME, null);
        }


        private static int getIntegerSafe(NBTTagCompound compound, String tag, int fallback) {
            if (!compound.hasKey(tag, 3)) return fallback;
            return compound.getInteger(tag);
        }

        private static String getStringSafe(NBTTagCompound compound, String tag, String fallback) {
            if (!compound.hasKey(tag, 8)) return fallback;
            return compound.getString(tag);
        }

        @Nonnull
        private static NBTTagCompound getPersistentCompound(UUID uuid) {
            if (uuid == null) return new NBTTagCompound();

            AnimusSaveData saveData = getSaveData();

            if (!saveData.animusData.containsKey(uuid))
                saveData.animusData.put(uuid, new NBTTagCompound());

            return saveData.animusData.get(uuid);
        }

        @Nonnull
        private static AnimusSaveData getSaveData() {
            World world = DimensionManager.getWorld(0);
            if (world == null || world.getMapStorage() == null)
                return new AnimusSaveData();

            AnimusSaveData saveData = (AnimusSaveData) world.getMapStorage().getOrLoadData(AnimusSaveData.class, KEY_ANIMUS_NETWORK);

            if (saveData == null) {
                saveData = new AnimusSaveData();
                world.getMapStorage().setData(KEY_ANIMUS_NETWORK, saveData);
            }

            return saveData;
        }

        public static class AnimusSaveData extends WorldSavedData {

            private Map<UUID, NBTTagCompound> animusData = new HashMap<>();

            public AnimusSaveData(String id) {
                super(id);
            }

            public AnimusSaveData() {
                super(KEY_ANIMUS_NETWORK);
            }

            @Override
            @Nonnull
            public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
                for (UUID key : animusData.keySet())
                    compound.setTag(key.toString(), animusData.get(key));
                return compound;
            }

            @Override
            public void readFromNBT(@Nonnull NBTTagCompound compound) {
                for (String key : compound.getKeySet()) {
                    animusData.put(UUID.fromString(key), compound.getCompoundTag(key));
                }
            }
        }

        public static class EventHandler {
            public EventHandler() {
                MinecraftForge.EVENT_BUS.register(this);
            }

            @SubscribeEvent
            public void onEntityTick(LivingEvent.LivingUpdateEvent e) {
                if (e.getEntityLiving() instanceof EntityPlayer) {
                    AnimusHelper.Network.updatePlayerName((EntityPlayer) e.getEntityLiving());
                }
            }
        }
    }
}
