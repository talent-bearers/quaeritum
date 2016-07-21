package eladkay.quaritum.client.core;

import eladkay.quaritum.api.animus.AnimusHelper;
import eladkay.quaritum.common.lib.LibLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientEventHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void textureStitchEvent(TextureStitchEvent e) {
        e.getMap().registerSprite(LibLocations.MAGICLINEFX);
    }

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    //copy pasta from my modularguilib
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {

        if (Minecraft.getMinecraft().thePlayer == null || !AnimusHelper.Network.getInfused(Minecraft.getMinecraft().thePlayer.getUniqueID()))
            return;
        int w = 5; //width, change this if needed
        FontRenderer fontRendererObj = FMLClientHandler.instance().getClient().fontRendererObj; //get the font renderer
        String aDisplay = TextFormatting.RED + "Animus: " + AnimusHelper.Network.getAnimus(Minecraft.getMinecraft().thePlayer.getUniqueID());
        fontRendererObj.drawString(aDisplay, w, 15, 0xffffff); //draw it

    }

}
