package eladkay.quaritum.client.core;

import eladkay.quaritum.client.render.RenderChaosborn;
import eladkay.quaritum.common.core.CommonProxy;
import eladkay.quaritum.common.entity.EntityChaosborn;
import eladkay.quaritum.common.lib.LibMisc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleDragonBreath;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class ClientProxy extends CommonProxy {
    @Override
    public void pre(FMLPreInitializationEvent e) {
        super.pre(e);
        ModelHandler.preInit(LibMisc.MOD_ID);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        ModelHandler.init();
        RenderingRegistry.registerEntityRenderingHandler(EntityChaosborn.class, new RenderChaosborn());
        Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(new ResourceLocation("quaritum:entity/magicParticle"));
        Minecraft.getMinecraft().getTextureMapBlocks().loadTextureAtlas(Minecraft.getMinecraft().getResourceManager());
    }

    @Override
    public void spawnParticleMagixFX(World world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b) {
        //ParticleMagicLine particle = new ParticleMagicLine(world,x,y,z,vx,vy,vz,r,g,b);
        ParticleDragonBreath particle = new ParticleDragonBreath2(world, x, y, z, vx, vy, vz);
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    @SideOnly(Side.CLIENT)
    private static class ParticleMagicLine extends Particle {

        public double colorR = 0;
        public double colorG = 0;
        public double colorB = 0;
        public int lifetime = 8;
        public ResourceLocation texture = new ResourceLocation("quaritum:entity/magicParticle");
        Random random = new Random();

        public ParticleMagicLine(World worldIn, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b) {
            super(worldIn, x, y, z, 0, 0, 0);
            this.colorR = r;
            this.colorG = g;
            this.colorB = b;
            if (this.colorR > 1.0) {
                this.colorR = this.colorR / 255.0;
            }
            if (this.colorG > 1.0) {
                this.colorG = this.colorG / 255.0;
            }
            if (this.colorB > 1.0) {
                this.colorB = this.colorB / 255.0;
            }
            this.setRBGColorF(1, 1, 1);
            this.particleMaxAge = 8;
            this.particleGravity = 0.0f;
            this.motionX = (vx - x) / (double) this.particleMaxAge;
            this.motionY = (vy - y) / (double) this.particleMaxAge;
            this.motionZ = (vz - z) / (double) this.particleMaxAge;
            TextureAtlasSprite sprite = null;
            /*try {
                sprite = (TextureAtlasSprite)TextureAtlasSprite.class.getDeclaredMethod("makeAtlasSprite", ResourceLocation.class).invoke(sprite, texture);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }*/
            Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture.toString());
            this.setParticleTexture(sprite);
        }

        @Override
        public boolean isTransparent() {
            return true;
        }

        @Override
        public int getFXLayer() {
            return 1;
        }

        @Override
        public void onUpdate() {
            super.onUpdate();
            float lifeCoeff = ((float) this.particleMaxAge - (float) this.particleAge) / (float) this.particleMaxAge;
            this.particleRed = Math.min(1.0f, (float) colorR * (1.5f - lifeCoeff) + lifeCoeff);
            this.particleGreen = Math.min(1.0f, (float) colorG * (1.5f - lifeCoeff) + lifeCoeff);
            this.particleBlue = Math.min(1.0f, (float) colorB * (1.5f - lifeCoeff) + lifeCoeff);
            this.particleAlpha = lifeCoeff;
        }
    }

    private static class ParticleDragonBreath2 extends ParticleDragonBreath {

        public ParticleDragonBreath2(World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            super(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
