/**
 * This class was created by . It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 *
 * File Created @ [Jan 19, 2014, 5:40:38 PM (GMT)]
 */
package eladkay.quaeritum.client.core

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11
import java.util.*

object RenderHelper {

    fun renderTooltip(x: Int, y: Int, tooltipData: List<String>) {
        val color = 0x505000ff
        val color2 = 0xf0100010.toInt()

        renderTooltip(x, y, tooltipData, color, color2)
    }

    fun renderTooltipOrange(x: Int, y: Int, tooltipData: List<String>) {
        val color = 0x50a06600
        val color2 = 0xf01e1200.toInt()

        renderTooltip(x, y, tooltipData, color, color2)
    }

    fun renderTooltipGreen(x: Int, y: Int, tooltipData: List<String>) {
        val color = 0x5000a000
        val color2 = 0xf0001e00.toInt()

        renderTooltip(x, y, tooltipData, color, color2)
    }

    fun renderTooltip(x: Int, y: Int, tooltipData: List<String>, color: Int, color2: Int) {
        val lighting = GL11.glGetBoolean(GL11.GL_LIGHTING)
        if (lighting)
            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting()

        if (!tooltipData.isEmpty()) {
            var var5 = 0
            var var6: Int
            var var7: Int
            val fontRenderer = Minecraft.getMinecraft().fontRendererObj
            var6 = 0
            while (var6 < tooltipData.size) {
                var7 = fontRenderer.getStringWidth(tooltipData[var6])
                if (var7 > var5)
                    var5 = var7
                ++var6
            }
            var6 = x + 12
            var7 = y - 12
            var var9 = 8
            if (tooltipData.size > 1)
                var9 += 2 + (tooltipData.size - 1) * 10
            val z = 300f
            drawGradientRect(var6 - 3, var7 - 4, z, var6 + var5 + 3, var7 - 3, color2, color2)
            drawGradientRect(var6 - 3, var7 + var9 + 3, z, var6 + var5 + 3, var7 + var9 + 4, color2, color2)
            drawGradientRect(var6 - 3, var7 - 3, z, var6 + var5 + 3, var7 + var9 + 3, color2, color2)
            drawGradientRect(var6 - 4, var7 - 3, z, var6 - 3, var7 + var9 + 3, color2, color2)
            drawGradientRect(var6 + var5 + 3, var7 - 3, z, var6 + var5 + 4, var7 + var9 + 3, color2, color2)
            val var12 = color and 0xFFFFFF shr 1 or (color and -16777216)
            drawGradientRect(var6 - 3, var7 - 3 + 1, z, var6 - 3 + 1, var7 + var9 + 3 - 1, color, var12)
            drawGradientRect(var6 + var5 + 2, var7 - 3 + 1, z, var6 + var5 + 3, var7 + var9 + 3 - 1, color, var12)
            drawGradientRect(var6 - 3, var7 - 3, z, var6 + var5 + 3, var7 - 3 + 1, color, color)
            drawGradientRect(var6 - 3, var7 + var9 + 2, z, var6 + var5 + 3, var7 + var9 + 3, var12, var12)

            GlStateManager.disableDepth()
            for (var13 in tooltipData.indices) {
                val var14 = tooltipData[var13]
                fontRenderer.drawStringWithShadow(var14, var6.toFloat(), var7.toFloat(), -1)
                if (var13 == 0)
                    var7 += 2
                var7 += 10
            }
            GlStateManager.enableDepth()
        }
        if (!lighting)
            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting()
        GlStateManager.color(1f, 1f, 1f, 1f)
    }

