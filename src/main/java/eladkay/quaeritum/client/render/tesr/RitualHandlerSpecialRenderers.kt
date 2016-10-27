package eladkay.quaeritum.client.render.tesr

import eladkay.quaeritum.common.block.tile.TileEntityBlueprint
import eladkay.quaeritum.common.block.tile.TileEntityFoundationStone
import eladkay.quaeritum.common.item.ModItems
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.VertexBuffer
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats

object RitualHandlerSpecialRenderers {
    private val always = false
    private fun func_190055_a(tesselator: Tessellator, vb: VertexBuffer, x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double, idk1: Int, idk2: Int, idk3: Int) {
        GlStateManager.glLineWidth(2.0f)
        vb.begin(3, DefaultVertexFormats.POSITION_COLOR)
        vb.pos(x1, y1, z1).color(idk2.toFloat(), idk2.toFloat(), idk2.toFloat(), 0.0f).endVertex()
        vb.pos(x1, y1, z1).color(idk2, idk2, idk2, idk1).endVertex()
        vb.pos(x2, y1, z1).color(idk2, idk3, idk3, idk1).endVertex()
        vb.pos(x2, y1, z2).color(idk2, idk2, idk2, idk1).endVertex()
        vb.pos(x1, y1, z2).color(idk2, idk2, idk2, idk1).endVertex()
        vb.pos(x1, y1, z1).color(idk3, idk3, idk2, idk1).endVertex()
        vb.pos(x1, y2, z1).color(idk3, idk2, idk3, idk1).endVertex()
        vb.pos(x2, y2, z1).color(idk2, idk2, idk2, idk1).endVertex()
        vb.pos(x2, y2, z2).color(idk2, idk2, idk2, idk1).endVertex()
        vb.pos(x1, y2, z2).color(idk2, idk2, idk2, idk1).endVertex()
        vb.pos(x1, y2, z1).color(idk2, idk2, idk2, idk1).endVertex()
        vb.pos(x1, y2, z2).color(idk2, idk2, idk2, idk1).endVertex()
        vb.pos(x1, y1, z2).color(idk2, idk2, idk2, idk1).endVertex()
        vb.pos(x2, y1, z2).color(idk2, idk2, idk2, idk1).endVertex()
        vb.pos(x2, y2, z2).color(idk2, idk2, idk2, idk1).endVertex()
        vb.pos(x2, y2, z1).color(idk2, idk2, idk2, idk1).endVertex()
        vb.pos(x2, y1, z1).color(idk2, idk2, idk2, idk1).endVertex()
        vb.pos(x2, y1, z1).color(idk2.toFloat(), idk2.toFloat(), idk2.toFloat(), 0.0f).endVertex()
        tesselator.draw()
        GlStateManager.glLineWidth(1.0f)
    }

    class BlueprintSpecialRenderer : TileEntitySpecialRenderer<TileEntityBlueprint>() {
        override fun renderTileEntityAt(te: TileEntityBlueprint, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int) {
            if ((Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() == null || Minecraft.getMinecraft().thePlayer.inventory == null || Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem()!!.item == null) && !always)
                super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage)
            if (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null && Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem()!!.item === ModItems.debug || always) {
                super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage)
                val i = 255
                val j = 223
                val k = 127
                val tessellator = Tessellator.getInstance()
                val vertexbuffer = tessellator.buffer
                GlStateManager.disableFog()
                GlStateManager.disableLighting()
                GlStateManager.disableTexture2D()
                GlStateManager.enableBlend()
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)
                this.func_190053_a(true)
                func_190055_a(tessellator, vertexbuffer, x - 4, y, z - 4, x + 5, y, z + 5, 255, 223, 127)
                this.func_190053_a(false)
                GlStateManager.glLineWidth(1.0f)
                GlStateManager.enableLighting()
                GlStateManager.enableTexture2D()
                GlStateManager.enableDepth()
                GlStateManager.depthMask(true)
                GlStateManager.enableFog()
            }
        }
    }

    class FoundationStoneSpecialRenderer : TileEntitySpecialRenderer<TileEntityFoundationStone>() {
        override fun renderTileEntityAt(te: TileEntityFoundationStone, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int) {
            if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() == null && !always)
                super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage)
            if (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null && Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem()!!.item === ModItems.debug || always) {
                super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage)
                val i = 255
                val j = 223
                val k = 127
                val tessellator = Tessellator.getInstance()
                val vertexbuffer = tessellator.buffer
                GlStateManager.disableFog()
                GlStateManager.disableLighting()
                GlStateManager.disableTexture2D()
                GlStateManager.enableBlend()
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)
                this.func_190053_a(true)
                func_190055_a(tessellator, vertexbuffer, x - 12, y + 1, z - 12, x + 13, y + 25, z + 13, 255, 223, 127)
                this.func_190053_a(false)
                GlStateManager.glLineWidth(1.0f)
                GlStateManager.enableLighting()
                GlStateManager.enableTexture2D()
                GlStateManager.enableDepth()
                GlStateManager.depthMask(true)
                GlStateManager.enableFog()
            }
        }
    }
}



