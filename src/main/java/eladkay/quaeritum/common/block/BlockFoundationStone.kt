package eladkay.quaeritum.common.block




import com.teamwizardry.librarianlib.common.base.block.ItemModBlock
import com.teamwizardry.librarianlib.common.util.ItemNBTHelper
import eladkay.quaeritum.api.lib.LibNBT
import eladkay.quaeritum.common.block.base.BlockQuaeritum
import eladkay.quaeritum.common.block.tile.TileEntityFoundationStone
import eladkay.quaeritum.common.lib.LibLocations
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class BlockFoundationStone : BlockQuaeritum(LibNames.FOUNDATION, Material.ROCK), /*IGuideLinked, */ITileEntityProvider {

    init {
        //constructBook()
    }

   /*fun constructBook() {
        pages.add(PageText(TooltipHelper.local(LibBook.ENTRY_BLUEPRINT_PAGE1)))
        ModBook.register(ModBook.pagesAnimus, LibBook.ENTRY_BLUEPRINT_NAME, pages, ItemStack(ModBlocks.blueprint))
    }

    override fun getLinkedEntry(world: World, pos: BlockPos, player: EntityPlayer, stack: ItemStack): ResourceLocation {
        return ResourceLocation(LibMisc.MOD_ID, LibBook.ENTRY_BLUEPRINT_NAME)
    }*/

    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity {
        return TileEntityFoundationStone()
    }

    override fun onBlockActivated(worldIn: World?, pos: BlockPos?, state: IBlockState?, playerIn: EntityPlayer?, hand: EnumHand?, heldItem: ItemStack?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val tile = worldIn!!.getTileEntity(pos!!)
        return tile is TileEntityFoundationStone && tile.onBlockActivated(playerIn!!)
    }

    override fun createItemForm(): ItemBlock? {
        val item = ItemModBlock(this)
        item.addPropertyOverride(LibLocations.FLAT_CHALK) { stack, world, entityLivingBase -> if (ItemNBTHelper.getBoolean(stack.copy(), LibNBT.FLAT, false)) 1.0f else 0.0f }
        return item
    }

    companion object {
        //var pages: MutableList<IPage> = ArrayList()
    }
}
