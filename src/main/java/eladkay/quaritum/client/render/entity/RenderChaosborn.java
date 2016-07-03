package eladkay.quaritum.client.render.entity;

import eladkay.quaritum.common.lib.LibLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderChaosborn extends RenderLiving implements IRenderFactory {
    public RenderChaosborn(RenderManager manager) {
        super(manager, new ModelVortex(), 0.5f);
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
