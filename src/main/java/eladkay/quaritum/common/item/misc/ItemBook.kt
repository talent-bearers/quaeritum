package eladkay.quaritum.common.item.misc

import com.teamwizardry.librarianlib.common.base.item.ItemMod
import eladkay.quaritum.api.lib.LibMisc
import eladkay.quaritum.common.lib.LibNames
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

class ItemBook : ItemMod(LibNames.BOOK) {

    override fun onItemRightClick(itemStackIn: ItemStack, worldIn: World?, playerIn: EntityPlayer?, hand: EnumHand?): ActionResult<ItemStack> {
        BookHandler.display(LibMisc.MOD_ID)
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand)
    }
}
