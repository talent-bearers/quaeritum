package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import eladkay.quaeritum.api.spell.EnumSpellElement
import eladkay.quaeritum.api.spell.ISpellReagent
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult

/**
 * @author WireSegal
 * Created at 2:29 PM on 7/28/17.
 */
class ItemReagentBag : ItemMod(LibNames.REAGENT_BAG), ISpellReagent {
    companion object {
        fun getAmountIn(stack: ItemStack, element: EnumSpellElement): Int {
            val compound = ItemNBTHelper.getCompound(stack, "reagent") ?: return 0
            return compound.getInteger(element.representation.toString())
        }

        fun setAmountIn(stack: ItemStack, element: EnumSpellElement, amount: Int) {
            if (!ItemNBTHelper.verifyExistence(stack, "reagent"))
                ItemNBTHelper.setCompound(stack, "reagent", NBTTagCompound())

            val compound = ItemNBTHelper.getCompound(stack, "reagent") ?: return

            compound.setInteger(element.representation.toString(), amount)
        }
    }

    override fun canAddToReagentBag(stack: ItemStack) = false

    override fun chargesForElement(stack: ItemStack, element: EnumSpellElement) = getAmountIn(stack, element)

    override fun consumeCharge(stack: ItemStack, element: EnumSpellElement, charges: Int): ActionResult<ItemStack> {
        val amountIn = getAmountIn(stack, element)
        if (amountIn < charges)
            return ActionResult(EnumActionResult.FAIL, stack)

        val returnStack = stack.copy()
        setAmountIn(returnStack, element, amountIn - charges)
        return ActionResult(EnumActionResult.SUCCESS, returnStack)
    }
}
