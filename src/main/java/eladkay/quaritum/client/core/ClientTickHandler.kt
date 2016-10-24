/**
 * This class was created by . It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 *
 * File Created @ [Feb 3, 2014, 9:59:17 PM (GMT)]
 */
package eladkay.quaritum.client.core

import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.*

class ClientTickHandler {

    private fun calcDelta() {
        val oldTotal = total
        total = ticksInGame + partialTicks
        delta = total - oldTotal
    }

    @SubscribeEvent
    fun renderTick(event: RenderTickEvent) {
        if (event.phase == Phase.START)
            partialTicks = event.renderTickTime
        else {
            calcDelta()
        }
    }

    @SubscribeEvent
    fun clientTickEnd(event: ClientTickEvent) {
        if (event.phase == Phase.END) {

            val gui = Minecraft.getMinecraft().currentScreen
            if (gui == null || !gui.doesGuiPauseGame()) {
                ticksInGame++
                partialTicks = 0f
            }

            calcDelta()
        }
    }

    companion object {

        var ticksInGame = 0
        var partialTicks = 0f
        var delta = 0f
        var total = 0f
    }

}
