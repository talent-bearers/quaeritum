package eladkay.quaritum.client.core;

import eladkay.quaritum.client.render.RenderChaosborn;
import eladkay.quaritum.common.core.CommonProxy;
import eladkay.quaritum.common.entity.EntityChaosborn;
import eladkay.quaritum.common.lib.LibMisc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
        RenderingRegistry.registerEntityRenderingHandler(EntityChaosborn.class, (Render<? extends Entity>) new RenderChaosborn());
        Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(new ResourceLocation("quaritum:entity/magicParticle"));
        Minecraft.getMinecraft().getTextureMapBlocks().loadTextureAtlas(Minecraft.getMinecraft().getResourceManager());
    }

    @Override
    public void spawnParticleMagixFX(World world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b) {
        //ParticleMagicLine particle = new ParticleMagicLine(world,x,y,z,vx,vy,vz,r,g,b);
        //ParticleDragonBreath particle = new ParticleDragonBreath2(world, x, y, z, vx, vy, vz);
        //Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }
}
