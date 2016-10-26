package eladkay.quaritum.client.core

import com.teamwizardry.librarianlib.client.util.TooltipHelper
import eladkay.quaritum.api.animus.AnimusHelper
import eladkay.quaritum.common.Quaritum
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower
import net.minecraft.item.ItemStack

object ClientUtils {
    fun addInformation(stack: ItemStack, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.tooltipIfShift(tooltip, Runnable {
            if (Quaritum.isDevEnv) {
                tooltip.add("Animus: " + AnimusHelper.getAnimus(stack))
                tooltip.add("Rarity: " + AnimusHelper.getRarity(stack))
            } else
                for (variant in BlockAnimusFlower.Variants.values())
                    if (variant.rarity == AnimusHelper.getRarity(stack))
                        tooltip.add("Rarity: " + TooltipHelper.local(variant.getName()))
        })
    }
}
