package eladkay.quaeritum.client.core

import eladkay.quaeritum.common.lib.LibLocations
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class ClientEventHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    fun textureStitchEvent(e: TextureStitchEvent) {
        e.map.registerSprite(LibLocations.MAGICLINEFX)
        e.map.registerSprite(LibLocations.PARTICLE_SPARKLE)
        e.map.registerSprite(LibLocations.PARTICLE_SMOKE)
    }

    fun init() {
        MinecraftForge.EVENT_BUS.register(this)
    }

}
