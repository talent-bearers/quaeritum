package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.LibrarianLib
import com.teamwizardry.librarianlib.common.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.common.base.item.ItemMod
import com.teamwizardry.librarianlib.common.util.ItemNBTHelper
import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.api.contract.ContractRegistry
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World

/**
 * @author WireSegal
 * Created at 5:33 PM on 2/26/17.
 */
class ItemContractScroll : ItemMod(LibNames.SCROLL, LibNames.SCROLL, LibNames.SEALED_SCROLL), IItemColorProvider {
    companion object {
        init {
            ContractRegistry.registerOath("ea", 4, { player, stack, world, pos -> })
            ContractRegistry.registerOath("seven", 4, { player, stack, world, pos -> })
        }
    }

    override fun onItemUse(playerIn: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        return onItemRightClick(worldIn, playerIn, hand).type
    }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { stack, i ->
            if (i == 1)
                AnimusHelper.Network.getAnimusColor(LibrarianLib.PROXY.getClientPlayer()) // Temporary, will be done via animus binding
            else -1
        }

    /*
        May secrets be kept in the shadows of light,
        may truth be found in the apex of night.
        May life flow through all flesh and blood,
        through soul and storm, let our hope be a flood.
     */
    val WORDS_OF_AGES = 0x2F0E38FE

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val itemStackIn = playerIn.getHeldItem(hand)

        if (!playerIn.isSneaking)
            return ActionResult(EnumActionResult.PASS, itemStackIn)

        if (!worldIn.isRemote) {
            val oathIndex = itemStackIn.oath
            var newOathIndex = (oathIndex + 1) % ContractRegistry.getMaxId()
            var newOath = ContractRegistry.getOathFromId(newOathIndex)!!
            while (!newOath.unlocked(playerIn)) {
                newOathIndex = (newOathIndex + 1) % ContractRegistry.getMaxId()
                newOath = ContractRegistry.getOathFromId(newOathIndex)!!
            }
            itemStackIn.oath = newOathIndex
            val component = TextComponentTranslation(newOath.getUnlocName(itemStackIn)).setStyle(Style().setBold(true))
            for (line in newOath.getUnlocText(itemStackIn)) {
                component.appendSibling(TextComponentString("\n | ").setStyle(Style().setBold(false)))
                component.appendSibling(TextComponentTranslation(line).setStyle(Style().setBold(false)))
            }

            LibrarianLib.PROXY.sendSpamlessMessage(playerIn, component, WORDS_OF_AGES)
        }

        return ActionResult(EnumActionResult.SUCCESS, itemStackIn)
    }

}

var ItemStack.oath: Int
    get() = ItemNBTHelper.getInt(this, "oath", -1)
    set(value) = if (value == -1) ItemNBTHelper.removeEntry(this, "oath") else ItemNBTHelper.setInt(this, "oath", value)
