package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import eladkay.quaeritum.api.spell.ElementHandler
import eladkay.quaeritum.api.spell.EnumSpellElement
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

/**
 * @author WireSegal
 * Created at 10:01 PM on 8/3/17.
 */
class ItemEvoker : ItemMod(LibNames.SOUL_EVOKER) {
    companion object {
        fun getEvocationFromStack(stack: ItemStack): Array<EnumSpellElement> {
            if (stack.item !is ItemEvoker) return arrayOf()
            return ElementHandler.fromBytes(ItemNBTHelper.getByteArray(stack, "elements")?: byteArrayOf())
        }

        fun setStackEvocation(stack: ItemStack, elements: Array<EnumSpellElement>) {
            if (stack.item !is ItemEvoker) return
            ItemNBTHelper.setIntArray(stack, "elements", ElementHandler.fromElements(elements))
        }
    }

    init {
        setMaxStackSize(1)
    }

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        val stack = playerIn.getHeldItem(handIn)
        if (worldIn.isRemote) return ActionResult(EnumActionResult.SUCCESS, stack)
        val evocation = getEvocationFromStack(stack)
        val playerReagents = ElementHandler.getReagentsTyped(playerIn)
        if (evocation.isEmpty() && playerIn.isSneaking) {
            ElementHandler.clearReagents(playerIn)
            setStackEvocation(stack, playerReagents)
        } else if (evocation.isNotEmpty()) {
            if (playerReagents.isNotEmpty())
                if (playerIn.isSneaking) ElementHandler.clearReagents(playerIn)
                else {
                    // todo breaking sound
                    return ActionResult(EnumActionResult.SUCCESS, stack)
                }

            if (ElementHandler.addReagents(playerIn, *evocation) != EnumActionResult.SUCCESS)
                if (playerIn.isSneaking) {
                    setStackEvocation(stack, arrayOf())
                    ElementHandler.setReagents(playerIn, *evocation)
                } else {
                    // todo breaking sound
                    return ActionResult(EnumActionResult.SUCCESS, stack)
                }
        }
        return ActionResult(EnumActionResult.SUCCESS, stack)
    }
}
