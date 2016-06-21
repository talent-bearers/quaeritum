package eladkay.quaritum.client.render;

import eladkay.quaritum.common.lib.LibLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderChaosborn extends RenderLiving implements IRenderFactory {
    public RenderChaosborn() {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelVortex(), 0.5f);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return LibLocations.CHAOSBORN;
    }

    @Override
    public Render createRenderFor(RenderManager manager) {
        return this;
    }
}
