package eladkay.quaritum.client.render;

import eladkay.quaritum.common.lib.LibLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderChaosborn extends RenderLiving {
    public RenderChaosborn() {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelVortex(), 0.5f);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return LibLocations.CHAOSBORN;
    }
}
