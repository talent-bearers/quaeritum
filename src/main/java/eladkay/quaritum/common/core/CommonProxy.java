package eladkay.quaritum.common.core;

import eladkay.quaritum.api.animus.AnimusHelper;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.crafting.ModRecipes;
import eladkay.quaritum.common.entity.ModEntities;
import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.rituals.ModDiagrams;
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
        new AnimusHelper.Network.EventHandler();
        ModDiagrams.init();
        ChatHelper.PacketHandler.init();
        FMLInterModComms.sendMessage("Waila", "register", "eladkay.quaritum.common.compat.waila.Waila.onWailaCall");
    }

    public void init(FMLInitializationEvent e) {

    }

    public void post(FMLPostInitializationEvent e) {

    }

    public void spawnParticleMagixFX(World world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b) {
        //NO-OP
    }
}
