package eladkay.quaritum.common.core;

import eladkay.quaritum.common.block.ModBlocks;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author WireSegal
 *         Created at 4:29 PM on 4/16/16.
 */
public class CommonProxy {
    public void pre(FMLPreInitializationEvent e) {
        ModBlocks.init();
    }

    public void init(FMLInitializationEvent e) {

    }

    public void post(FMLPostInitializationEvent e) {

    }
}
