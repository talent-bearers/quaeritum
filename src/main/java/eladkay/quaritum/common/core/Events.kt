package eladkay.quaritum.common.core

import net.minecraftforge.common.MinecraftForge

class Events {

    fun init() {
        MinecraftForge.EVENT_BUS.register(this)
    }


}
