package eladkay.quaritum.client.core;

import eladkay.quaritum.common.lib.LibLocations;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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

}
