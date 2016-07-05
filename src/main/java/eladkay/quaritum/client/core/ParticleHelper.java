package eladkay.quaritum.client.core;

import eladkay.quaritum.client.fx.FXBeam;
import eladkay.quaritum.client.fx.FXMagicLine;
import eladkay.quaritum.common.Quartium;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleHelper {
    public static void summonParticleBetweenTwoVectors(World worldIn, BlockPos pos1, BlockPos pos2) {
        if (pos1.subtract(pos2) == BlockPos.ORIGIN) Quartium.proxy.spawnStafflikeParticles(
                worldIn,
                pos1.getX(),
                pos1.getY(),
                pos1.getZ());
        else {

        }

    }

    /*
         public void doRender(EntityDragon entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);

        if (entity.healingEnderCrystal != null)
        {
            this.bindTexture(ENDERCRYSTAL_BEAM_TEXTURES);
            float f = MathHelper.sin(((float)entity.healingEnderCrystal.ticksExisted + partialTicks) * 0.2F) / 2.0F + 0.5F;
            f = (f * f + f) * 0.2F;
            renderCrystalBeams(x, y, z, partialTicks, entity.posX + (entity.prevPosX - entity.posX) * (double)(1.0F - partialTicks), entity.posY + (entity.prevPosY - entity.posY) * (double)(1.0F - partialTicks), entity.posZ + (entity.prevPosZ - entity.posZ) * (double)(1.0F - partialTicks), entity.ticksExisted, entity.healingEnderCrystal.posX, (double)f + entity.healingEnderCrystal.posY, entity.healingEnderCrystal.posZ);
        }
    }

     */
    //Player x, y, z, partial ticks (Use 1 for now), TE x, y, z, total ticks (use world time), Item x, y, z
    public static void renderBeam(double x, double y, double z,
                                          float partialTicks,
                                          double entityPosX, double entityPosY, double entityPosZ,
                                          int entityTickExisted,
                                          double destinationPosX, double destinationPosY, double destinationPosZ) {
        float f = (float) (destinationPosX - entityPosX);
        float f1 = (float) (destinationPosY - 1.0D - entityPosY);
        float f2 = (float) (destinationPosZ - entityPosZ);
        float f3 = MathHelper.sqrt_float(f * f + f2 * f2);
        float f4 = MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 2.0F, (float) z);
        GlStateManager.rotate((float) (-Math.atan2((double) f2, (double) f)) * (180F / (float) Math.PI) - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (-Math.atan2((double) f3, (double) f1)) * (180F / (float) Math.PI) - 90.0F, 1.0F, 0.0F, 0.0F);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.shadeModel(7425);
        float f5 = 0.0F - ((float) entityTickExisted + partialTicks) * 0.01F;
        float f6 = MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2) / 32.0F - ((float) entityTickExisted + partialTicks) * 0.01F;
        vertexbuffer.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);
        int i = 8;

        for (int j = 0; j <= 8; ++j) {
            float f7 = MathHelper.sin((float) (j % 8) * ((float) Math.PI * 2F) / 8.0F) * 0.75F;
            float f8 = MathHelper.cos((float) (j % 8) * ((float) Math.PI * 2F) / 8.0F) * 0.75F;
            float f9 = (float) (j % 8) / 8.0F;
            vertexbuffer.pos((double) (f7 * 0.2F), (double) (f8 * 0.2F), 0.0D).tex((double) f9, (double) f5).color(0, 0, 0, 255).endVertex();
            vertexbuffer.pos((double) f7, (double) f8, (double) f4).tex((double) f9, (double) f6).color(255, 255, 255, 255).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableCull();
        GlStateManager.shadeModel(7424);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
    public static void spawnParticle(String particleName, double posX, double posY, double posZ, double motX, double motY, double motZ, double r, double g, double b) {

        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft != null && minecraft.getRenderViewEntity() != null && minecraft.effectRenderer != null) {
            double var15 = minecraft.getRenderViewEntity().posX - posX;
            double var17 = minecraft.getRenderViewEntity().posY - posY;
            double var19 = minecraft.getRenderViewEntity().posZ - posZ;
            Particle entityfx = null;
            double var22 = 16.0D;
            if (!(var15 * var15 + var17 * var17 + var19 * var19 > var22 * var22)) {
                if (particleName.equals("sphere")) {
                    entityfx = new FXMagicLine(minecraft.theWorld, posX, posY, posZ, (float) motX, (float) motY, (float) motZ, r, g, b);
                }
                if (particleName.equals("spell")) {
                    entityfx = new ParticleSpell.Factory().getEntityFX(-1, minecraft.theWorld, posX, posY, posZ, (float) motX, (float) motY, (float) motZ);
                }
                if (particleName.equals("happyVillager")) {
                    entityfx = new ParticleSuspendedTown.HappyVillagerFactory().getEntityFX(-1, minecraft.theWorld, posX, posY, posZ, (float) motX, (float) motY, (float) motZ);
                    entityfx.setParticleTextureIndex(82);
                    entityfx.setRBGColorF(1.0F, 1.0F, 1.0F);
                }
                if (particleName.equals("magicCrit")) {
                    entityfx = new ParticleCrit.MagicFactory().getEntityFX(-1, Minecraft.getMinecraft().theWorld, posX, posY, posZ, (float) motX, (float) motY, (float) motZ);
                    entityfx.setRBGColorF(entityfx.getRedColorF() * 0.3F, entityfx.getGreenColorF() * 0.8F, entityfx.getBlueColorF());
                    entityfx.nextTextureIndexX();
                }
                if (particleName.equals("enchantmenttable")) {
                    entityfx = new ParticleEnchantmentTable.EnchantmentTable().getEntityFX(-1, Minecraft.getMinecraft().theWorld, posX, posY, posZ, (float) motX, (float) motY, (float) motZ);
                    entityfx.nextTextureIndexX();
                }
                if (particleName.equals("crit")) {
                    entityfx = new ParticleCrit.Factory().getEntityFX(-1, Minecraft.getMinecraft().theWorld, posX, posY, posZ, motX, motY, motZ);
                    if (r != 0 || g != 0 || b != 0) {
                        entityfx.setRBGColorF((float) r, (float) g, (float) b);
                    }
                }
                if (particleName.equals("square")) {
                    entityfx = new FXBeam(Minecraft.getMinecraft().theWorld, posX, posY, posZ, (float) r, (float) g, (float) b);
                }
                if (particleName.equals("redBeamOfAwesome")) {
                    entityfx = new FXBeam(Minecraft.getMinecraft().theWorld, posX, posY, posZ, 255, 0, 0);
                }
                if (particleName.equals("squareLong")) {
                    entityfx = new FXBeam(Minecraft.getMinecraft().theWorld, posX, posY, posZ, (float) r, (float) g, (float) b, true);
                }

                Quartium.proxy.spawnParticle(entityfx, Minecraft.getMinecraft().theWorld);
            }
        }

    }
}

