package eladkay.quaeritum.common.book

/*import amerifrance.guideapi.api.IPage
import amerifrance.guideapi.api.impl.Book
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract
import amerifrance.guideapi.gui.GuiBase
import amerifrance.guideapi.gui.GuiEntry
import com.google.common.collect.Maps
import com.teamwizardry.librarianlib.common.util.plus
import eladkay.quaeritum.api.lib.LibNBT
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.util.ItemNBTHelper
import eladkay.quaeritum.common.block.ModBlocks
import eladkay.quaeritum.common.core.PositionedBlockHelper
import eladkay.quaeritum.common.lib.LibLocations
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.texture.TextureUtil
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.ForgeHooksClient
import net.minecraftforge.client.model.pipeline.LightUtil
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.*

/*class PageWork(private val mb: List<PositionedBlock>) : IPage {

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

        Minecraft.getMinecraft().renderItem.renderItemIntoGUI(ItemStack(ModBlocks.foundation), x + 104, y + 120)
        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableRescaleNormal()

        GlStateManager.pushMatrix()
        GlStateManager.translate(0f, 0f, 200f)
        if (mx >= x + 96 + 8 && mx < x + 16 + 96 + 8 && my >= y + 120 && my < y + 136) {
            val mats = ArrayList<String>()
            mats.add(I18n.format("quaeritum.blocksrequired"))
            if (mb.size > 0) {
                val list = ArrayList<String>()
                for (block in mb) {
                    var name = ""
                    if (block.state.block === ModBlocks.chalk || block.state.block === ModBlocks.tempest)
                        name = PositionedBlockHelper.getStackFromChalk(block, false).displayName.replace("Tempest", TextFormatting.GOLD + "Tempest")
                    else if (Item.getItemFromBlock(block.state.block) != null)
                        name = Item.getItemFromBlock(block.state.block)!!.getItemStackDisplayName(ItemStack(Item.getItemFromBlock(block.state.block)!!, 1, block.state.block.getMetaFromState(block.state)))
                    list.add(name)
                }
                val map = Maps.newHashMap<String, Int>()
                for (name in list)
                    map.putIfAbsent(name, Collections.frequency(list, name))

                mats.addAll(map.keys.map({ name -> " " + TextFormatting.AQUA + map[name] + " " + TextFormatting.GRAY + name }))
            } else
                mats.add(" " + I18n.format("quaeritum.noblocks"))

            eladkay.quaeritum.client.core.RenderHelper.renderTooltip(mx, my, mats)
        }
        GlStateManager.popMatrix()


        val dims = PositionedBlockHelper.getDimensions(mb)
        val shift = if (dims.x >= 4 || dims.z >= 4) 2 else 1
        val s = if (shift == 2) 0.5 else 1.0

        GlStateManager.pushMatrix()
        GlStateManager.scale(s, s, s)
        for (block in mb)
            renderItemIntoGUI(PositionedBlockHelper.getStackFromChalk(block, true),
                    getXFromPos(block.getPos(), gui, shift) * shift, getYFromPos(block.getPos(), gui, shift) * shift, s)
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

    fun renderItemIntoGUI(stack: ItemStack, x: Double, y: Double, s: Double) {
        this.renderItemModelIntoGUI(stack, x, y, Minecraft.getMinecraft().renderItem.getItemModelWithOverrides(stack, null, null), s)
    }


    protected fun renderItemModelIntoGUI(stack: ItemStack, x: Double, y: Double, bakedmodel: IBakedModel, s: Double) {
        var bakedmodel = bakedmodel
        GlStateManager.pushMatrix()
        Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
        Minecraft.getMinecraft().textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false)
        GlStateManager.enableRescaleNormal()
        GlStateManager.enableAlpha()
        GlStateManager.alphaFunc(516, 0.1f)
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA)
        setupGuiTransform(x, y, bakedmodel.isGui3d)
        bakedmodel = ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.NONE, false)
        GlStateManager.scale(s / 2 * 3, s / 2 * 3, s / 2 * 3)
        GlStateManager.rotate(90f, 1f, 0f, 0f)
        renderItem(stack, bakedmodel)
        GlStateManager.disableAlpha()
        GlStateManager.disableRescaleNormal()
        GlStateManager.disableLighting()
        GlStateManager.popMatrix()
        Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
        Minecraft.getMinecraft().textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap()
    }

    fun renderItem(stack: ItemStack?, model: IBakedModel) {
        if (stack != null) {
            GlStateManager.pushMatrix()
            GlStateManager.translate(-0.5f, -0.5f, -0.5f)

            if (model.isBuiltInRenderer) {
                val rgb = Minecraft.getMinecraft().itemColors.getColorFromItemstack(stack, 1)
                GlStateManager.color(argb(rgb).red.toFloat(), argb(rgb).green.toFloat(), argb(rgb).blue.toFloat(), argb(rgb).alpha.toFloat())
                GlStateManager.enableRescaleNormal()
                TileEntityItemStackRenderer.instance.renderByItem(stack)
            } else {
                renderModel(model, Minecraft.getMinecraft().itemColors.getColorFromItemstack(stack, 1), stack)
                if (stack.hasEffect())
                    renderEffect(model)
            }

            GlStateManager.popMatrix()
        }
    }

    private fun renderQuads(renderer: VertexBuffer, quads: List<BakedQuad>, color: Int, stack: ItemStack?) {
        val flag = color == -1 && stack != null
        var i = 0

        val j = quads.size
        while (i < j) {
            val bakedquad = quads[i]
            var k = color

            if (flag && bakedquad.hasTintIndex()) {
                k = Minecraft.getMinecraft().itemColors.getColorFromItemstack(stack!!, bakedquad.tintIndex)

                if (EntityRenderer.anaglyphEnable) {
                    k = TextureUtil.anaglyphColor(k)
                }

                k = k or -16777216
            }

            LightUtil.renderQuadColor(renderer, bakedquad, k)
            ++i
        }
    }

    private fun renderModel(model: IBakedModel, color: Int, stack: ItemStack?) {
        val tessellator = Tessellator.getInstance()
        val vertexbuffer = tessellator.buffer
        vertexbuffer.begin(7, DefaultVertexFormats.ITEM)
        for (enumfacing in EnumFacing.values())
            renderQuads(vertexbuffer, model.getQuads(null, enumfacing, 0L), color, stack)
        renderQuads(vertexbuffer, model.getQuads(null, null, 0L), color, stack)
        tessellator.draw()
    }

    private fun setupGuiTransform(xPosition: Double, yPosition: Double, isGui3d: Boolean) {
        GlStateManager.translate(xPosition.toFloat(), yPosition.toFloat(), 100.0f + Minecraft.getMinecraft().renderItem.zLevel)
        GlStateManager.translate(8.0f, 8.0f, 0.0f)
        GlStateManager.scale(1.0f, -1.0f, 1.0f)
        GlStateManager.scale(16.0f, 16.0f, 16.0f)

        if (isGui3d)
            GlStateManager.enableLighting()
        else
            GlStateManager.disableLighting()

    }

    private fun renderEffect(model: IBakedModel) {
        GlStateManager.depthMask(false)
        GlStateManager.depthFunc(514)
        GlStateManager.disableLighting()
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE)
        Minecraft.getMinecraft().textureManager.bindTexture(RES_ITEM_GLINT)
        GlStateManager.matrixMode(5890)
        GlStateManager.pushMatrix()
        GlStateManager.scale(8.0f, 8.0f, 8.0f)
        val f = (Minecraft.getSystemTime() % 3000L).toFloat() / 3000.0f / 8.0f
        GlStateManager.translate(f, 0.0f, 0.0f)
        GlStateManager.rotate(-50.0f, 0.0f, 0.0f, 1.0f)
        Minecraft.getMinecraft().renderItem.renderModel(model, -8372020)
        GlStateManager.popMatrix()
        GlStateManager.pushMatrix()
        GlStateManager.scale(8.0f, 8.0f, 8.0f)
        val f1 = (Minecraft.getSystemTime() % 4873L).toFloat() / 4873.0f / 8.0f
        GlStateManager.translate(-f1, 0.0f, 0.0f)
        GlStateManager.rotate(10.0f, 0.0f, 0.0f, 1.0f)
        Minecraft.getMinecraft().renderItem.renderModel(model, -8372020)
        GlStateManager.popMatrix()
        GlStateManager.matrixMode(5888)
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA)
        GlStateManager.enableLighting()
        GlStateManager.depthFunc(515)
        GlStateManager.depthMask(true)
        Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
    }

    companion object {
        private val foundationStack = ItemStack(ModBlocks.foundation)

        init {
            ItemNBTHelper.setBoolean(foundationStack, LibNBT.FLAT, true)
        }

        private fun getXFromPos(pos: BlockPos, gui: GuiBase, scale: Int): Double {
            return gui.guiLeft + gui.xSize / 3 + pos.x * 32 / (3 * scale) + scale * 3 + 22.toDouble()
        }

        private fun getYFromPos(pos: BlockPos, gui: GuiBase, scale: Int): Double {
            return gui.guiTop + gui.ySize / 3 + pos.z * 32 / (3 * scale) - 34 + scale * 3 + 48.toDouble()
        }

        fun argb(color: Int): Color {
            val a = (color shr 24 and 0xff) / 255f
            val r = (color shr 16 and 0xff) / 255f
            val g = (color shr 8 and 0xff) / 255f
            val b = (color and 0xff) / 255f
            return Color(r, g, b, a)
        }

        private val RES_ITEM_GLINT = ResourceLocation("textures/misc/enchanted_item_glint.png")
    }

}
*/