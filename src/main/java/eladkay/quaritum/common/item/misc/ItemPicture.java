package eladkay.quaritum.common.item.misc;

import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

//This item is here just for its texture.
public class ItemPicture extends ItemMod {
    public ItemPicture() {
        super(LibNames.PICTURE, "picture", "questionMark");
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        //noop
    }
}
