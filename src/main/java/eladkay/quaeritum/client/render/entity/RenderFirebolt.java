package eladkay.quaeritum.client.render.entity;

import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
import eladkay.quaeritum.common.entity.EntityFirebolt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderFirebolt extends Render<EntityFirebolt> {

    public RenderFirebolt(RenderManager renderManager) {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityFirebolt entity) {
        return null;
    }

    @Override
    public void doRender(EntityFirebolt entity, double x, double y, double z, float entityYaw, float partialTicks) {

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.disableCull();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableLighting();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.disableStandardItemLighting();
        Minecraft.getMinecraft().entityRenderer.disableLightmap();

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();

        Vec3d norm = new Vec3d(entity.motionX, entity.motionY, entity.motionZ).normalize();
        GlStateManager.rotate(90, 0, 1, 0);
        GlStateManager.rotate((ClientTickHandler.getTicks() + partialTicks) * 50.0f, (float) norm.x, (float) norm.y, (float) norm.z);
        double s = 0.25;
        // TOP
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(-s, s, -s).color(255, 0, 0, 255).endVertex();
        buffer.pos(-s, s, s).color(255, 0, 0, 255).endVertex();
        buffer.pos(s, s, s).color(255, 0, 0, 255).endVertex();
        buffer.pos(s, s, -s).color(255, 0, 0, 255).endVertex();
        tess.draw();

        // BOTTOM
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(-s, -s, -s).color(0, 255, 0, 255).endVertex();
        buffer.pos(-s, -s, s).color(0, 255, 0, 255).endVertex();
        buffer.pos(s, -s, s).color(0, 255, 0, 255).endVertex();
        buffer.pos(s, -s, -s).color(0, 255, 0, 255).endVertex();
        tess.draw();

        // TO THE RIGHT
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(-s, -s, s).color(0, 0, 255, 255).endVertex();
        buffer.pos(-s, s, s).color(0, 0, 255, 255).endVertex();
        buffer.pos(s, s, s).color(0, 0, 255, 255).endVertex();
        buffer.pos(s, -s, s).color(0, 0, 255, 255).endVertex();
        tess.draw();

        // TO THE LEFT
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(-s, -s, -s).color(255, 255, 0, 255).endVertex();
        buffer.pos(-s, s, -s).color(255, 255, 0, 255).endVertex();
        buffer.pos(s, s, -s).color(255, 255, 0, 255).endVertex();
        buffer.pos(s, -s, -s).color(255, 255, 0, 255).endVertex();
        tess.draw();

        // FRONT
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(s, -s, -s).color(255, 0, 255, 255).endVertex();
        buffer.pos(s, s, -s).color(255, 0, 255, 255).endVertex();
        buffer.pos(s, s, s).color(255, 0, 255, 255).endVertex();
        buffer.pos(s, -s, s).color(255, 0, 255, 255).endVertex();
        tess.draw();

        // BACK
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(-s, -s, -s).color(0, 0, 255, 255).endVertex();
        buffer.pos(-s, s, -s).color(0, 0, 255, 255).endVertex();
        buffer.pos(-s, s, s).color(0, 0, 255, 255).endVertex();
        buffer.pos(-s, -s, s).color(0, 0, 255, 255).endVertex();
        tess.draw();

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
