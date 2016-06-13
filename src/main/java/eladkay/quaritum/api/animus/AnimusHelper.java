package eladkay.quaritum.api.animus;

import eladkay.quaritum.client.core.TooltipHelper;
import eladkay.quaritum.common.core.ItemNBTHelper;
import eladkay.quaritum.common.lib.LibNBT;
import net.minecraft.item.ItemStack;

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
            list.add("Animus: " + getAnimus(stack));
            list.add("Rarity: " + getRarity(stack));
        });
    }

    public static void tooltipIfShift(List<String> tooltip, Runnable r) {
        TooltipHelper.tooltipIfShift(tooltip, r);
    }
}
