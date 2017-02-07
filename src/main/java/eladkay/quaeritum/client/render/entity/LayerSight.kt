package eladkay.quaeritum.client.render.entity

import baubles.api.render.IRenderBauble
import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.common.core.SightHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11



/**
 * @author WireSegal
 * Created at 9:22 PM on 2/6/17.
 */
object LayerSight : LayerRenderer<AbstractClientPlayer> {

    val SIGHT_ICON = ResourceLocation(LibMisc.MOD_ID, "textures/misc/sight.png")

    override fun doRenderLayer(player: AbstractClientPlayer, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float) {
        if (SightHandler.hasTheSight(player)) {
            val yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * partialTicks
            val yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks
            val pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks

            Minecraft.getMinecraft().renderEngine.bindTexture(SIGHT_ICON)
            GlStateManager.enableBlend()
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GlStateManager.enableAlpha()
            GlStateManager.pushMatrix()
            GlStateManager.disableCull()

            GlStateManager.rotate(yawOffset, 0f, -1f, 0f)
            GlStateManager.rotate(yaw - 270, 0f, 1f, 0f)
            GlStateManager.rotate(pitch, 0f, 0f, 1f)

            IRenderBauble.Helper.translateToHeadLevel(player)
            IRenderBauble.Helper.translateToFace()
            GlStateManager.translate(0.25f, 3.425f, 0.625f)
            GlStateManager.rotate(180f, 0f, 0f, 1f)
            GlStateManager.scale(0.5, 0.5, 0.5)

            val tess = Tessellator.getInstance()
            val buffer = tess.buffer
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            buffer.pos(1.0, 1.0, 0.5).tex(1.0, 1.0).endVertex()
            buffer.pos(0.0, 1.0, 0.5).tex(0.0, 1.0).endVertex()
            buffer.pos(0.0, 0.0, 0.5).tex(0.0, 0.0).endVertex()
            buffer.pos(1.0, 0.0, 0.5).tex(1.0, 0.0).endVertex()
            tess.draw()
            GlStateManager.popMatrix()
            GlStateManager.enableCull()
            GlStateManager.enableLighting()
            GlStateManager.disableBlend()
            GlStateManager.disableAlpha()
        }
    }

    override fun shouldCombineTextures(): Boolean {
        return true
    }
}
