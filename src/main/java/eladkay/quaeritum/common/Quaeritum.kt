package eladkay.quaeritum.common

import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.common.core.CommonProxy
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

@Mod(modid = LibMisc.MOD_ID, name = LibMisc.NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES)
class Quaeritum {

    @Mod.EventHandler
    fun pre(e: FMLPreInitializationEvent) {
        proxy.pre(e)
    }

    @Mod.EventHandler
    fun init(e: FMLInitializationEvent) {
        proxy.init(e)
    }

    @Mod.EventHandler
    fun post(e: FMLPostInitializationEvent) {
        proxy.post(e)
    }

    companion object {

        @SidedProxy(clientSide = LibMisc.CLIENT_PROXY, serverSide = LibMisc.COMMON_PROXY)
        lateinit var proxy: CommonProxy

        @Mod.Instance(LibMisc.MOD_ID)
        lateinit var instance: Quaeritum
    }
}
