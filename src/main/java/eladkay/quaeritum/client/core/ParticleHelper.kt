package eladkay.quaeritum.client.core

import eladkay.quaeritum.client.fx.FXBeam
import eladkay.quaeritum.client.fx.FXMagicLine
import eladkay.quaeritum.common.Quaeritum
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.*
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World

object ParticleHelper {
    fun summonParticleBetweenTwoVectors(worldIn: World, pos1: BlockPos, pos2: BlockPos) {
        if (pos1.subtract(pos2) === BlockPos.ORIGIN)
            Quaeritum.proxy.spawnStafflikeParticles(
                    worldIn,
                    pos1.x.toDouble(),
                    pos1.y.toDouble(),
                    pos1.z.toDouble())
        else {

        }

    }

    /*
         public void doRender(EntityDragon entity, double animX, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, animX, y, z, entityYaw, partialTicks);

        if (entity.healingEnderCrystal != null)
        {
            this.bindTexture(ENDERCRYSTAL_BEAM_TEXTURES);
            float f = MathHelper.sin(((float)entity.healingEnderCrystal.ticksExisted + partialTicks) * 0.2F) / 2.0F + 0.5F;
            f = (f * f + f) * 0.2F;
            renderCrystalBeams(animX, y, z, partialTicks, entity.posX + (entity.prevPosX - entity.posX) * (double)(1.0F - partialTicks), entity.posY + (entity.prevPosY - entity.posY) * (double)(1.0F - partialTicks), entity.posZ + (entity.prevPosZ - entity.posZ) * (double)(1.0F - partialTicks), entity.ticksExisted, entity.healingEnderCrystal.posX, (double)f + entity.healingEnderCrystal.posY, entity.healingEnderCrystal.posZ);
        }
    }

     */
    //Player animX, y, z, partial ticks (Use 1 for now), TE animX, y, z, total ticks (use world time), Item animX, y, z
    fun renderBeam(x: Double, y: Double, z: Double,
                   partialTicks: Float,
                   entityPosX: Double, entityPosY: Double, entityPosZ: Double,
                   entityTickExisted: Int,
                   destinationPosX: Double, destinationPosY: Double, destinationPosZ: Double) {
        val f = (destinationPosX - entityPosX).toFloat()
        val f1 = (destinationPosY - 1.0 - entityPosY).toFloat()
        val f2 = (destinationPosZ - entityPosZ).toFloat()
        val f3 = MathHelper.sqrt(f * f + f2 * f2)
        val f4 = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2)
        GlStateManager.pushMatrix()
        GlStateManager.translate(x.toFloat(), y.toFloat() + 2.0f, z.toFloat())
        GlStateManager.rotate((-Math.atan2(f2.toDouble(), f.toDouble())).toFloat() * (180f / Math.PI.toFloat()) - 90.0f, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate((-Math.atan2(f3.toDouble(), f1.toDouble())).toFloat() * (180f / Math.PI.toFloat()) - 90.0f, 1.0f, 0.0f, 0.0f)
        val tessellator = Tessellator.getInstance()
        val vertexbuffer = tessellator.buffer
        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableCull()
        GlStateManager.shadeModel(7425)
        val f5 = 0.0f - (entityTickExisted.toFloat() + partialTicks) * 0.01f
        val f6 = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2) / 32.0f - (entityTickExisted.toFloat() + partialTicks) * 0.01f
        vertexbuffer.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR)
        val i = 8

        for (j in 0..8) {
            val f7 = MathHelper.sin((j % 8).toFloat() * (Math.PI.toFloat() * 2f) / 8.0f) * 0.75f
            val f8 = MathHelper.cos((j % 8).toFloat() * (Math.PI.toFloat() * 2f) / 8.0f) * 0.75f
            val f9 = (j % 8).toFloat() / 8.0f
            vertexbuffer.pos((f7 * 0.2f).toDouble(), (f8 * 0.2f).toDouble(), 0.0).tex(f9.toDouble(), f5.toDouble()).color(0, 0, 0, 255).endVertex()
            vertexbuffer.pos(f7.toDouble(), f8.toDouble(), f4.toDouble()).tex(f9.toDouble(), f6.toDouble()).color(255, 255, 255, 255).endVertex()
        }

        tessellator.draw()
        GlStateManager.enableCull()
        GlStateManager.shadeModel(7424)
        RenderHelper.enableStandardItemLighting()
        GlStateManager.popMatrix()
    }

    fun spawnParticle(particleName: String, posX: Double, posY: Double, posZ: Double, motX: Double, motY: Double, motZ: Double, r: Double, g: Double, b: Double) {

        val minecraft = Minecraft.getMinecraft()
        if (minecraft != null && minecraft.renderViewEntity != null && minecraft.effectRenderer != null) {
            val var15 = minecraft.renderViewEntity!!.posX - posX
            val var17 = minecraft.renderViewEntity!!.posY - posY
            val var19 = minecraft.renderViewEntity!!.posZ - posZ
            var entityfx: Particle? = null
            val var22 = 16.0
            if (var15 * var15 + var17 * var17 + var19 * var19 <= var22 * var22) {
                if (particleName == "sphere") {
                    entityfx = FXMagicLine(minecraft.world, posX, posY, posZ, motX.toFloat().toDouble(), motY.toFloat().toDouble(), motZ.toFloat().toDouble(), r, g, b)
                }
                if (particleName == "spell") {
                    entityfx = ParticleSpell.Factory().createParticle(-1, minecraft.world, posX, posY, posZ, motX, motY, motZ)
                }
                if (particleName == "happyVillager") {
                    entityfx = ParticleSuspendedTown.HappyVillagerFactory().createParticle(-1, minecraft.world, posX, posY, posZ, motX, motY, motZ)
                    entityfx!!.setParticleTextureIndex(82)
                    entityfx.setRBGColorF(1.0f, 1.0f, 1.0f)
                }
                if (particleName == "magicCrit") {
                    entityfx = ParticleCrit.MagicFactory().createParticle(-1, Minecraft.getMinecraft().world, posX, posY, posZ, motX, motY, motZ)
                    entityfx!!.setRBGColorF(entityfx.redColorF * 0.3f, entityfx.greenColorF * 0.8f, entityfx.blueColorF)
                    entityfx.nextTextureIndexX()
                }
                if (particleName == "enchantmenttable") {
                    entityfx = ParticleEnchantmentTable.EnchantmentTable().createParticle(-1, Minecraft.getMinecraft().world, posX, posY, posZ, motX, motY, motZ)
                    entityfx!!.nextTextureIndexX()
                }
                if (particleName == "crit") {
                    entityfx = ParticleCrit.Factory().createParticle(-1, Minecraft.getMinecraft().world, posX, posY, posZ, motX, motY, motZ)
                    if (r != 0.0 || g != 0.0 || b != 0.0) {
                        entityfx!!.setRBGColorF(r.toFloat(), g.toFloat(), b.toFloat())
                    }
                }
                if (particleName == "square") {
                    entityfx = FXBeam(Minecraft.getMinecraft().world, posX, posY, posZ, r.toFloat(), g.toFloat(), b.toFloat())
                }
                if (particleName == "redBeamOfAwesome") {
                    entityfx = FXBeam(Minecraft.getMinecraft().world, posX, posY, posZ, 255f, 0f, 0f)
                }
                if (particleName == "squareLong") {
                    entityfx = FXBeam(Minecraft.getMinecraft().world, posX, posY, posZ, r.toFloat(), g.toFloat(), b.toFloat(), true)
                }

                Quaeritum.proxy.spawnParticle(entityfx ?: throw IllegalArgumentException(particleName), Minecraft.getMinecraft().world)
            }
        }

    }
}

