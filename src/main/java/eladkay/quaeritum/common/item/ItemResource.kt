package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.common.base.item.ItemMod
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.item.ItemStack

/**
 * @author WireSegal
 * Created at 4:23 PM on 1/30/17.
 */
class ItemResource : ItemMod(LibNames.RESOURCE, *Resources.NAMES) {
    enum class Resources {
        RUINED_SLAG, VICTIUM_INGOT, VICTIUM_NUGGET, ARCANE_ESSENCE;

        @JvmOverloads
        fun stackOf(size: Int = 1) = ItemStack(ModItems.resource, size, ordinal)

        companion object {
            @JvmField
            val NAMES = values().map { it.name.toLowerCase().split("_").map(String::capitalize).joinToString("").decapitalize() }.toTypedArray()
        }
    }
}
