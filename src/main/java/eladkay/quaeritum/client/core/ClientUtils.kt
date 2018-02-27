package eladkay.quaeritum.client.core

import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.common.lib.capitalizeFirst
import net.minecraft.item.ItemStack
import java.util.*

object ClientUtils {
    fun addInformation(stack: ItemStack, tooltip: MutableList<String>, advanced: Boolean) {
        val animus = AnimusHelper.getAnimus(stack)
        if (animus != 0) {
            tooltip.add(AnimusHelper.getTier(stack).toString().toLowerCase(Locale.ROOT).capitalizeFirst())
            if (advanced)
                tooltip.add("${animus}x")
        }
    }
}
