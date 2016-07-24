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
    private static void func_190055_a(Tessellator p_190055_1_, VertexBuffer p_190055_2_, double p_190055_3_, double p_190055_5_, double p_190055_7_, double p_190055_9_, double p_190055_11_, double p_190055_13_, int p_190055_15_, int p_190055_16_, int p_190055_17_) {
        GlStateManager.glLineWidth(2.0F);
        p_190055_2_.begin(3, DefaultVertexFormats.POSITION_COLOR);
        p_190055_2_.pos(p_190055_3_, p_190055_5_, p_190055_7_).color((float) p_190055_16_, (float) p_190055_16_, (float) p_190055_16_, 0.0F).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_5_, p_190055_7_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_5_, p_190055_7_).color(p_190055_16_, p_190055_17_, p_190055_17_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_5_, p_190055_13_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_5_, p_190055_13_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_5_, p_190055_7_).color(p_190055_17_, p_190055_17_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_11_, p_190055_7_).color(p_190055_17_, p_190055_16_, p_190055_17_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_11_, p_190055_7_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_11_, p_190055_13_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_11_, p_190055_13_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_11_, p_190055_7_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_11_, p_190055_13_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_3_, p_190055_5_, p_190055_13_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_5_, p_190055_13_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_11_, p_190055_13_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_11_, p_190055_7_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_5_, p_190055_7_).color(p_190055_16_, p_190055_16_, p_190055_16_, p_190055_15_).endVertex();
        p_190055_2_.pos(p_190055_9_, p_190055_5_, p_190055_7_).color((float) p_190055_16_, (float) p_190055_16_, (float) p_190055_16_, 0.0F).endVertex();
        p_190055_1_.draw();
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



