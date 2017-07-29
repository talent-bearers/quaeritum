package eladkay.quaeritum.client.render

import com.teamwizardry.librarianlib.core.client.ClientTickHandler
import com.teamwizardry.librarianlib.features.kotlin.safeCast
import eladkay.quaeritum.api.spell.ElementHandler
import eladkay.quaeritum.api.spell.EnumSpellElement
import eladkay.quaeritum.api.spell.render.RenderUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.nbt.NBTPrimitive
import net.minecraft.util.math.MathHelper
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11


/**
 * @author WireSegal
 * Created at 8:16 PM on 7/26/17.
 */
@SideOnly(Side.CLIENT)
object RenderSymbol {

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onHUDRender(e: RenderGameOverlayEvent.Post) {
        if (e.type != RenderGameOverlayEvent.ElementType.ALL) return

        val elements = ElementHandler.getReagents(Minecraft.getMinecraft().player)
        if (elements.tagCount() == 0) return

        val cX = e.resolution.scaledWidth / 2.0
        val cY = e.resolution.scaledHeight / 2.0
        val scale = e.resolution.scaleFactor * 15.0

        val startingAngle = (e.partialTicks + ClientTickHandler.ticks) * Math.PI / 120
        val angleSep = 2 * Math.PI / (elements.tagCount() + 1)


        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE)
        GlStateManager.shadeModel(GL11.GL_SMOOTH)
        GlStateManager.disableTexture2D()
        val tess = Tessellator.getInstance()
        val buffer = tess.buffer
        buffer.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_COLOR)
        RenderUtil.renderNGon(buffer,
                cX + MathHelper.cos(startingAngle.toFloat()) * scale - 0.5,
                cY + MathHelper.sin(startingAngle.toFloat()) * scale - 0.5,
                1f, 1f, 1f, 7.5, 5.0, RenderUtil.SEGMENTS_CIRCLE)
        tess.draw()
        GlStateManager.shadeModel(GL11.GL_FLAT)
        GlStateManager.enableTexture2D()
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA)

        for (idx in 0 until elements.tagCount()) {
            val el = elements.get(idx)
            val element = EnumSpellElement.values()[el.safeCast(NBTPrimitive::class.java).int % EnumSpellElement.values().size]
            val angle = startingAngle + (idx + 1) * angleSep
            val x = cX + scale * MathHelper.cos(angle.toFloat()) - 7.5
            val y = cY + scale * MathHelper.sin(angle.toFloat()) - 7.5
            renderSymbol(x.toFloat() , y.toFloat(), element)
        }
    }

    fun renderSymbol(x: Float, y: Float, element: EnumSpellElement) {
        val symbols = element.symbolInstructions
        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE)
        GlStateManager.shadeModel(GL11.GL_SMOOTH)
        GlStateManager.disableTexture2D()
        for (symbol in symbols) symbol.render(element, x, y)
        GlStateManager.shadeModel(GL11.GL_FLAT)
        GlStateManager.enableTexture2D()
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA)
    }
}
