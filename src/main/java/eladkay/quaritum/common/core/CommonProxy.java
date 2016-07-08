package eladkay.quaritum.common.core;

import eladkay.quaritum.api.animus.AnimusHelper;
import eladkay.quaritum.client.core.ClientEventHandler;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.book.ModBook;
import eladkay.quaritum.common.crafting.ModRecipes;
import eladkay.quaritum.common.entity.ModEntities;
import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.networking.NetworkHelper;
import eladkay.quaritum.common.rituals.ModDiagrams;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author WireSegal
 *         Created at 4:29 PM on 4/16/16.
 */
public class CommonProxy {
    public void pre(FMLPreInitializationEvent e) {
        ModBlocks.init();
        ModItems.init();
        ModEntities.init();
        ModRecipes.init();
        new Events().init();
        new ClientEventHandler().init();
        new AnimusHelper.Network.EventHandler();
        NetworkHelper.init();
        ModDiagrams.init();
        ChatHelper.PacketHandler.init();
        FMLInterModComms.sendMessage("Waila", "register", "eladkay.quaritum.common.compat.waila.Waila.onWailaCall");
    }

    public void init(FMLInitializationEvent e) {
        ModBook.init();
    }

    public void post(FMLPostInitializationEvent e) {

    }

    public void spawnParticleMagixFX(World world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b) {
        //NO-OP
    }
    @Deprecated
    public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
        //NO-OP
    }
    @Deprecated
    public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size) {
        wispFX(world, x, y, z, r, g, b, size, 0F);
    }

    @Deprecated
    public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float gravity) {
        wispFX(world, x, y, z, r, g, b, size, gravity, 1F);
    }

    @Deprecated
    public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float gravity, float maxAgeMul) {
        wispFX(world, x, y, z, r, g, b, size, 0, -gravity, 0, maxAgeMul);
    }

    @Deprecated
    public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz) {
        wispFX(world, x, y, z, r, g, b, size, motionx, motiony, motionz, 1F);
    }
    public void spawnStafflikeParticles(World world, double x, double y, double z) {
        //noop
    }
    public void spawnParticle(Particle particle, World world) {}
}
