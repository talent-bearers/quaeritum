package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.core.common.OreDictionaryRegistrar
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

/**
 * @author WireSegal
 * Created at 4:23 PM on 1/30/17.
 */
class ItemResource : ItemMod(LibNames.RESOURCE, *Resources.NAMES) {
    enum class Resources {
        RUINED_SLAG, VICTIUM_INGOT, VICTIUM_NUGGET, TEMPESTEEL;

        @JvmOverloads
        fun stackOf(size: Int = 1) = ItemStack(ModItems.resource, size, ordinal)

        val oreName = name.toLowerCase().split("_").map(String::capitalize).joinToString("").decapitalize()

        companion object {
            @JvmField
            val NAMES = values().map { it.oreName }.toTypedArray()
        }
    }

    init {
        for (variant in Resources.values())
            OreDictionaryRegistrar.registerOre(variant.oreName, ItemStack(this, 1, variant.ordinal))
    }
}
