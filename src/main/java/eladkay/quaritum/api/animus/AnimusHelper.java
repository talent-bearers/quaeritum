package eladkay.quaritum.api.animus;

import eladkay.quaritum.client.core.TooltipHelper;
import eladkay.quaritum.common.Quartium;
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower;
import eladkay.quaritum.common.core.ItemNBTHelper;
import eladkay.quaritum.common.lib.LibMisc;
import eladkay.quaritum.common.lib.LibNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public final class AnimusHelper {
    private AnimusHelper() {}

    public static int setAnimus(int animus, ItemStack stack) {
        ItemNBTHelper.setInt(stack, LibNBT.TAG_ANIMUS, animus);
        return animus;
    }

    public static int setRarity(int rarity, ItemStack stack) {
        ItemNBTHelper.setInt(stack, LibNBT.TAG_RARITY, rarity);
        return rarity;
    }

    public static int getRarity(ItemStack stack) {
        return ItemNBTHelper.getInt(stack, LibNBT.TAG_RARITY, 0);
    }

    public static int getAnimus(ItemStack stack) {
        return ItemNBTHelper.getInt(stack, LibNBT.TAG_ANIMUS, 0);
    }

    public static void addInformation(ItemStack stack, List list) {
        tooltipIfShift(list, () -> {
            if (Quartium.isDevEnv) {
                list.add("Animus: " + getAnimus(stack));
                list.add("Rarity: " + getRarity(stack));
            } else {
                list.add("Quality: " + BlockAnimusFlower.Variants.rarityToName(getRarity(stack)));
            }
        });
    }

    public static void tooltipIfShift(List<String> tooltip, Runnable r) {
        TooltipHelper.tooltipIfShift(tooltip, r);
    }

    public static final class Player {
        private static final String TAG_ANIMUS_LEVEL = LibMisc.MOD_ID + "_animusLevel";
        private static final String TAG_ANIMUS_RARITY = LibMisc.MOD_ID + "_animusRarity";

        private Player() {}

        public static void setAnimus(EntityPlayer player, int animus) {
            getPersistentCompound(player).setInteger(TAG_ANIMUS_LEVEL, animus);
        }

        public static void setAnimusRarity(EntityPlayer player, int rarity) {
            getPersistentCompound(player).setInteger(TAG_ANIMUS_RARITY, rarity);
        }

        public static int getAnimus(EntityPlayer player) {
            return getIntegerSafe(getPersistentCompound(player), TAG_ANIMUS_LEVEL, 0);
        }

        public static int getAnimusRarity(EntityPlayer player) {
            return getIntegerSafe(getPersistentCompound(player), TAG_ANIMUS_RARITY, 0);
        }

        public static void addAnimus(EntityPlayer player, int animus) {
            getPersistentCompound(player).setInteger(TAG_ANIMUS_LEVEL, animus + getAnimus(player));
        }

        public static void addAnimusRarity(EntityPlayer player, int rarity) {
            getPersistentCompound(player).setInteger(TAG_ANIMUS_RARITY, rarity + getAnimusRarity(player));
        }

        public static void addAnimusRarityProper(EntityPlayer player, int rarity) {
            if (getAnimusRarity(player) < rarity)
                setAnimusRarity(player, rarity);
        }

        private static int getIntegerSafe(NBTTagCompound compound, String tag, int fallback) {
            if (!compound.hasKey(tag, 3)) return fallback;
            return compound.getInteger(tag);
        }

        private static NBTTagCompound getPersistentCompound(EntityPlayer player) {
            NBTTagCompound data = player.getEntityData();
            if (!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
                data.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());

            return data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        }
    }
}
