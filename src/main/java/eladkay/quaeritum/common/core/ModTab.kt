package eladkay.quaeritum.common.core

import com.teamwizardry.librarianlib.features.base.ModCreativeTab
import eladkay.quaeritum.common.item.ModItems
import net.minecraft.item.ItemStack

/**
 * @author WireSegal
 * Created at 4:53 PM on 11/5/16.
 */
object ModTab : ModCreativeTab() {
    init {
        registerDefaultTab()
    }

    override val iconStack: ItemStack by lazy {
        ItemStack(ModItems.attuned)
    }
}
