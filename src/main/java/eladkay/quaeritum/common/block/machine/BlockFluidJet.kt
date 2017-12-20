package eladkay.quaeritum.common.block.machine

import com.teamwizardry.librarianlib.features.autoregister.TileRegister
import com.teamwizardry.librarianlib.features.base.block.tile.BlockModContainer
import com.teamwizardry.librarianlib.features.base.block.tile.TileModTickable
import com.teamwizardry.librarianlib.features.saving.Save
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumFacing.*
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.CapabilityFluidHandler

class BlockFluidJet : BlockModContainer("fluid_jet", Material.IRON) {
    companion object {
        val UP_AABB = AxisAlignedBB(6 / 16.0, 9 / 16.0, 6 / 16.0, 10 / 16.0, 1.0, 10 / 16.0)
        val DOWN_AABB = AxisAlignedBB(6 / 16.0, 0.0, 6 / 16.0, 10 / 16.0, 7 / 16.0, 10 / 16.0)

        val SOUTH_AABB = AxisAlignedBB(6 / 16.0, 6 / 16.0, 9 / 16.0, 10 / 16.0, 10 / 16.0, 1.0)
        val NORTH_AABB = AxisAlignedBB(6 / 16.0, 6 / 16.0, 0.0, 10 / 16.0, 10 / 16.0, 7 / 16.0)

        val EAST_AABB = AxisAlignedBB(9 / 16.0, 6 / 16.0, 6 / 16.0, 1.0, 10 / 16.0, 10 / 16.0)
        val WEST_AABB = AxisAlignedBB(0.0, 6 / 16.0, 6 / 16.0, 7 / 16.0, 10 / 16.0, 10 / 16.0)

        val FACING: PropertyDirection = PropertyDirection.create("attachment")

        val AABBS = mapOf(
                UP to UP_AABB,
                DOWN to DOWN_AABB,
                WEST to WEST_AABB,
                EAST to EAST_AABB,
                NORTH to NORTH_AABB,
                SOUTH to SOUTH_AABB
        )


        fun getTarget(pos: BlockPos, worldIn: IBlockAccess, predicate: (IBlockState, TileEntity?, EnumFacing) -> Boolean): Pair<TileEntity, Int>? {
            val thisState = worldIn.getBlockState(pos)
            val thisFacing = thisState.getValue(FACING).opposite
            var shift = pos.offset(thisFacing)
            var state = worldIn.getBlockState(shift)
            var te = worldIn.getTileEntity(shift)
            var dist = 1
            while (!predicate(state, te, thisFacing) && dist++ < 7) {
                shift = shift.offset(thisFacing)
                state = worldIn.getBlockState(shift)
                te = worldIn.getTileEntity(shift)
            }
            if (dist == 8 || te == null) return null
            return te to dist
        }
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess?, pos: BlockPos?) = AABBS[state.getValue(FACING)]

    override fun createBlockState() = BlockStateContainer(this, FACING)

    override fun getMetaFromState(state: IBlockState) = state.getValue(FACING).index
    override fun getStateFromMeta(meta: Int) = defaultState.withProperty(FACING, EnumFacing.getFront(meta))

    override fun isFullCube(state: IBlockState) = false
    override fun isOpaqueCube(blockState: IBlockState) = false

    override fun getStateForPlacement(worldIn: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase): IBlockState
            = defaultState.withProperty(FACING, facing.opposite)

    override fun getBlockFaceShape(world: IBlockAccess, state: IBlockState, pos: BlockPos, facing: EnumFacing): BlockFaceShape {
        if (facing == state.getValue(FACING).opposite) return BlockFaceShape.MIDDLE_POLE_THIN
        return BlockFaceShape.UNDEFINED
    }

    override fun createTileEntity(world: World, state: IBlockState): TileEntity? {
        return TileJet()
    }

    @TileRegister("water_jet")
    class TileJet : TileModTickable() {
        @Save
        var lastStack: FluidStack? = null
        @Save
        var distance: Double = 0.0

        override fun tick() {
            if (world.isRemote) return
            val lastPrev = lastStack
            val lastDist = distance
            lastStack = null
            distance = 0.0
            val facing = world.getBlockState(pos).getValue(FACING)
            val tile = world.getTileEntity(pos.offset(facing))
            if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.opposite)) {
                val cap = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.opposite)
                if (cap != null) {
                    val target = getTarget(pos, world) { _, te, facing ->
                        te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.opposite)
                    }
                    if (target != null) {
                        val (te, dist) = target
                        val targetCap = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing)
                        if (targetCap != null) {
                            val tentativeStack = cap.drain(100, false)
                            if (tentativeStack != null) {
                                val succeeded = targetCap.fill(tentativeStack, false)
                                if (succeeded > 0) {
                                    lastStack = tentativeStack
                                    val offset = pos.offset(facing.opposite, dist)
                                    val trace = world.getBlockState(offset)
                                            .collisionRayTrace(world, pos, Vec3d(pos).addVector(0.5, 0.5, 0.5), Vec3d(offset).addVector(0.5, 0.5, 0.5))
                                    distance = dist - trace.hitVec.subtract(Vec3d(pos).addVector(0.5, 0.5, 0.5)).lengthVector()
                                    // todo hurt people who are in the way
                                    targetCap.fill(cap.drain(succeeded, true), true)
                                    te.markDirty()
                                    tile.markDirty()
                                }
                            }
                        }
                    }
                }
            }

            if (lastPrev != lastStack || lastDist != distance)
                markDirty()
        }

        override fun getRenderBoundingBox(): AxisAlignedBB {
            return INFINITE_EXTENT_AABB
        }
    }
}
