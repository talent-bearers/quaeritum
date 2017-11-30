package eladkay.quaeritum.client.render

import com.teamwizardry.librarianlib.features.utilities.client.GlUtils
import eladkay.quaeritum.common.block.machine.BlockFluidHolder.TileFluidColumn
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.util.math.BlockPos
import org.lwjgl.opengl.GL11

/**
 * @author WireSegal
 * Created at 7:54 PM on 11/27/17.
 */
object RenderHolder : TileEntitySpecialRenderer<TileFluidColumn>() {

    override fun render(te: TileFluidColumn, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        val fluid = te.fluid.handler.fluid
        val down = te.world.getTileEntity(te.pos.down())
        if (down is TileFluidColumn && down.fluid.handler.fluid?.fluid == fluid?.fluid)
            return

        if (fluid != null) {

            var amount = fluid.amount

            var up: TileFluidColumn? = te
            val pos = BlockPos.MutableBlockPos(te.pos)
            while (up != null) {
                pos.setPos(pos.x, pos.y + 1, pos.z)
                up = te.world.getTileEntity(pos) as? TileFluidColumn
                if (up != null) {
                    if (up.fluid.handler.fluid?.fluid != fluid.fluid)
                        break
                    amount += up.fluid.handler.fluidAmount
                    if (up.fluid.handler.fluidAmount != up.fluid.handler.capacity)
                        break
                }
            }

            GlStateManager.pushMatrix()
            GlStateManager.translate(x + 0.15, y + 0.02, z + 0.15)
            GlStateManager.disableLighting()
            GlStateManager.enableBlend()
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            val x1 = 0.05
            val y1 = 0.00
            val z1 = 0.05
            val x2 = 0.65
            val y2 = (amount / te.fluid.handler.capacity.toDouble() + 0.15) * 0.7825 + ((amount - 1) / te.fluid.handler.capacity) * 0.2
            val z2 = 0.65
            GlUtils.useLightmap(te.world.getCombinedLight(te.pos, fluid.fluid.getLuminosity(fluid))) {
                ClientUtil.renderFluidCuboid(fluid.copy(), x1, y1, z1, x2, y2, z2)
            }
            GlStateManager.enableLighting()
            GlStateManager.disableBlend()
            GlStateManager.popMatrix()
        }
    }
}
