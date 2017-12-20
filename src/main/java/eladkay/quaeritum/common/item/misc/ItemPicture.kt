package eladkay.quaeritum.common.item.misc


import com.teamwizardry.librarianlib.features.base.item.ItemMod
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList

//This item is here just for its texture.
class ItemPicture : ItemMod(LibNames.PICTURE, "picture", "questionMark") {

    override fun getSubItems(tab: CreativeTabs, subItems: NonNullList<ItemStack>) {
        // NO-OP
    }
}
