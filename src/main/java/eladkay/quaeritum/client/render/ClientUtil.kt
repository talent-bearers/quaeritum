package eladkay.quaeritum.client.render

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.EnumFacing
import net.minecraftforge.fluids.FluidStack
import org.lwjgl.opengl.GL11

/**
 * @author WireSegal
 * Created at 7:55 PM on 11/27/17.
 */
object ClientUtil {
    fun renderFluidCuboid(fluid: FluidStack, x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double) {
        val color = fluid.fluid.getColor(fluid)
        renderFluidCuboid(fluid, x1, y1, z1, x2, y2, z2, color)
    }

    fun renderFluidCuboid(fluid: FluidStack, x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double, color: Int) {
        val renderer = Tessellator.getInstance().buffer
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR)
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
        val still = Minecraft.getMinecraft().textureMapBlocks.getTextureExtry(fluid.fluid.getStill(fluid).toString())
        val flowing = Minecraft.getMinecraft().textureMapBlocks.getTextureExtry(fluid.fluid.getFlowing(fluid).toString())
        putTexturedQuad(renderer, still, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.DOWN, color, false)
        putTexturedQuad(renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.NORTH, color, true)
        putTexturedQuad(renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.EAST, color, true)
        putTexturedQuad(renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.SOUTH, color, true)
        putTexturedQuad(renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.WEST, color, true)
        putTexturedQuad(renderer, still, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.UP, color, false)
        Tessellator.getInstance().draw()
    }

    fun putTexturedQuad(renderer: BufferBuilder, sprite: TextureAtlasSprite?, x: Double, y: Double, z: Double, w: Double, h: Double, d: Double, face: EnumFacing, color: Int, flowing: Boolean) {
        val a = color shr 24 and 0xFF
        val r = color shr 16 and 0xFF
        val g = color shr 8 and 0xFF
        val b = color and 0xFF
        putTexturedQuad(renderer, sprite, x, y, z, w, h, d, face, r, g, b, a, flowing)
    }

    fun putTexturedQuad(renderer: BufferBuilder, sprite: TextureAtlasSprite?, x: Double, y: Double, z: Double, w: Double, h: Double, d: Double, face: EnumFacing, r: Int, g: Int, b: Int, a: Int, flowing: Boolean) {
        if (sprite == null) {
            return
        }

        val minU: Double
        val maxU: Double
        val minV: Double
        val maxV: Double
        var size = 16.0

        if (flowing) {
            size = 8.0
        }

        val x1 = x
        val x2 = x + w
        val y1 = y
        val y2 = y + h
        val z1 = z
        val z2 = z + d
        val xt1 = x1 % 1.0
        var xt2 = xt1 + w

        while (xt2 > 1f) {
            xt2 -= 1.0
        }

        var yt1 = y1 % 1.0
        var yt2 = yt1 + h

        while (yt2 > 1f) {
            yt2 -= 1.0
        }

        val zt1 = z1 % 1.0
        var zt2 = zt1 + d

        while (zt2 > 1f) {
            zt2 -= 1.0
        }

        if (flowing) {
            val tmp = 1.0 - yt1
            yt1 = 1.0 - yt2
            yt2 = tmp
        }

        when (face) {
            EnumFacing.DOWN, EnumFacing.UP -> {
                minU = sprite.getInterpolatedU(xt1 * size).toDouble()
                maxU = sprite.getInterpolatedU(xt2 * size).toDouble()
                minV = sprite.getInterpolatedV(zt1 * size).toDouble()
                maxV = sprite.getInterpolatedV(zt2 * size).toDouble()
            }
            EnumFacing.NORTH, EnumFacing.SOUTH -> {
                minU = sprite.getInterpolatedU(xt2 * size).toDouble()
                maxU = sprite.getInterpolatedU(xt1 * size).toDouble()
                minV = sprite.getInterpolatedV(yt1 * size).toDouble()
                maxV = sprite.getInterpolatedV(yt2 * size).toDouble()
            }
            EnumFacing.WEST, EnumFacing.EAST -> {
                minU = sprite.getInterpolatedU(zt2 * size).toDouble()
                maxU = sprite.getInterpolatedU(zt1 * size).toDouble()
                minV = sprite.getInterpolatedV(yt1 * size).toDouble()
                maxV = sprite.getInterpolatedV(yt2 * size).toDouble()
            }
            else -> {
                minU = sprite.minU.toDouble()
                maxU = sprite.maxU.toDouble()
                minV = sprite.minV.toDouble()
                maxV = sprite.maxV.toDouble()
            }
        }

        when (face) {
            EnumFacing.DOWN -> {
                renderer.pos(x1, y1, z1).tex(minU, minV).color(r, g, b, a).endVertex()
                renderer.pos(x2, y1, z1).tex(maxU, minV).color(r, g, b, a).endVertex()
                renderer.pos(x2, y1, z2).tex(maxU, maxV).color(r, g, b, a).endVertex()
                renderer.pos(x1, y1, z2).tex(minU, maxV).color(r, g, b, a).endVertex()
            }
            EnumFacing.UP -> {
                renderer.pos(x1, y2, z1).tex(minU, minV).color(r, g, b, a).endVertex()
                renderer.pos(x1, y2, z2).tex(minU, maxV).color(r, g, b, a).endVertex()
                renderer.pos(x2, y2, z2).tex(maxU, maxV).color(r, g, b, a).endVertex()
                renderer.pos(x2, y2, z1).tex(maxU, minV).color(r, g, b, a).endVertex()
            }
            EnumFacing.NORTH -> {
                renderer.pos(x1, y1, z1).tex(minU, maxV).color(r, g, b, a).endVertex()
                renderer.pos(x1, y2, z1).tex(minU, minV).color(r, g, b, a).endVertex()
                renderer.pos(x2, y2, z1).tex(maxU, minV).color(r, g, b, a).endVertex()
                renderer.pos(x2, y1, z1).tex(maxU, maxV).color(r, g, b, a).endVertex()
            }
            EnumFacing.SOUTH -> {
                renderer.pos(x1, y1, z2).tex(maxU, maxV).color(r, g, b, a).endVertex()
                renderer.pos(x2, y1, z2).tex(minU, maxV).color(r, g, b, a).endVertex()
                renderer.pos(x2, y2, z2).tex(minU, minV).color(r, g, b, a).endVertex()
                renderer.pos(x1, y2, z2).tex(maxU, minV).color(r, g, b, a).endVertex()
            }
            EnumFacing.WEST -> {
                renderer.pos(x1, y1, z1).tex(maxU, maxV).color(r, g, b, a).endVertex()
                renderer.pos(x1, y1, z2).tex(minU, maxV).color(r, g, b, a).endVertex()
                renderer.pos(x1, y2, z2).tex(minU, minV).color(r, g, b, a).endVertex()
                renderer.pos(x1, y2, z1).tex(maxU, minV).color(r, g, b, a).endVertex()
            }
            EnumFacing.EAST -> {
                renderer.pos(x2, y1, z1).tex(minU, maxV).color(r, g, b, a).endVertex()
                renderer.pos(x2, y2, z1).tex(minU, minV).color(r, g, b, a).endVertex()
                renderer.pos(x2, y2, z2).tex(maxU, minV).color(r, g, b, a).endVertex()
                renderer.pos(x2, y1, z2).tex(maxU, maxV).color(r, g, b, a).endVertex()
            }
        }
    }
}
