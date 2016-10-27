package eladkay.quaeritum.client.core

import com.teamwizardry.librarianlib.common.util.plus
import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.common.lib.LibLocations
import net.minecraft.client.Minecraft
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class ClientEventHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    fun textureStitchEvent(e: TextureStitchEvent) {
        e.map.registerSprite(LibLocations.MAGICLINEFX)
    }

    fun init() {
        MinecraftForge.EVENT_BUS.register(this)
    }

    //copy pasta from my modularguilib
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    fun onRenderTick(event: TickEvent.RenderTickEvent) {

        if (Minecraft.getMinecraft().thePlayer == null || !AnimusHelper.Network.getInfused(Minecraft.getMinecraft().thePlayer.uniqueID))
            return
        val w = 5 //width, change this if needed
        val fontRendererObj = FMLClientHandler.instance().client.fontRendererObj //get the font renderer
        val aDisplay = TextFormatting.RED + "Animus: " + AnimusHelper.Network.getAnimus(Minecraft.getMinecraft().thePlayer.uniqueID)
        fontRendererObj.drawString(aDisplay, w, 15, 0xffffff) //draw it

    }

}
