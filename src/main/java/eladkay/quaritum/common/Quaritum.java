package eladkay.quaritum.common;

import eladkay.quaritum.common.core.CommonProxy;
import eladkay.quaritum.api.lib.LibMisc;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

//@Mod(modid = LibMisc.MOD_ID, name = LibMisc.NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES, acceptedMinecraftVersions = LibMisc.ACCEPTED_VERSION, guiFactory = LibMisc.GUI_FACTORY)
@Mod(modid = LibMisc.MOD_ID, name = LibMisc.NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES)
public class Quaritum {

    @SidedProxy(clientSide = LibMisc.CLIENT_PROXY, serverSide = LibMisc.COMMON_PROXY)
    public static CommonProxy proxy;
    @Mod.Instance(LibMisc.MOD_ID)
    public static Quaritum instance;
    private static boolean devEnvOverride = false; //if this is true, the environment will launch as normal, even in a
                                                   //dev environment
    public static boolean isDevEnv = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment") && !devEnvOverride;

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
