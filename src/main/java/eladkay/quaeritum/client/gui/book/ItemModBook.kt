package eladkay.quaeritum.client.gui.book

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import eladkay.quaeritum.api.book.hierarchy.book.Book
import eladkay.quaeritum.client.gui.GUIHandler
import eladkay.quaeritum.common.Quaeritum
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

/**
 * @author WireSegal
 * Created at 11:03 AM on 2/18/18.
 */
abstract class ItemModBook(name: String, vararg variants: String) : ItemMod(name, *variants) {

    abstract fun getBook(player: EntityPlayer, world: World?, stack: ItemStack): Book

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        val stack = playerIn.getHeldItem(handIn)
        playerIn.openGui(Quaeritum.instance, GUIHandler.GUI_BOOK, worldIn, 0, 0, 0)
        return ActionResult(EnumActionResult.SUCCESS, stack)
    }

    class ItemSingleBook(name: String, val book: Book) : ItemModBook(name) {
        override fun getBook(player: EntityPlayer, world: World?, stack: ItemStack) = book
    }

    companion object {
        @JvmStatic
        @JvmName("forBook")
        operator fun invoke(name: String, book: Book) = ItemSingleBook(name, book)
    }
}
