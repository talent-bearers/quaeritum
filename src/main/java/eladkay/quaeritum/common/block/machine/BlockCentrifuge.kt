package eladkay.quaeritum.common.block.machine

import com.teamwizardry.librarianlib.features.autoregister.TileRegister
import com.teamwizardry.librarianlib.features.base.block.tile.BlockModContainer
import com.teamwizardry.librarianlib.features.base.block.tile.TileMod
import com.teamwizardry.librarianlib.features.base.block.tile.module.ModuleInventory
import com.teamwizardry.librarianlib.features.saving.Module
import com.teamwizardry.librarianlib.features.saving.Save
import eladkay.quaeritum.api.machines.CentrifugeRecipes
import eladkay.quaeritum.common.core.QuaeritumSoundEvents
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.BlockHorizontal
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.Blocks
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * @author WireSegal
 * Created at 9:24 AM on 2/1/17.
 */
class BlockCentrifuge : BlockModContainer(LibNames.CENTRIFUGE, Material.CLOTH) {

    companion object {
        val FACING: PropertyDirection = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL)
    }

    override fun createBlockState() = BlockStateContainer(this, FACING)

    override fun withRotation(state: IBlockState, rot: Rotation): IBlockState = state.withProperty(FACING, rot.rotate(state.getValue(FACING)))

    override fun withMirror(state: IBlockState, mirrorIn: Mirror): IBlockState = state.withRotation(mirrorIn.toRotation(state.getValue(FACING)))

    override fun getStateFromMeta(meta: Int): IBlockState = defaultState.withProperty(FACING, EnumFacing.getHorizontal(meta))

    override fun getMetaFromState(state: IBlockState) = state.getValue(FACING).horizontalIndex

    override fun getStateForPlacement(world: World?, pos: BlockPos?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase, hand: EnumHand?): IBlockState = defaultState.withProperty(BlockHorizontal.FACING, placer.horizontalFacing.opposite)

    override fun createTileEntity(world: World, state: IBlockState) = TileCentrifuge()

    @TileRegister("centrifuge")
    class TileCentrifuge : TileMod(), ITickable {

        @Module
        val inputs = ModuleInventory(2).disallowSides(EnumFacing.DOWN)

        @Module
        val output =  ModuleInventory(1).setSides(EnumFacing.DOWN)

        @Save
        var totalSteam = 0

        override fun update() {
            if (world.isRemote) return

            val heated = world.getBlockState(pos.down()).block == Blocks.FIRE
            val recipe = CentrifugeRecipes.getRecipe(inputs.handler, heated)
            if (recipe != null) {
                if (totalSteam >= recipe.steamRequired(inputs.handler, heated)) {
                    if (output.handler.insertItem(0, recipe.getOutput(inputs.handler, heated), false).isEmpty) {
                        recipe.consumeInputs(inputs.handler, heated)
                        totalSteam = 0
                    }
                } else {
                    totalSteam += 20 // temporary
                    if (totalSteam % 50 == 0)
                        world.playSound(null, pos, QuaeritumSoundEvents.centrifuge, SoundCategory.BLOCKS, 1f, 5f)
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
