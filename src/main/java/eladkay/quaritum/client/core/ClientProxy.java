package eladkay.quaritum.client.core;

import eladkay.quaritum.client.render.RenderChaosborn;
import eladkay.quaritum.common.core.CommonProxy;
import eladkay.quaritum.common.entity.EntityChaosborn;
import eladkay.quaritum.common.lib.LibMisc;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void pre(FMLPreInitializationEvent e) {
        super.pre(e);
        ModelHandler.preInit(LibMisc.MOD_ID);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        ModelHandler.init();
        RenderingRegistry.registerEntityRenderingHandler(EntityChaosborn.class, new RenderChaosborn());
    }
}
