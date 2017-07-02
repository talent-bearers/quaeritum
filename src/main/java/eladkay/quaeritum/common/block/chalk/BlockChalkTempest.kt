package eladkay.quaeritum.common.block.chalk

import com.teamwizardry.librarianlib.features.base.block.BlockMod
import eladkay.quaeritum.common.block.ModBlocks
import eladkay.quaeritum.common.item.ModItems
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.*

class BlockChalkTempest : BlockMod(LibNames.CHALK_BLOCK_TEMPEST, Material.CIRCUITS) {
    private val WIRE_AABBS = arrayOf(AxisAlignedBB(0.1875, 0.0, 0.1875, 0.8125, 0.0625, 0.8125), AxisAlignedBB(0.1875, 0.0, 0.1875, 0.8125, 0.0625, 1.0), AxisAlignedBB(0.0, 0.0, 0.1875, 0.8125, 0.0625, 0.8125), AxisAlignedBB(0.0, 0.0, 0.1875, 0.8125, 0.0625, 1.0), AxisAlignedBB(0.1875, 0.0, 0.0, 0.8125, 0.0625, 0.8125), AxisAlignedBB(0.1875, 0.0, 0.0, 0.8125, 0.0625, 1.0), AxisAlignedBB(0.0, 0.0, 0.0, 0.8125, 0.0625, 0.8125), AxisAlignedBB(0.0, 0.0, 0.0, 0.8125, 0.0625, 1.0), AxisAlignedBB(0.1875, 0.0, 0.1875, 1.0, 0.0625, 0.8125), AxisAlignedBB(0.1875, 0.0, 0.1875, 1.0, 0.0625, 1.0), AxisAlignedBB(0.0, 0.0, 0.1875, 1.0, 0.0625, 0.8125), AxisAlignedBB(0.0, 0.0, 0.1875, 1.0, 0.0625, 1.0), AxisAlignedBB(0.1875, 0.0, 0.0, 1.0, 0.0625, 0.8125), AxisAlignedBB(0.1875, 0.0, 0.0, 1.0, 0.0625, 1.0), AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.0625, 0.8125), AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0))

    public override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, NORTH, EAST, SOUTH, WEST)
    }


    override fun getBoundingBox(state: IBlockState?, source: IBlockAccess?, pos: BlockPos?): AxisAlignedBB {
        return WIRE_AABBS[getAABBIndex(state!!.getActualState(source!!, pos!!))]
    }

    override fun getCollisionBoundingBox(blockState: IBlockState?, worldIn: IBlockAccess?, pos: BlockPos?): AxisAlignedBB? {
        return Block.NULL_AABB
    }

    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState {
        var state = state
        state = state.withProperty(WEST, this.getAttachPosition(worldIn, pos, EnumFacing.WEST))
        state = state.withProperty(EAST, this.getAttachPosition(worldIn, pos, EnumFacing.EAST))
        state = state.withProperty(NORTH, this.getAttachPosition(worldIn, pos, EnumFacing.NORTH))
        state = state.withProperty(SOUTH, this.getAttachPosition(worldIn, pos, EnumFacing.SOUTH))
        return state
    }

    private fun getAttachPosition(worldIn: IBlockAccess, pos: BlockPos, direction: EnumFacing): EnumAttachPosition {
        val blockpos = pos.offset(direction)
        val iblockstate = worldIn.getBlockState(pos.offset(direction))

        if (!canConnectTo(worldIn.getBlockState(pos), worldIn.getBlockState(blockpos)) && (iblockstate.isNormalCube || !canConnectupwardsTo(worldIn, pos, blockpos.down()))) {
            val iblockstate1 = worldIn.getBlockState(pos.up())

            if (!iblockstate1.isNormalCube) {
                val flag = worldIn.getBlockState(blockpos).isSideSolid(worldIn, blockpos, EnumFacing.UP) || worldIn.getBlockState(blockpos).block === Blocks.GLOWSTONE

                if (flag && canConnectupwardsTo(worldIn, pos, blockpos.up())) {
                    if (iblockstate.isBlockNormalCube) {
                        return EnumAttachPosition.up
                    }

                    return EnumAttachPosition.side
                }
            }

            return EnumAttachPosition.none
        } else {
            return EnumAttachPosition.side
        }
    }

    private fun canConnectupwardsTo(worldIn: IBlockAccess, pos: BlockPos, downPos: BlockPos): Boolean {
        return canConnectTo(worldIn.getBlockState(pos), worldIn.getBlockState(downPos))
    }

    private fun canConnectTo(selfState: IBlockState, blockState: IBlockState): Boolean {
        return blockState.block === this && selfState.block === this
    }

    override fun isOpaqueCube(state: IBlockState?): Boolean {
        return false
    }

    override fun isFullCube(state: IBlockState?): Boolean {
        return false
    }

    @SideOnly(Side.CLIENT)
    override fun getBlockLayer(): BlockRenderLayer {
        return BlockRenderLayer.CUTOUT
    }

    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean {
        return Blocks.REDSTONE_WIRE.canPlaceBlockAt(worldIn, pos)
    }

    override fun withRotation(state: IBlockState, rot: Rotation?): IBlockState {
        when (rot) {
            Rotation.CLOCKWISE_180 -> return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(EAST, state.getValue(WEST)).withProperty(SOUTH, state.getValue(NORTH)).withProperty(WEST, state.getValue(EAST))
            Rotation.COUNTERCLOCKWISE_90 -> return state.withProperty(NORTH, state.getValue(EAST)).withProperty(EAST, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(WEST)).withProperty(WEST, state.getValue(NORTH))
            Rotation.CLOCKWISE_90 -> return state.withProperty(NORTH, state.getValue(WEST)).withProperty(EAST, state.getValue(NORTH)).withProperty(SOUTH, state.getValue(EAST)).withProperty(WEST, state.getValue(SOUTH))
            else -> return state
        }
    }

    override fun withMirror(state: IBlockState, mirrorIn: Mirror?): IBlockState {
        when (mirrorIn) {
            Mirror.LEFT_RIGHT -> return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(NORTH))
            Mirror.FRONT_BACK -> return state.withProperty(EAST, state.getValue(WEST)).withProperty(WEST, state.getValue(EAST))
            else -> return state
        }
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos?, state: IBlockState?, playerIn: EntityPlayer, hand: EnumHand?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val stack = playerIn.getHeldItem(hand)
        if (stack.item === ModItems.chalk) {
            worldIn.setBlockState(pos!!, ModBlocks.chalk.getStateFromMeta(stack.itemDamage))
            return stack.itemDamage != getMetaFromState(state)
        }
        return false
    }

    override fun getMetaFromState(state: IBlockState?): Int {
        return 0
    }

    override fun neighborChanged(state: IBlockState?, worldIn: World?, pos: BlockPos?, blockIn: Block?, fromPos: BlockPos?) {
        if (!worldIn!!.isRemote) {
            if (!this.canPlaceBlockAt(worldIn, pos!!)) {
                worldIn.setBlockToAir(pos)
            }
        }
    }

    override fun getPickBlock(state: IBlockState, target: RayTraceResult?, world: World, pos: BlockPos, player: EntityPlayer?): ItemStack {
        return ItemStack(ModItems.tempest, 1, getMetaFromState(state))
    }

    override fun quantityDropped(random: Random?): Int {
        return 0
    }

    override fun createItemForm(): ItemBlock? {
        return null
    }

    enum class EnumAttachPosition(private val nm: String) : IStringSerializable {
        up("up"),
        side("side"),
        none("none");

        override fun toString(): String {
            return this.getName()
        }

        override fun getName(): String {
            return this.name
        }
    }

    companion object {
        val NORTH = PropertyEnum.create<EnumAttachPosition>("north", EnumAttachPosition::class.java)
        val EAST = PropertyEnum.create<EnumAttachPosition>("east", EnumAttachPosition::class.java)
        val SOUTH = PropertyEnum.create<EnumAttachPosition>("south", EnumAttachPosition::class.java)
        val WEST = PropertyEnum.create<EnumAttachPosition>("west", EnumAttachPosition::class.java)

        private fun getAABBIndex(state: IBlockState): Int {
            var index = 0
            val north = state.getValue(NORTH) != EnumAttachPosition.none
            val east = state.getValue(EAST) != EnumAttachPosition.none
            val south = state.getValue(SOUTH) != EnumAttachPosition.none
            val west = state.getValue(WEST) != EnumAttachPosition.none

            if (north || south && !east && !west) index = index or (1 shl EnumFacing.NORTH.horizontalIndex)
            if (east || west && !north && !south) index = index or (1 shl EnumFacing.EAST.horizontalIndex)
            if (south || north && !east && !west) index = index or (1 shl EnumFacing.SOUTH.horizontalIndex)
            if (west || east && !north && !south) index = index or (1 shl EnumFacing.WEST.horizontalIndex)

            return index
        }
    }
}
