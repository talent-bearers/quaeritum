package eladkay.quaeritum.common.block.machine

import com.teamwizardry.librarianlib.common.base.block.BlockModContainer
import com.teamwizardry.librarianlib.common.base.block.TileMod
import com.teamwizardry.librarianlib.common.util.autoregister.TileRegister
import com.teamwizardry.librarianlib.common.util.saving.CapabilityProvide
import com.teamwizardry.librarianlib.common.util.saving.Save
import com.teamwizardry.librarianlib.common.util.saving.SaveMethodGetter
import com.teamwizardry.librarianlib.common.util.saving.SaveMethodSetter
import eladkay.quaeritum.api.machines.CentrifugeRecipes
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.items.ItemStackHandler
import net.minecraftforge.items.wrapper.RangedWrapper

/**
 * @author WireSegal
 * Created at 9:24 AM on 2/1/17.
 */
class BlockCentrifuge : BlockModContainer(LibNames.CENTRIFUGE, Material.CLOTH) {

    companion object {
        val FACING: PropertyDirection = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL)
    }

    override fun withRotation(state: IBlockState, rot: Rotation)
            = state.withProperty(FACING, rot.rotate(state.getValue(FACING)))
    override fun withMirror(state: IBlockState, mirrorIn: Mirror)
            = state.withRotation(mirrorIn.toRotation(state.getValue(FACING)))

    override fun getStateFromMeta(meta: Int)
            = defaultState.withProperty(FACING, EnumFacing.getHorizontal(meta))
    override fun getMetaFromState(state: IBlockState)
            = state.getValue(FACING).horizontalIndex

    override fun createTileEntity(world: World, state: IBlockState) = TileCentrifuge()

    @TileRegister("centrifuge")
    class TileCentrifuge : TileMod(), ITickable {

        @CapabilityProvide
        var handler = object : ItemStackHandler(3) {
            override fun onContentsChanged(slot: Int) {
                markDirty()
            }}
            @SaveMethodGetter("handler") get
            @SaveMethodSetter("handler") set(value) {
                handler.deserializeNBT(value.serializeNBT())
            }

        @CapabilityProvide(EnumFacing.UP)
        val inputs = RangedWrapper(handler, 0, 2)
        @CapabilityProvide(EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST)
        val output = RangedWrapper(handler, 2, 3)

        @Save var totalSteam = 0

        override fun update() {
            if (worldObj.isRemote) return

            val heated = worldObj.getBlockState(pos.down()).block == Blocks.FIRE
            val recipe = CentrifugeRecipes.getRecipe(inputs, heated)
            if (recipe != null) {
                totalSteam += 20 // temporary
                if (totalSteam >= recipe.steamRequired(inputs, heated)) {
                    if (output.insertItem(0, recipe.getOutput(inputs, heated), false) == null) {
                        recipe.consumeInputs(inputs, heated)
                        totalSteam = 0
                    }
                }
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, 0.0, 0.0, 0.0)
                markDirty()
            } else if (totalSteam != 0) {
                totalSteam = 0
                markDirty()
            }
        }
    }
}
