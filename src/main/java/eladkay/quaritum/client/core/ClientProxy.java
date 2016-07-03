package eladkay.quaritum.client.core;

import eladkay.quaritum.api.lib.LibMisc;
import eladkay.quaritum.client.render.entity.RenderChaosborn;
import eladkay.quaritum.common.core.CommonProxy;
import eladkay.quaritum.common.entity.EntityChaosborn;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
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

        MinecraftForge.EVENT_BUS.register(new ClientTickHandler());

        RenderingRegistry.registerEntityRenderingHandler(EntityChaosborn.class, RenderChaosborn::new);
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
