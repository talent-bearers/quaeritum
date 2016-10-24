package eladkay.quaritum.common.item.misc;

import com.teamwizardry.librarianlib.client.gui.book.BookHandler;
import eladkay.quaritum.api.lib.LibMisc;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemBook extends ItemMod {

    public ItemBook() {
        super(LibNames.BOOK);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        BookHandler.display(LibMisc.MOD_ID);
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }
}