    fun drawGradientRect(par1: Int, par2: Int, z: Float, par3: Int, par4: Int, par5: Int, par6: Int) {
        val var7 = (par5 shr 24 and 255) / 255f
        val var8 = (par5 shr 16 and 255) / 255f
        val var9 = (par5 shr 8 and 255) / 255f
        val var10 = (par5 and 255) / 255f
        val var11 = (par6 shr 24 and 255) / 255f
        val var12 = (par6 shr 16 and 255) / 255f
        val var13 = (par6 shr 8 and 255) / 255f
        val var14 = (par6 and 255) / 255f
        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.disableAlpha()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GlStateManager.shadeModel(GL11.GL_SMOOTH)
        val var15 = Tessellator.getInstance()
        var15.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR)
        var15.buffer.pos(par3.toDouble(), par2.toDouble(), z.toDouble()).color(var8, var9, var10, var7).endVertex()
        var15.buffer.pos(par1.toDouble(), par2.toDouble(), z.toDouble()).color(var8, var9, var10, var7).endVertex()
        var15.buffer.pos(par1.toDouble(), par4.toDouble(), z.toDouble()).color(var12, var13, var14, var11).endVertex()
        var15.buffer.pos(par3.toDouble(), par4.toDouble(), z.toDouble()).color(var12, var13, var14, var11).endVertex()
        var15.draw()
        GlStateManager.shadeModel(GL11.GL_FLAT)
        GlStateManager.disableBlend()
        GlStateManager.enableAlpha()
        GlStateManager.enableTexture2D()
    }

    @JvmOverloads fun drawTexturedModalRect(par1: Int, par2: Int, z: Float, par3: Int, par4: Int, par5: Int, par6: Int, f: Float = 0.00390625f, f1: Float = 0.00390625f) {
        val tessellator = Tessellator.getInstance()
        tessellator.buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
        tessellator.buffer.pos(par1.toDouble(), (par2 + par6).toDouble(), z.toDouble()).tex((par3 * f).toDouble(), ((par4 + par6) * f1).toDouble()).endVertex()
        tessellator.buffer.pos((par1 + par5).toDouble(), (par2 + par6).toDouble(), z.toDouble()).tex(((par3 + par5) * f).toDouble(), ((par4 + par6) * f1).toDouble()).endVertex()
        tessellator.buffer.pos((par1 + par5).toDouble(), par2.toDouble(), z.toDouble()).tex(((par3 + par5) * f).toDouble(), (par4 * f1).toDouble()).endVertex()
        tessellator.buffer.pos(par1.toDouble(), par2.toDouble(), z.toDouble()).tex((par3 * f).toDouble(), (par4 * f1).toDouble()).endVertex()
        tessellator.draw()
    }

    fun renderStar(color: Int, xScale: Float, yScale: Float, zScale: Float, seed: Long) {
        val tessellator = Tessellator.getInstance()

        var ticks = (Minecraft.getMinecraft().theWorld.totalWorldTime % 200).toInt()
        if (ticks >= 100)
            ticks = 200 - ticks - 1

        val f1 = ticks / 200f
        var f2 = 0f
        if (f1 > 0.7f)
            f2 = (f1 - 0.7f) / 0.2f
        val random = Random(seed)

        GlStateManager.pushMatrix()
        GlStateManager.disableTexture2D()
        GlStateManager.shadeModel(GL11.GL_SMOOTH)
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE)
        GlStateManager.disableAlpha()
        GlStateManager.enableCull()
        GlStateManager.depthMask(false)
        GlStateManager.scale(xScale, yScale, zScale)

        var i = 0
        while (i < (f1 + f1 * f1) / 2f * 90f + 30f) {
            GlStateManager.rotate(random.nextFloat() * 360f, 1f, 0f, 0f)
            GlStateManager.rotate(random.nextFloat() * 360f, 0f, 1f, 0f)
            GlStateManager.rotate(random.nextFloat() * 360f, 0f, 0f, 1f)
            GlStateManager.rotate(random.nextFloat() * 360f, 1f, 0f, 0f)
            GlStateManager.rotate(random.nextFloat() * 360f, 0f, 1f, 0f)
            GlStateManager.rotate(random.nextFloat() * 360f + f1 * 90f, 0f, 0f, 1f)
            tessellator.buffer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR)
            val f3 = random.nextFloat() * 20f + 5f + f2 * 10f
            val f4 = random.nextFloat() * 2f + 1f + f2 * 2f
            val r = (color and 0xFF0000 shr 16) / 255f
            val g = (color and 0xFF00 shr 8) / 255f
            val b = (color and 0xFF) / 255f
            tessellator.buffer.pos(0.0, 0.0, 0.0).color(r, g, b, 1f - f2).endVertex()
            tessellator.buffer.pos(-0.866 * f4, f3.toDouble(), (-0.5f * f4).toDouble()).color(0, 0, 0, 0).endVertex()
            tessellator.buffer.pos(0.866 * f4, f3.toDouble(), (-0.5f * f4).toDouble()).color(0, 0, 0, 0).endVertex()
            tessellator.buffer.pos(0.0, f3.toDouble(), (1f * f4).toDouble()).color(0, 0, 0, 0).endVertex()
            tessellator.buffer.pos(-0.866 * f4, f3.toDouble(), (-0.5f * f4).toDouble()).color(0, 0, 0, 0).endVertex()
            tessellator.draw()
            i++
        }

        GlStateManager.depthMask(true)
        GlStateManager.disableCull()
        GlStateManager.disableBlend()
        GlStateManager.shadeModel(GL11.GL_FLAT)
        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.enableTexture2D()
        GlStateManager.enableAlpha()
        GlStateManager.popMatrix()
    }


    fun getKeyDisplayString(keyName: String): String? {
        var key: String? = null
        val keys = Minecraft.getMinecraft().gameSettings.keyBindings
        for (otherKey in keys)
            if (otherKey.keyDescription == keyName) {
                key = otherKey.displayName
                break
            }

        return key
    }
}
