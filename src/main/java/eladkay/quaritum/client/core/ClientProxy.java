package eladkay.quaritum.client.core;

import eladkay.quaritum.api.lib.LibMisc;
import eladkay.quaritum.client.fx.FXMagicLine;
import eladkay.quaritum.client.fx.FXWisp;
import eladkay.quaritum.client.render.entity.RenderChaosborn;
import eladkay.quaritum.common.core.CommonProxy;
import eladkay.quaritum.common.entity.EntityChaosborn;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

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
    public void addToVariantCache(ModelHandler.IVariantHolder o) {
        ModelHandler.variantCache.add(o);
    }

    @Override
    public void spawnParticleMagixFX(World world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b) {
        if (!doParticle(world))
            return;
        FXMagicLine particle = new FXMagicLine(world, x, y, z, vx, vy, vz, r, g, b);
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    @Override
    public void spawnParticle(Particle particle, World world) {
        if (!doParticle(world))
            return;
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    @Override
    public void spawnStafflikeParticles(World world, double x, double y, double z) {
        spawnParticleMagixFX(world, x + 2 * (world.rand.nextFloat() - 0.5), y + 2.0 * (world.rand.nextFloat() - 0.5) + 1.0, z + 2.0 * (world.rand.nextFloat() - 0.5), x, y + 1.0, z, 0, 0, 0);
    }

    //This doesn't work
    @Deprecated
    @Override
    public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
        if (!doParticle(world))
            return;
        //todo params 9, 10
        FXWisp wisp = new FXWisp(world, x, y, z, size, r, g, b, true, true, maxAgeMul);
        wisp.setSpeed(motionx, motiony, motionz);
        Minecraft.getMinecraft().effectRenderer.addEffect(wisp);
    }

    private boolean doParticle(World world) {
        if (!world.isRemote)
            return false;

        /*if(!ConfigHandler.useVanillaParticleLimiter)
            return true;*/

        float chance = 1F;
        if (Minecraft.getMinecraft().gameSettings.particleSetting == 1)
            chance = 0.6F;
        else if (Minecraft.getMinecraft().gameSettings.particleSetting == 2)
            chance = 0.2F;

        return chance == 1F || Math.random() < chance;
    }

    @Override
    public void copyText(String s) {
        StringSelection stringSelection = new StringSelection(s);
        Clipboard board = Toolkit.getDefaultToolkit().getSystemClipboard();
        board.setContents(stringSelection, null);
    }


}
