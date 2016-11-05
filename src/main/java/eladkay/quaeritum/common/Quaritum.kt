package eladkay.quaeritum.common

import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.common.core.CommonProxy
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.registry.GameRegistry

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

    @Mod.EventHandler
    fun onRemapNeeded(remap: FMLMissingMappingsEvent) {
        println("It's remapping time! ${remap.all.size} mappings needed")
        remap.all.filter { it.resourceLocation.resourceDomain == "quaritum" }.
                forEach {
                    println("Fixing missing mapping quaritum:${it.resourceLocation.resourcePath} of type ${it.type} to ${LibMisc.MOD_ID}:${it.resourceLocation.resourcePath}")
                    if(it.type == GameRegistry.Type.BLOCK) it.remap(Block.getBlockFromName("${LibMisc.MOD_ID}:${it.resourceLocation.resourcePath}"))
                    else it.remap(Item.getByNameOrId("${LibMisc.MOD_ID}:${it.resourceLocation.resourcePath}"))

                }
    }

    companion object {

        @SidedProxy(clientSide = LibMisc.CLIENT_PROXY, serverSide = LibMisc.COMMON_PROXY)
        lateinit var proxy: CommonProxy

        @Mod.Instance(LibMisc.MOD_ID)
        lateinit var instance: Quaeritum
    }
}
