package eladkay.quaeritum.common.item.misc

import com.teamwizardry.librarianlib.common.base.item.ItemMod
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

//This item is here just for its texture.
class ItemPicture : ItemMod(LibNames.PICTURE, "picture", "questionMark") {

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
        //noop
    }
}
