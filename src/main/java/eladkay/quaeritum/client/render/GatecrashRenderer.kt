package eladkay.quaeritum.client.render

import com.teamwizardry.librarianlib.features.forgeevents.CustomWorldRenderEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.math.MathHelper
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.GL_ONE
import org.lwjgl.opengl.GL11.GL_SRC_ALPHA
import org.lwjgl.opengl.GL14
import org.lwjgl.opengl.GL14.GL_FUNC_ADD
import org.lwjgl.opengl.GL14.GL_FUNC_REVERSE_SUBTRACT
import java.awt.Color

@SideOnly(Side.CLIENT)
object GatecrashRenderer {

    const val SECTIONS = 40
    const val WHEEL_SECTION = SECTIONS * 2 * Math.PI.toFloat()
    val BLANK = Color(255, 255, 255, 0)

    @SubscribeEvent
    fun tickDisplay(event: CustomWorldRenderEvent) {
        val player = Minecraft.getMinecraft().player ?: return

        GlStateManager.pushMatrix()

        GlStateManager.disableDepth()

        GlStateManager.disableCull()
        GlStateManager.enableAlpha()
        GlStateManager.enableBlend()
        GlStateManager.shadeModel(GL11.GL_SMOOTH)
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE)
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE)
        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.disableTexture2D()
        GlStateManager.enableColorMaterial()

        val look = player.getLook(event.partialTicks)
        val rY = MathHelper.atan2(look.z, look.x).toFloat()
        val rX = MathHelper.atan2(MathHelper.sqrt(look.x * look.x + look.z + look.z).toDouble(), look.y).toFloat()
        GlStateManager.translate(0f, 10f, 0f)
        GlStateManager.rotate(rY, 0f, 1f, 0f)
        GlStateManager.rotate(90 + rX, 1f, 0f, 0f)

        GL14.glBlendEquation(GL_FUNC_REVERSE_SUBTRACT)

        drawRing(0.4, 0.42, Color.WHITE, Color.BLACK)
        drawRing(0.1, 0.4, Color.GREEN, Color.WHITE)
        drawCircle(0.1, Color.GREEN, Color.GREEN)

        GL14.glBlendEquation(GL_FUNC_ADD)

        drawCircle(0.08, Color.WHITE, Color.WHITE)
        drawRing(0.08, 0.085, Color.WHITE, BLANK)

        GlStateManager.disableBlend()
        GlStateManager.enableDepth()
        GlStateManager.enableAlpha()
        GlStateManager.enableTexture2D()
        GlStateManager.disableColorMaterial()

        GlStateManager.enableDepth()
        GlStateManager.popMatrix()
    }

    private fun drawCircle(radius: Double, inner: Color, outer: Color) {
        val tess = Tessellator.getInstance()
        val vb = tess.buffer

        vb.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR)
        vb.pos(0.0, 0.0, 0.0).color(inner.red, inner.green, inner.blue, inner.alpha).endVertex()

        for (i in 0..SECTIONS) {
            val angle = i * WHEEL_SECTION
            val nextAngle = i * WHEEL_SECTION

            val x = MathHelper.cos(angle) * radius
            val y = MathHelper.sin(angle) * radius
            val nx = MathHelper.cos(nextAngle) * radius
            val ny = MathHelper.sin(nextAngle) * radius

            vb.pos(x, y, 0.0).color(outer.red, outer.green, outer.blue, outer.alpha).endVertex()
            vb.pos(nx, ny, 0.0).color(outer.red, outer.green, outer.blue, outer.alpha).endVertex()
        }

        tess.draw()
    }

    private fun drawRing(innerRadius: Double, outerRadius: Double, inner: Color, outer: Color) {
        val tess = Tessellator.getInstance()
        val vb = tess.buffer

        vb.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR)

        for (i in 0..SECTIONS) {
            val angle = i * WHEEL_SECTION

            val xIn = MathHelper.cos(angle) * innerRadius
            val yIn = MathHelper.sin(angle) * innerRadius

            val xOut = MathHelper.cos(angle) * outerRadius
            val yOut = MathHelper.sin(angle) * outerRadius

            vb.pos(xIn, yIn, 0.0).color(inner.red, inner.green, inner.blue, inner.alpha).endVertex()
            vb.pos(xOut, yOut, 0.0).color(outer.red, outer.green, outer.blue, outer.alpha).endVertex()
        }

        tess.draw()
    }
}
