package eladkay.quaeritum.common.core

import com.teamwizardry.librarianlib.common.base.ModCreativeTab
import eladkay.quaeritum.common.item.ModItems
import net.minecraft.item.ItemStack

/**
 * @author WireSegal
 * Created at 4:53 PM on 11/5/16.
 */
object Tab : ModCreativeTab() {
    override fun getIconItemStack(): ItemStack {
        return ItemStack(ModItems.attuned)
    }
}
