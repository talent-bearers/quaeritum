package eladkay.quaeritum.common.item.misc

import eladkay.quaeritum.common.item.base.ItemQuaeritum
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

class ItemBook : ItemQuaeritum(LibNames.BOOK) {

    override fun onItemRightClick(itemStackIn: ItemStack, worldIn: World?, playerIn: EntityPlayer?, hand: EnumHand?): ActionResult<ItemStack> {
//        BookHandler.display(LibMisc.MOD_ID)
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand)
    }
}
