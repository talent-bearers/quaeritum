package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.common.base.item.ItemMod
import eladkay.quaeritum.api.animus.EnumAnimusTier
import eladkay.quaeritum.api.animus.IAnimusResource
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

/**
 * @author WireSegal
 * Created at 9:05 PM on 2/8/17.
 */
class ItemEssence : ItemMod(LibNames.ESSENCE, *NAMES), IAnimusResource {
    companion object {
        @JvmOverloads
        fun stackOf(enum: EnumAnimusTier, size: Int = 1) = ItemStack(ModItems.essence, size, enum.ordinal)

        val NAMES = EnumAnimusTier.values().map { it.oreName }.toTypedArray()
    }

    init {
        for (variant in EnumAnimusTier.values())
            OreDictionary.registerOre(variant.oreName, ItemStack(this, 1, variant.ordinal))
    }

    override fun getAnimus(stack: ItemStack)
            = (EnumAnimusTier.fromMeta(stack.itemDamage).awakenedFillPercentage * ModItems.awakened.MAX_ANIMUS).toInt()
    override fun getAnimusTier(stack: ItemStack) = EnumAnimusTier.fromMeta(stack.itemDamage)
}
