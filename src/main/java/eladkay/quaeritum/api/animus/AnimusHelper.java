package eladkay.quaeritum.api.animus;

import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import eladkay.quaeritum.api.lib.LibMisc;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;

public final class AnimusHelper {

    public static ItemStack setAnimus(ItemStack stack, int animus) {
        if (stack.getItem() instanceof INetworkProvider && !(stack.getItem() instanceof ISoulstone)) {
            Network.setAnimus(((INetworkProvider) stack.getItem()).getPlayer(stack), animus);
            return stack;
        }
        if (!(stack.getItem() instanceof ISoulstone))
            return stack;
        ((ISoulstone) stack.getItem()).setAnimus(stack, animus);
        return stack;
    }

    public static ItemStack setTier(ItemStack stack, EnumAnimusTier tier) {
        if (stack.getItem() instanceof INetworkProvider && !(stack.getItem() instanceof ISoulstone)) {
            Network.setTier(((INetworkProvider) stack.getItem()).getPlayer(stack), tier);
            return stack;
        }
        if (!(stack.getItem() instanceof ISoulstone))
            return stack;
        ((ISoulstone) stack.getItem()).setAnimusTier(stack, tier);
        return stack;
    }

    public static ItemStack addAnimus(ItemStack stack, int animus) {
        return setAnimus(stack, getAnimus(stack) + animus);
    }

    public static EnumAnimusTier getTier(ItemStack stack) {
        if (stack.getItem() instanceof INetworkProvider && !(stack.getItem() instanceof ISoulstone))
            return Network.getTier(((INetworkProvider) stack.getItem()).getPlayer(stack));
        if (!(stack.getItem() instanceof ISoulstone)) return EnumAnimusTier.VERDIS;
        return ((ISoulstone) stack.getItem()).getAnimusTier(stack);
    }

    public static int getAnimus(ItemStack stack) {
        if (stack.getItem() instanceof INetworkProvider && !(stack.getItem() instanceof ISoulstone))
            return Network.getAnimus(((INetworkProvider) stack.getItem()).getPlayer(stack));
        if (!(stack.getItem() instanceof ISoulstone)) return 0;
        return ((ISoulstone) stack.getItem()).getAnimusLevel(stack);
    }

