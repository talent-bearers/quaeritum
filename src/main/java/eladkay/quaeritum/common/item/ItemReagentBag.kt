package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import eladkay.quaeritum.api.spell.EnumSpellElement
import eladkay.quaeritum.api.spell.ISpellReagent
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.NonNullList

/**
 * @author WireSegal
 * Created at 2:29 PM on 7/28/17.
 */
class ItemReagentBag : ItemMod(LibNames.REAGENT_BAG), ISpellReagent {
    companion object {
        fun getAmountIn(stack: ItemStack, element: EnumSpellElement): Int {
            val compound = ItemNBTHelper.getCompound(stack, "reagent") ?: return 0
            val i = compound.getInteger(element.representation.toString())
            if (i == -1) return 8
            return i
        }

        fun setAmountIn(stack: ItemStack, element: EnumSpellElement, amount: Int) {
            if (!ItemNBTHelper.verifyExistence(stack, "reagent"))
                ItemNBTHelper.setCompound(stack, "reagent", NBTTagCompound())

            val compound = ItemNBTHelper.getCompound(stack, "reagent") ?: return
            if (compound.getInteger(element.representation.toString()) == -1) return

            compound.setInteger(element.representation.toString(), amount)
        }
    }

    override fun getSubItems(tab: CreativeTabs?, subItems: NonNullList<ItemStack>) {
        super.getSubItems(tab, subItems)
        if (isInCreativeTab(tab)) {
            val stack = ItemStack(this)
            for (el in EnumSpellElement.values())
                setAmountIn(stack, el, -1)
            stack.setStackDisplayName("∞")
            subItems.add(stack)
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
