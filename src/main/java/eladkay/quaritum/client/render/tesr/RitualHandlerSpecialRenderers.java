package eladkay.quaritum.client.render.tesr;

import eladkay.quaritum.common.block.tile.TileEntityBlueprint;
import eladkay.quaritum.common.block.tile.TileEntityFoundationStone;
import eladkay.quaritum.common.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RitualHandlerSpecialRenderers {
    private static boolean always = false;
    private static void func_190055_a(Tessellator tesselator, VertexBuffer vb, double x1, double y1, double z1, double x2, double y2, double z2, int idk1, int idk2, int idk3) {
        GlStateManager.glLineWidth(2.0F);
        vb.begin(3, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(x1, y1, z1).color((float) idk2, (float) idk2, (float) idk2, 0.0F).endVertex();
        vb.pos(x1, y1, z1).color(idk2, idk2, idk2, idk1).endVertex();
        vb.pos(x2, y1, z1).color(idk2, idk3, idk3, idk1).endVertex();
        vb.pos(x2, y1, z2).color(idk2, idk2, idk2, idk1).endVertex();
        vb.pos(x1, y1, z2).color(idk2, idk2, idk2, idk1).endVertex();
        vb.pos(x1, y1, z1).color(idk3, idk3, idk2, idk1).endVertex();
        vb.pos(x1, y2, z1).color(idk3, idk2, idk3, idk1).endVertex();
        vb.pos(x2, y2, z1).color(idk2, idk2, idk2, idk1).endVertex();
        vb.pos(x2, y2, z2).color(idk2, idk2, idk2, idk1).endVertex();
        vb.pos(x1, y2, z2).color(idk2, idk2, idk2, idk1).endVertex();
        vb.pos(x1, y2, z1).color(idk2, idk2, idk2, idk1).endVertex();
        vb.pos(x1, y2, z2).color(idk2, idk2, idk2, idk1).endVertex();
        vb.pos(x1, y1, z2).color(idk2, idk2, idk2, idk1).endVertex();
        vb.pos(x2, y1, z2).color(idk2, idk2, idk2, idk1).endVertex();
        vb.pos(x2, y2, z2).color(idk2, idk2, idk2, idk1).endVertex();
        vb.pos(x2, y2, z1).color(idk2, idk2, idk2, idk1).endVertex();
        vb.pos(x2, y1, z1).color(idk2, idk2, idk2, idk1).endVertex();
        vb.pos(x2, y1, z1).color((float) idk2, (float) idk2, (float) idk2, 0.0F).endVertex();
        tesselator.draw();
        GlStateManager.glLineWidth(1.0F);
    }
    public static class BlueprintSpecialRenderer extends TileEntitySpecialRenderer<TileEntityBlueprint> {
        @Override
        public void renderTileEntityAt(TileEntityBlueprint te, double x, double y, double z, float partialTicks, int destroyStage) {
            if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() == null && !always)
                super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
            if ((Minecraft.getMinecraft().thePlayer.func_189808_dh() || Minecraft.getMinecraft().thePlayer.isSpectator()) &&
                    (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null &&
                            Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() == ModItems.debug) || always) {
                super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
                int i = 255;
                int j = 223;
                int k = 127;
                Tessellator tessellator = Tessellator.getInstance();
                VertexBuffer vertexbuffer = tessellator.getBuffer();
                GlStateManager.disableFog();
                GlStateManager.disableLighting();
                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                this.func_190053_a(true);
                func_190055_a(tessellator, vertexbuffer, x - 4, y, z - 4, x + 5, y, z + 5, 255, 223, 127);
                this.func_190053_a(false);
                GlStateManager.glLineWidth(1.0F);
                GlStateManager.enableLighting();
                GlStateManager.enableTexture2D();
                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                GlStateManager.enableFog();
            }
        }
    }
    public static class FoundationStoneSpecialRenderer extends TileEntitySpecialRenderer<TileEntityFoundationStone> {
        @Override
        public void renderTileEntityAt(TileEntityFoundationStone te, double x, double y, double z, float partialTicks, int destroyStage) {
            if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() == null && !always)
                super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
            if ((Minecraft.getMinecraft().thePlayer.func_189808_dh() || Minecraft.getMinecraft().thePlayer.isSpectator()) &&
                    (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null &&
                            Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() == ModItems.debug) || always) {
                super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
                int i = 255;
                int j = 223;
                int k = 127;
                Tessellator tessellator = Tessellator.getInstance();
                VertexBuffer vertexbuffer = tessellator.getBuffer();
                GlStateManager.disableFog();
                GlStateManager.disableLighting();
                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                this.func_190053_a(true);
                func_190055_a(tessellator, vertexbuffer, x - 12, y + 1, z - 12, x + 13, y + 25, z + 13, 255, 223, 127);
                this.func_190053_a(false);
                GlStateManager.glLineWidth(1.0F);
                GlStateManager.enableLighting();
                GlStateManager.enableTexture2D();
                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                GlStateManager.enableFog();
            }
        }
    }
}



