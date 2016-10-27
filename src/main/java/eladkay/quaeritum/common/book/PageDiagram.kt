package eladkay.quaeritum.common.book

/*import amerifrance.guideapi.api.IPage
import amerifrance.guideapi.api.impl.Book
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract
import amerifrance.guideapi.gui.GuiBase
import amerifrance.guideapi.gui.GuiEntry
import com.teamwizardry.librarianlib.common.util.plus
import eladkay.quaeritum.api.lib.LibNBT
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.util.ItemNBTHelper
import eladkay.quaeritum.common.block.ModBlocks
import eladkay.quaeritum.common.core.PositionedBlockHelper
import eladkay.quaeritum.common.item.ModItems
import eladkay.quaeritum.common.lib.LibLocations
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextFormatting
import org.lwjgl.opengl.GL11
import java.util.*

class PageDiagram(private val mb: List<PositionedBlock>, private val items: List<ItemStack>) : IPage {

    override fun draw(book: Book, category: CategoryAbstract, entry: EntryAbstract, guiLeft: Int, guiTop: Int, mx: Int, my: Int, gui: GuiBase, fontRendererObj: FontRenderer) {
        val render = Minecraft.getMinecraft().renderEngine
        render.bindTexture(LibLocations.MULTIBLOCK)

        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GlStateManager.disableAlpha()
        GlStateManager.color(1f, 1f, 1f, 1f)
        gui.drawTexturedModalRect(gui.guiLeft + 24, gui.guiTop, 0, 0, Math.min(gui.width, 256), Math.min(256, gui.height))
        GlStateManager.disableBlend()
        GlStateManager.enableAlpha()

        val maxX = 90f
        val maxY = 60f
        GlStateManager.pushMatrix()
        GlStateManager.translate((gui.guiLeft + gui.width / 2).toFloat(), (gui.guiTop + 90).toFloat(), gui.publicZLevel + 100f)

        val diag = Math.sqrt((xSize * xSize + zSize * zSize).toDouble()).toFloat()
        val height = ySize
        val scaleX = maxX / diag
        val scaleY = maxY / height
        val scale = -Math.min(scaleY, scaleX)
        GlStateManager.scale(scale, scale, scale)

        GlStateManager.rotate(-20f, 1f, 0f, 0f)

        GlStateManager.popMatrix()

        GlStateManager.enableRescaleNormal()
        RenderHelper.enableGUIStandardItemLighting()
        val x = guiLeft + 15 + 24
        val y = guiTop + 25

        Minecraft.getMinecraft().renderItem.renderItemIntoGUI(ItemStack(ModItems.picture, 1, 1), x, y)
        Minecraft.getMinecraft().renderItem.renderItemIntoGUI(ItemStack(ModBlocks.blueprint), x + 96, y)
        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableRescaleNormal()

        GlStateManager.pushMatrix()
        GlStateManager.translate(0f, 0f, 200f)
        if (mx >= x && mx < x + 16 && my >= y && my < y + 16) {
            val mats = ArrayList<String>()
            val map = HashMap<String, Int>()
            mats.add(I18n.format("quaeritum.itemsRequired"))
            for (stack in items) {
                val name = stack.displayName
                if (!map.containsKey(name))
                    map.put(name, stack.stackSize)
                else
                    map.put(name, stack.stackSize + (map[name] ?: 0))
            }
            mats.addAll(map.keys.map{ name -> " " + TextFormatting.AQUA + map[name] + " " + TextFormatting.GRAY + name })


            eladkay.quaeritum.client.core.RenderHelper.renderTooltip(mx, my, mats)
        }
        if (mx >= x + 96 && mx < x + 16 + 96 && my >= y && my < y + 16) {
            val mats = ArrayList<String>()
            mats.add(I18n.format("quaeritum.chalksRequired"))
            if (mb.size > 0) {
                val list = ArrayList<String>()
                for (block in mb) {
                    val name = PositionedBlockHelper.getStackFromChalk(block, false).displayName.replace("Tempest", TextFormatting.GOLD + "Tempest")
                    if (!list.contains(name)) list.add(name)
                }
                mats.addAll(list.map({ name -> " " + TextFormatting.GRAY + name }))
            } else
                mats.add(" " + I18n.format("quaeritum.nochalk"))

            eladkay.quaeritum.client.core.RenderHelper.renderTooltip(mx, my, mats)
        }
        GlStateManager.popMatrix()


        val dims = PositionedBlockHelper.getDimensions(mb)
        val shift = if (dims.x >= 4 || dims.z >= 4) 2 else 1
        val s = if (shift == 2) 0.5 else 1.0

        GlStateManager.pushMatrix()
        GlStateManager.scale(s, s, s)
        Minecraft.getMinecraft().renderItem.renderItemIntoGUI(blueprintStack,
                getXFromPos(BlockPos.ORIGIN, gui, shift) * shift, getYFromPos(BlockPos.ORIGIN, gui, shift) * shift)
        for (block in mb)
            Minecraft.getMinecraft().renderItem.renderItemIntoGUI(PositionedBlockHelper.getStackFromChalk(block, true),
                    getXFromPos(block.getPos(), gui, shift) * shift, getYFromPos(block.getPos(), gui, shift) * shift)
        GlStateManager.popMatrix()

    }

    override fun drawExtras(book: Book, category: CategoryAbstract, entry: EntryAbstract, guiLeft: Int, guiTop: Int, mouseX: Int, mouseY: Int, guiBase: GuiBase, fontRendererObj: FontRenderer) {

    }

    override fun canSee(book: Book, category: CategoryAbstract, entry: EntryAbstract, player: EntityPlayer, bookStack: ItemStack, guiEntry: GuiEntry): Boolean {
        return true
    }

    override fun onLeftClicked(book: Book, category: CategoryAbstract, entry: EntryAbstract, mouseX: Int, mouseY: Int, player: EntityPlayer, guiEntry: GuiEntry) {
    }

    override fun onRightClicked(book: Book, category: CategoryAbstract, entry: EntryAbstract, mouseX: Int, mouseY: Int, player: EntityPlayer, guiEntry: GuiEntry) {
    }

    override fun onInit(book: Book, category: CategoryAbstract, entry: EntryAbstract, player: EntityPlayer, bookStack: ItemStack, gui: GuiEntry) {
    }

    val ySize: Float
        get() = PositionedBlockHelper.getDimensions(mb).y.toFloat()

    val zSize: Float
        get() = PositionedBlockHelper.getDimensions(mb).y.toFloat()

    val xSize: Float
        get() = PositionedBlockHelper.getDimensions(mb).y.toFloat()

    companion object {
        private val blueprintStack = ItemStack(ModBlocks.blueprint)

        init {
            ItemNBTHelper.setBoolean(blueprintStack, LibNBT.FLAT, true)
        }

        private fun getXFromPos(pos: BlockPos, gui: GuiBase, scale: Int): Int {
            return gui.guiLeft + gui.xSize / 2 + pos.x * 16 / scale - 14 + scale * 6
        }

        private fun getYFromPos(pos: BlockPos, gui: GuiBase, scale: Int): Int {
            return gui.guiTop + gui.ySize / 2 + pos.z * 16 / scale - 34 + scale * 5
        }
    }

}
*/