package eladkay.quaeritum.client.core

import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.common.lib.capitalizeFirst
import net.minecraft.item.ItemStack
import java.util.*

object ClientUtils {
    fun addInformation(stack: ItemStack, tooltip: MutableList<String>, advanced: Boolean) {
        tooltip.add(AnimusHelper.getTier(stack).toString().toLowerCase(Locale.ROOT).capitalizeFirst())
        if (advanced)
            tooltip.add(AnimusHelper.getAnimus(stack).toString() + "x")
    }
}
