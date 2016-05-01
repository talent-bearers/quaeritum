package eladkay.quaritum.common.entity.render;

import eladkay.quaritum.common.entity.EntityChaosborn;
import eladkay.quaritum.common.lib.LibLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;

public class RenderChaosborn extends Render<EntityChaosborn> {
    protected RenderChaosborn() {
        super(Minecraft.getMinecraft().getRenderManager());
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityChaosborn entityChaosborn) {
        return LibLocations.CHAOSBORN;
    }
}
