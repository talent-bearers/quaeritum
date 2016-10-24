package eladkay.quaritum.common

import eladkay.quaritum.api.lib.LibMisc
import eladkay.quaritum.common.core.CommonProxy
import net.minecraft.launchwrapper.Launch
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

//@Mod(modid = LibMisc.MOD_ID, name = LibMisc.NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES, acceptedMinecraftVersions = LibMisc.ACCEPTED_VERSION, guiFactory = LibMisc.GUI_FACTORY)
@Mod(modid = LibMisc.MOD_ID, name = LibMisc.NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES)
class Quaritum {

    @Mod.EventHandler
    fun pre(e: FMLPreInitializationEvent) {
        proxy!!.pre(e)
    }

    @Mod.EventHandler
    fun init(e: FMLInitializationEvent) {
        proxy!!.init(e)
    }

    @Mod.EventHandler
    fun post(e: FMLPostInitializationEvent) {
        proxy!!.post(e)
    }

    companion object {

        @SidedProxy(clientSide = LibMisc.CLIENT_PROXY, serverSide = LibMisc.COMMON_PROXY)
        var proxy: CommonProxy? = null
        @Mod.Instance(LibMisc.MOD_ID)
        var instance: Quaritum? = null
        private val devEnvOverride = false //if this is true, the environment will launch as normal, even in a
        //dev environment
        var isDevEnv = Launch.blackboard["fml.deobfuscatedEnvironment"] as Boolean && !devEnvOverride
    }
}
