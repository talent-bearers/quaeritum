package eladkay.quaeritum.common.block

import com.teamwizardry.librarianlib.features.base.block.ItemModBlock
import com.teamwizardry.librarianlib.features.base.block.tile.BlockModContainer
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import eladkay.quaeritum.api.lib.LibNBT
import eladkay.quaeritum.common.block.tile.TileEntityBlueprint
import eladkay.quaeritum.common.lib.LibLocations
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

class BlockBlueprint(name: String) : BlockModContainer(name, Material.PISTON) {

    init {
        setHardness(1.2f)
    }

    override fun createItemForm(): ItemBlock? {
        val item = ItemModBlock(this)
        item.addPropertyOverride(LibLocations.FLAT_CHALK) { stack, _, _ -> if (ItemNBTHelper.getBoolean(stack, LibNBT.FLAT, false)) 1.0f else 0.0f }
        return item
    }

    override fun getBoundingBox(state: IBlockState?, source: IBlockAccess?, pos: BlockPos?): AxisAlignedBB {
        return BOUNDS
    }

    override fun isOpaqueCube(state: IBlockState?): Boolean {
        return false
    }

    override fun isFullBlock(state: IBlockState?): Boolean {
        return false
    }

    override fun isFullCube(state: IBlockState?): Boolean {
        return false
    }

    override fun onBlockAdded(worldIn: World, pos: BlockPos, state: IBlockState) {
        if (!worldIn.isRemote) {
            val power = worldIn.isBlockPowered(pos)
            if (power != state.getValue(POWERED))
                worldIn.setBlockState(pos, state.withProperty(POWERED, power))
        }
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val tile = worldIn.getTileEntity(pos)
        return tile is TileEntityBlueprint && tile.onBlockActivated()
    }

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) {
        if (!worldIn.isRemote) {
            val power = worldIn.isBlockPowered(pos)
            if (power != state.getValue(POWERED)) {
                worldIn.setBlockState(pos, state.withProperty(POWERED, power))

                val tile = worldIn.getTileEntity(pos)
                if (power && tile is TileEntityBlueprint)
                    tile.onBlockActivated()
            }
        }

    }

    override fun createTileEntity(world: World, state: IBlockState): TileEntity? {
        return TileEntityBlueprint()
    }

    override fun createBlockState() = BlockStateContainer(this, POWERED)
    override fun getStateFromMeta(meta: Int): IBlockState = defaultState.withProperty(POWERED, meta != 0)
    override fun getMetaFromState(state: IBlockState) = if (state.getValue(POWERED)) 1 else 0

    companion object {

        val POWERED: PropertyBool = PropertyBool.create("powered")

        val BOUNDS = AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1 / 16.0, 1.0)
    }
}
