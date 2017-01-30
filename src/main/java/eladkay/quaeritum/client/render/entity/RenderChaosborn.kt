package eladkay.quaeritum.client.render.entity

import eladkay.quaeritum.common.lib.LibLocations
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.entity.EntityLiving
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.client.registry.IRenderFactory

class RenderChaosborn(manager: RenderManager) : RenderLiving<EntityLiving>(manager, ModelVortex(), 0.5f), IRenderFactory<EntityLiving> {
    override fun getEntityTexture(entity: EntityLiving?): ResourceLocation? {
        return LibLocations.CHAOSBORN
    }


    override fun createRenderFor(manager: RenderManager) = this
}
