package eladkay.quaeritum.common.core

import eladkay.quaeritum.api.lib.LibMisc
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.GameRegistry

class Events {

    fun init() {
        MinecraftForge.EVENT_BUS.register(this)
    }




}
