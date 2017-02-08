package eladkay.quaeritum.client.core

import com.teamwizardry.librarianlib.LibrarianLib
import com.teamwizardry.librarianlib.client.util.TooltipHelper
import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.common.block.flowers.BlockAnimusFlower
import net.minecraft.item.ItemStack

object ClientUtils {
    fun addInformation(stack: ItemStack, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.tooltipIfShift(tooltip, Runnable {
            if (LibrarianLib.DEV_ENVIRONMENT) {
                tooltip.add("Animus: " + AnimusHelper.getAnimus(stack))
                tooltip.add("Rarity: " + AnimusHelper.getTier(stack))
            } else
                for (variant in BlockAnimusFlower.Variants.values())
                    if (variant.rarity == AnimusHelper.getTier(stack).ordinal)
                        tooltip.add("Rarity: " + TooltipHelper.local(variant.getName()))
        })
    }
}
