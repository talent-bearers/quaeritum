package eladkay.quaeritum.common

import eladkay.quaeritum.api.internal.InternalHandler
import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.common.core.CommonProxy
import eladkay.quaeritum.common.core.QuaeritumInternalHandler
import eladkay.quaeritum.common.core.command.CommandAddElement
import eladkay.quaeritum.common.core.command.CommandCastSpell
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent

@Mod(modid = LibMisc.MOD_ID, name = LibMisc.NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES)
class Quaeritum {

    init {
        FluidRegistry.enableUniversalBucket()
    }

    @Mod.EventHandler
    fun pre(e: FMLPreInitializationEvent) {
        InternalHandler.setInternalHandler(QuaeritumInternalHandler)

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

    @Mod.EventHandler
    fun serverStart(e: FMLServerStartingEvent) {
        e.registerServerCommand(CommandCastSpell)
        e.registerServerCommand(CommandAddElement)
    }

    companion object {

        @SidedProxy(clientSide = LibMisc.CLIENT_PROXY, serverSide = LibMisc.COMMON_PROXY)
        lateinit var proxy: CommonProxy

        @Mod.Instance(LibMisc.MOD_ID)
        lateinit var instance: Quaeritum
    }
}
