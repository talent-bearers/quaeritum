package eladkay.quaeritum.client.render

import com.teamwizardry.librarianlib.features.utilities.client.GlUtils
import eladkay.quaeritum.common.block.machine.BlockFluidJet
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import org.lwjgl.opengl.GL11

/**
 * @author WireSegal
 * Created at 7:54 PM on 11/27/17.
 */
object RenderJet : TileEntitySpecialRenderer<BlockFluidJet.TileJet>() {

    override fun render(te: BlockFluidJet.TileJet, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        val fluid = te.lastStack
        if (fluid != null) {
            val facing = te.world.getBlockState(te.pos).getValue(BlockFluidJet.FACING)
            GlStateManager.pushMatrix()
            GlStateManager.translate(
                    x + 0.5 - (0.5 / 16.0) + 0.5 * facing.frontOffsetX / 16.0,
                    y + 0.5 - (0.5 / 16.0) + 0.5 * facing.frontOffsetY / 16.0,
                    z + 0.5 - (0.5 / 16.0) + 0.5 * facing.frontOffsetZ / 16.0)
            GlStateManager.scale(
                    1 / 16.0 - (te.distance + 0.75 / 16.0) * facing.frontOffsetX,
                    1 / 16.0 - (te.distance + 0.75 / 16.0) * facing.frontOffsetY,
                    1 / 16.0 - (te.distance + 0.75 / 16.0) * facing.frontOffsetZ)
            GlStateManager.disableLighting()
            GlStateManager.enableBlend()
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GlUtils.useLightmap(te.world.getCombinedLight(te.pos, fluid.fluid.getLuminosity(fluid))) {
                ClientUtil.renderFluidCuboid(fluid.copy(), 0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
            }
            GlStateManager.enableLighting()
            GlStateManager.disableBlend()
            GlStateManager.popMatrix()
        }
    }
}
