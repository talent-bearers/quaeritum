package eladkay.quaritum.api.animus;

import eladkay.quaritum.api.lib.LibMisc;
import eladkay.quaritum.client.core.TooltipHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class AnimusHelper {

    public static void addInformation(ItemStack stack, List<String> tooltip, boolean advanced) {
        //todo
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


    public static final class Network {
        private static final String KEY_ANIMUS_NETWORK = LibMisc.MOD_ID + "-AnimusNetwork";
        private static final String TAG_ANIMUS_LEVEL = "animusLevel";
        private static final String TAG_ANIMUS_RARITY = "animusRarity";
        private static final String TAG_LAST_KNOWN_USERNAME = "lastUsername";

        public static void addInformation(ItemStack stack, List<String> tooltip, boolean advanced) {
            UUID uuid = ((INetworkProvider) stack.getItem()).getPlayer(stack);
            if (uuid != null)
                TooltipHelper.addToTooltip(tooltip, "misc." + LibMisc.MOD_ID + ".animusBound", getLastKnownUsername(uuid));
        }

        public static void setAnimus(EntityPlayer player, int animus) {
            updatePlayerName(player);
            setAnimus(player.getUniqueID(), animus);
        }

        public static void setAnimus(UUID uuid, int animus) {
            getPersistentCompound(uuid).setInteger(TAG_ANIMUS_LEVEL, animus);
        }

        public static void setAnimusRarity(EntityPlayer player, int rarity) {
            updatePlayerName(player);
            setAnimusRarity(player.getUniqueID(), rarity);
        }

        public static void setAnimusRarity(UUID uuid, int rarity) {
            getPersistentCompound(uuid).setInteger(TAG_ANIMUS_RARITY, rarity);
        }

        public static int getAnimus(EntityPlayer player) {
            updatePlayerName(player);
            return getAnimus(player.getUniqueID());
        }

        public static int getAnimus(UUID uuid) {
            return getIntegerSafe(getPersistentCompound(uuid), TAG_ANIMUS_LEVEL, 0);
        }

        public static int getAnimusRarity(EntityPlayer player) {
            updatePlayerName(player);
            return getAnimusRarity(player.getUniqueID());
        }

        public static int getAnimusRarity(UUID uuid) {
            return getIntegerSafe(getPersistentCompound(uuid), TAG_ANIMUS_RARITY, 0);
        }

        public static void addAnimus(EntityPlayer player, int animus) {
            updatePlayerName(player);
            addAnimus(player.getUniqueID(), animus);
        }

        public static void addAnimus(UUID uuid, int animus) {
            setAnimus(uuid, animus + getAnimus(uuid));
        }

        public static void addAnimusRarity(EntityPlayer player, int rarity) {
            updatePlayerName(player);
            addAnimusRarity(player.getUniqueID(), rarity);
        }

        public static void addAnimusRarity(UUID uuid, int rarity) {
            setAnimusRarity(uuid, Math.max(rarity, getAnimusRarity(uuid)));
        }

        public static void updatePlayerName(EntityPlayer player) {
            getPersistentCompound(player.getUniqueID()).setString(TAG_LAST_KNOWN_USERNAME, player.getDisplayNameString());
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
            World world = DimensionManager.getWorld(0);
            if (world == null || world.getMapStorage() == null || uuid == null)
                return new NBTTagCompound();

            AnimusSaveData saveData = (AnimusSaveData) world.getMapStorage().getOrLoadData(AnimusSaveData.class, KEY_ANIMUS_NETWORK);

            if (saveData == null) {
                saveData = new AnimusSaveData();
                world.getMapStorage().setData(KEY_ANIMUS_NETWORK, saveData);
            }

            if (!saveData.animusData.containsKey(uuid))
                saveData.animusData.put(uuid, new NBTTagCompound());

            return saveData.animusData.get(uuid);
        }

        private static class AnimusSaveData extends WorldSavedData {

            private Map<UUID, NBTTagCompound> animusData = new HashMap<>();

            private AnimusSaveData(String id) {
                super(id);
            }

            private AnimusSaveData() {
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
    }
}
