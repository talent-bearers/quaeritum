package eladkay.quaritum.common.core

import eladkay.quaritum.api.lib.LibMisc
import eladkay.quaritum.common.block.ModBlocks
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import java.util.*

/**
 * @author WireSegal
 * *         Created at 5:22 PM on 4/16/16.
 */

class CreativeTab : CreativeTabs(LibMisc.MOD_ID) {
    private var list: List<ItemStack>? = null

    override fun getIconItemStack(): ItemStack {
        return ItemStack(ModBlocks.blueprint)
    }

    override fun getTabIconItem(): Item {
        return iconItemStack.item
    }

    override fun displayAllRelevantItems(l: List<ItemStack>) {
        list = l
        items.forEach(Consumer<Item> { this.addItem(it) })
    }

    private fun addItem(item: Item?) {
        if (item == null) return
        item.getSubItems(item, this, list!!)
    }

    companion object {

        var INSTANCE = CreativeTab()

        private val items = ArrayList<Item>()

        fun set(block: Block) {
            items.add(Item.getItemFromBlock(block))
            block.setCreativeTab(INSTANCE)
        }

        fun set(item: Item) {
            //        if(!(item instanceof ItemPicture)) {
            items.add(item)
            item.creativeTab = INSTANCE
            // }
        }
    }

}
