package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import eladkay.quaeritum.api.animus.EnumAnimusTier
import eladkay.quaeritum.api.animus.IAnimusResource
import eladkay.quaeritum.api.spell.EnumSpellElement
import eladkay.quaeritum.api.spell.ISpellReagent
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraftforge.oredict.OreDictionary

/**
 * @author WireSegal
 * Created at 9:05 PM on 2/8/17.
 */
class ItemEssence : ItemMod(LibNames.ESSENCE, *NAMES), IAnimusResource, ISpellReagent {
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

    override fun canAddToReagentBag(stack: ItemStack) = true

    override fun chargesForElement(stack: ItemStack, element: EnumSpellElement): Int {
        val tier = getAnimusTier(stack)
        return if (element == tier.elementPrimary) 4 * stack.count
        else if (element == tier.elementSecondary) 2 * stack.count
        else -1
    }

    override fun consumeCharge(stack: ItemStack, element: EnumSpellElement, charges: Int): ActionResult<ItemStack> {
        val tier = getAnimusTier(stack)
        val amountPerItem = if (element == tier.elementPrimary) 4
        else if (element == tier.elementSecondary) 2
        else return ActionResult(EnumActionResult.PASS, stack)

        val totalAmount = amountPerItem * stack.count
        if (totalAmount < charges)
            return ActionResult(EnumActionResult.FAIL, stack)
        val returnStack = stack.copy()
        returnStack.count = (totalAmount - charges) / amountPerItem
        return ActionResult(EnumActionResult.SUCCESS, returnStack)
    }
}
