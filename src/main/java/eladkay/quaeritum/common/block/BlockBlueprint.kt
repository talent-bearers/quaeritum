package eladkay.quaeritum.common.block

import com.teamwizardry.librarianlib.features.base.block.BlockMod
import com.teamwizardry.librarianlib.features.base.block.ItemModBlock
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import eladkay.quaeritum.api.lib.LibNBT
import eladkay.quaeritum.common.block.tile.TileEntityBlueprint
import eladkay.quaeritum.common.lib.LibLocations
import net.minecraft.block.Block
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
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

class BlockBlueprint(name: String) : BlockMod(name, Material.PISTON), /*IGuideLinked, */ITileEntityProvider {

    init {
        setHardness(1.2f)
       // constructBook()
    }

    override fun createItemForm(): ItemBlock? {
        val item = ItemModBlock(this)
        item.addPropertyOverride(LibLocations.FLAT_CHALK) { stack, world, entityLivingBase -> if (ItemNBTHelper.getBoolean(stack.copy(), LibNBT.FLAT, false)) 1.0f else 0.0f }
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

    override fun isSideSolid(base_state: IBlockState?, world: IBlockAccess?, pos: BlockPos?, side: EnumFacing?): Boolean {
        return super.isSideSolid(base_state, world, pos, side)
    }

    override fun onBlockActivated(worldIn: World?, pos: BlockPos?, state: IBlockState?, playerIn: EntityPlayer?, hand: EnumHand?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        //Quaeritum.proxy.spawnStafflikeParticles(worldIn, pos.getX(), pos.getY() + 1, pos.getZ());
        val tile = worldIn!!.getTileEntity(pos!!)
        return tile is TileEntityBlueprint && tile.onBlockActivated()
    }

    override fun onNeighborChange(world: IBlockAccess?, pos: BlockPos?, neighbor: BlockPos?) {
        if ((world as World).isBlockPowered(pos!!)) {
            val tile = world.getTileEntity(pos)
            if (tile is TileEntityBlueprint) tile.onBlockActivated()
        }
    }

    override fun neighborChanged(state: IBlockState?, worldIn: World?, pos: BlockPos?, blockIn: Block?, fromPos: BlockPos?) {
        if (worldIn!!.isBlockPowered(pos!!)) {
            val tile = worldIn.getTileEntity(pos)
            if (tile is TileEntityBlueprint) tile.onBlockActivated()
        }
    }

   /* override fun getLinkedEntry(world: World, pos: BlockPos, player: EntityPlayer, stack: ItemStack): ResourceLocation {
        return ResourceLocation(LibMisc.MOD_ID, LibBook.ENTRY_BLUEPRINT_NAME)
    }

    fun constructBook() {
        pages.add(PageText(TooltipHelper.local(LibBook.ENTRY_BLUEPRINT_PAGE1)))
        ModBook.register(ModBook.pagesAnimus, LibBook.ENTRY_BLUEPRINT_NAME, pages, ItemStack(ModBlocks.blueprint))
    }*/

    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity {
        return TileEntityBlueprint()
    }

    companion object {

        val BOUNDS = AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0)
      //  var pages: MutableList<IPage> = ArrayList()
    }
}
