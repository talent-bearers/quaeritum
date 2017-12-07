package eladkay.quaeritum.client.core

import com.teamwizardry.librarianlib.core.LibrarianLib
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper
import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.api.animus.EnumAnimusTier
import net.minecraft.item.ItemStack

object ClientUtils {
    fun addInformation(stack: ItemStack, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.tooltipIfShift(tooltip, Runnable {
            if (LibrarianLib.DEV_ENVIRONMENT) {
                tooltip.add("Animus: " + AnimusHelper.getAnimus(stack))
                tooltip.add("Rarity: " + AnimusHelper.getTier(stack))
            } else
                for (variant in EnumAnimusTier.values())
                    if (variant == AnimusHelper.getTier(stack))
                        tooltip.add("Rarity: " + TooltipHelper.local(variant.getName()))
        })
    }
}
