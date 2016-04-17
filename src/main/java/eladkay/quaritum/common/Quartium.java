package eladkay.quaritum.common;

import eladkay.quaritum.common.core.CommonProxy;
import eladkay.quaritum.common.lib.LibMisc;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

//@Mod(modid = LibMisc.MOD_ID, name = LibMisc.NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES, acceptedMinecraftVersions = LibMisc.ACCEPTED_VERSION, guiFactory = LibMisc.GUI_FACTORY)
@Mod(modid = LibMisc.MOD_ID, name = LibMisc.NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES)
public class Quartium {

    @SidedProxy(clientSide = LibMisc.CLIENT_PROXY, serverSide = LibMisc.COMMON_PROXY)
    private static CommonProxy proxy;

    @Mod.EventHandler
    public void pre(FMLPreInitializationEvent e) {
        proxy.pre(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void post(FMLPostInitializationEvent e) {
        proxy.post(e);
    }
}
