package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.core.common.OreDictionaryRegistrar
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.item.ItemStack
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * @author WireSegal
 * Created at 4:23 PM on 1/30/17.
 */
class ItemResource : ItemMod(LibNames.RESOURCE, *Resources.NAMES) {
    enum class Resources {
        VICTIUM_INGOT, VICTIUM_NUGGET, TEMPESTEEL,
        BITUMEN, FLOWER_DUST, SLURRY, MIXTURE_MATRIX, ALLOY_MATRIX, RUSTED_MATRIX, PERFECT_MATRIX;

        @JvmOverloads
        fun stackOf(size: Int = 1) = ItemStack(ModItems.resource, size, ordinal)

        val oreName = name.toLowerCase().split("_").map(String::capitalize).joinToString("").decapitalize()

        companion object {
            @JvmField
            val NAMES = values().map { it.oreName }.toTypedArray()
        }
    }

    companion object {
        @SubscribeEvent
        fun fuel(e: FurnaceFuelBurnTimeEvent) {
            if (e.itemStack.item is ItemResource) {
                if (e.itemStack.itemDamage == Resources.BITUMEN.ordinal)
                    e.burnTime = 1600
                else if (e.itemStack.itemDamage == Resources.FLOWER_DUST.ordinal)
                    e.burnTime = 400
            }
        }

        init {
            MinecraftForge.EVENT_BUS.register(this)
        }
    }

    init {
        for (variant in Resources.values())
            OreDictionaryRegistrar.registerOre(variant.oreName) {
                ItemStack(this, 1, variant.ordinal)
            }
    }
}
