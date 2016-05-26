package eladkay.quaritum.common.core;

import net.minecraftforge.common.MinecraftForge;

public class Events {

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }


}
