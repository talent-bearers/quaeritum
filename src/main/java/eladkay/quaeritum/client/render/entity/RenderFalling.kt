package eladkay.quaeritum.client.render.entity

import eladkay.quaeritum.common.entity.EntityDroppingBlock
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * @author WireSegal
 * Created at 9:59 PM on 8/13/17.
 */
@SideOnly(Side.CLIENT)
class RenderFalling(renderManagerIn: RenderManager) : Render<EntityDroppingBlock>(renderManagerIn) {
    init {
        this.shadowSize = 0.5f
    }

    /**
     * Renders the desired `T` type Entity.
     */
    override fun doRender(entity: EntityDroppingBlock, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        val iblockstate = entity.block

        if (iblockstate.renderType == EnumBlockRenderType.MODEL) {
            val world = entity.world

            if (iblockstate !== world.getBlockState(BlockPos(entity)) && iblockstate.renderType != EnumBlockRenderType.INVISIBLE) {
                this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
                GlStateManager.pushMatrix()
                GlStateManager.disableLighting()
                val tessellator = Tessellator.getInstance()
                val vertexbuffer = tessellator.buffer

                if (this.renderOutlines) {
                    GlStateManager.enableColorMaterial()
                    GlStateManager.enableOutlineMode(this.getTeamColor(entity))
                }

                vertexbuffer.begin(7, DefaultVertexFormats.BLOCK)
                val blockpos = BlockPos(entity.posX, entity.entityBoundingBox.maxY, entity.posZ)
                GlStateManager.translate((x - blockpos.x.toDouble() - 0.5).toFloat(), (y - blockpos.y.toDouble()).toFloat(), (z - blockpos.z.toDouble() - 0.5).toFloat())
                val blockrendererdispatcher = Minecraft.getMinecraft().blockRendererDispatcher
                blockrendererdispatcher.blockModelRenderer.renderModel(world, blockrendererdispatcher.getModelForState(iblockstate), iblockstate, blockpos, vertexbuffer, false, MathHelper.getPositionRandom(entity.origin))
                tessellator.draw()

                if (this.renderOutlines) {
                    GlStateManager.disableOutlineMode()
                    GlStateManager.disableColorMaterial()
                }

                GlStateManager.enableLighting()
                GlStateManager.popMatrix()
                super.doRender(entity, x, y, z, entityYaw, partialTicks)
            }
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    override fun getEntityTexture(entity: EntityDroppingBlock): ResourceLocation {
        return TextureMap.LOCATION_BLOCKS_TEXTURE
    }
}
