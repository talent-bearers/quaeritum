package eladkay.quaeritum.client.gui

import eladkay.quaeritum.common.item.ItemReagentBag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler

/**
 * @author WireSegal
 * Created at 1:49 PM on 7/29/17.
 */
object GUIHandler : IGuiHandler {
    val GUI_BAG = 0
    val GUI_CODEX = 1

    override fun getClientGuiElement(ID: Int, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int): Any? {
        if (ID == GUI_BAG) {
            val stack = getStack(player, ItemReagentBag::class.java)
            // todo
        } else if (ID == GUI_CODEX) {
            return GuiCodex()
        }
        return null
    }

    override fun getServerGuiElement(ID: Int, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int): Any? {
        if (ID == GUI_BAG) {
            val stack = getStack(player, ItemReagentBag::class.java)
            // todo
        }
        return null
    }

    private fun getStack(p: EntityPlayer, itemClass: Class<*>): ItemStack {
        var item = p.heldItemMainhand?.item
        if (item != null && itemClass.isInstance(item) || (item is ItemBlock && itemClass.isInstance(item.block)))
            return p.heldItemMainhand

        item = p.heldItemOffhand?.item
        if (item != null && itemClass.isInstance(item) || (item is ItemBlock && itemClass.isInstance(item.block)))
            return p.heldItemOffhand

        return ItemStack.EMPTY
    }
}