    public static void damageItem(ItemStack stack, int damageToApply, EntityLivingBase entity, int animusPerDamage, EnumAnimusTier tier) {
        int animusToDrain = damageToApply * animusPerDamage;
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode) return;
        boolean shouldDamage = !(entity instanceof EntityPlayer) || !Network.requestAnimus((EntityPlayer) entity, animusToDrain, tier, true);
        if (shouldDamage)
            stack.damageItem(damageToApply, entity);
    }

    public static final class Network {
        private static final String KEY_ANIMUS_NETWORK = LibMisc.MOD_ID + "-AnimusNetwork";
        private static final String TAG_ANIMUS_LEVEL = "animusLevel";
        private static final String TAG_INFUSED = "infused";
        private static final String TAG_ANIMUS_RARITY = "animusRarity";
        private static final String TAG_LAST_KNOWN_USERNAME = "lastUsername";

        public static int getAnimusColor(EntityPlayer player) {
            return getAnimusColor(player.getUniqueID());
        }

        private static final Map<UUID, Integer> cachedColors = new HashMap<>();

        public static int getAnimusColor(UUID uuid) {
            if (cachedColors.containsKey(uuid)) return cachedColors.get(uuid);
            Random random = new Random(uuid.getLeastSignificantBits() ^ (uuid.getMostSignificantBits() * 31));
            int color = Color.HSBtoRGB(random.nextFloat(), (random.nextFloat() / 2) + 0.5f, 1f);
            cachedColors.put(uuid, color);
            return color;
        }

        public static void addInformation(ItemStack stack, List<String> tooltip, boolean advanced) {
            UUID uuid = ((INetworkProvider) stack.getItem()).getPlayer(stack);
            if (uuid != null) {
                String username = getLastKnownUsername(uuid);
                if (username != null)
                    TooltipHelper.addToTooltip(tooltip, "misc." + LibMisc.MOD_ID + ".animus_bound", username);
                else
                    TooltipHelper.addToTooltip(tooltip, "misc." + LibMisc.MOD_ID + ".not_bound");
            } else
                TooltipHelper.addToTooltip(tooltip, "misc." + LibMisc.MOD_ID + ".not_bound");
        }

        public static void setAnimus(EntityPlayer player, int animus) {
            setAnimus(player.getUniqueID(), animus);
        }

        public static void setAnimus(UUID uuid, int animus) {
            getPersistentCompound(uuid).setInteger(TAG_ANIMUS_LEVEL, animus);
            getSaveData().markDirty();

        }

        public static void setTier(EntityPlayer player, EnumAnimusTier tier) {
            setTier(player.getUniqueID(), tier);
        }

        public static void setTier(UUID uuid, EnumAnimusTier tier) {
            getPersistentCompound(uuid).setInteger(TAG_ANIMUS_RARITY, tier.ordinal());
            getSaveData().markDirty();
        }

        public static int getAnimus(EntityPlayer player) {
            return getAnimus(player.getUniqueID());
        }

        public static int getAnimus(UUID uuid) {
            return getIntegerSafe(getPersistentCompound(uuid), TAG_ANIMUS_LEVEL, 0);
        }

        public static EnumAnimusTier getTier(EntityPlayer player) {
            return getTier(player.getUniqueID());
        }

        public static EnumAnimusTier getTier(UUID uuid) {
            return EnumAnimusTier.fromMeta(getIntegerSafe(getPersistentCompound(uuid), TAG_ANIMUS_RARITY, 0));
        }

        public static void addAnimus(EntityPlayer player, int animus) {
            addAnimus(player.getUniqueID(), animus);
        }

        public static void addAnimus(UUID uuid, int animus) {
            setAnimus(uuid, animus + getAnimus(uuid));
        }

        public static void addTier(EntityPlayer player, EnumAnimusTier tier) {
            addTier(player.getUniqueID(), tier);
        }

        public static void addTier(UUID uuid, EnumAnimusTier tier) {
            setTier(uuid, EnumAnimusTier.fromMeta(Math.max(tier.ordinal(), getTier(uuid).ordinal())));
        }

        public static boolean requestAnimus(EntityPlayer player, int animus, EnumAnimusTier tier, boolean drain) {
            return requestAnimus(player.getUniqueID(), animus, tier, drain);
        }

        public static boolean requestAnimus(UUID uuid, int animus, EnumAnimusTier tier, boolean drain) {
            if (getAnimus(uuid) < animus) return false;
            if (getTier(uuid).ordinal() < tier.ordinal()) return false;
            if (drain) addAnimus(uuid, -animus);
            return true;
        }

        public static void updatePlayerName(EntityPlayer player) {
            NBTTagCompound compound = getPersistentCompound(player.getUniqueID());
            if (!player.getName().equals(getStringSafe(compound, TAG_LAST_KNOWN_USERNAME, null))) {
                compound.setString(TAG_LAST_KNOWN_USERNAME, player.getName());
                getSaveData().markDirty();
            }
        }

        public static String getLastKnownUsername(UUID uuid) {
            return getStringSafe(getPersistentCompound(uuid), TAG_LAST_KNOWN_USERNAME, null);
        }

        private static boolean getBooleanSafe(NBTTagCompound compound, String tag, boolean fallback) {
            if (!compound.hasKey(tag)) return fallback;
            return compound.getBoolean(tag);
        }

        private static int getIntegerSafe(NBTTagCompound compound, String tag, int fallback) {
            if (!compound.hasKey(tag, 3)) return fallback;
            return compound.getInteger(tag);
        }

        private static String getStringSafe(NBTTagCompound compound, String tag, String fallback) {
            if (!compound.hasKey(tag, 8)) return fallback;
            return compound.getString(tag);
        }

        @NotNull
        private static NBTTagCompound getPersistentCompound(UUID uuid) {
            if (uuid == null) return new NBTTagCompound();

            AnimusSaveData saveData = getSaveData();

            if (!saveData.animusData.containsKey(uuid))
                saveData.animusData.put(uuid, new NBTTagCompound());

            return saveData.animusData.get(uuid);
        }

        @NotNull
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
            @NotNull
            public NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
                for (UUID key : animusData.keySet())
                    compound.setTag(key.toString(), animusData.get(key));
                return compound;
            }

            @Override
            public void readFromNBT(@NotNull NBTTagCompound compound) {
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
