package eladkay.quaeritum.common.item

import com.google.common.collect.Lists
import com.teamwizardry.librarianlib.common.base.item.ItemMod
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.rituals.PositionedBlockChalk
import eladkay.quaeritum.common.block.ModBlocks
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ItemPlacer : ItemMod(LibNames.DEV_PLACER) {
    init {
        setMaxStackSize(1)
    }

    override fun onItemUse(stack: ItemStack?, playerIn: EntityPlayer?, worldIn: World?, pos: BlockPos?, hand: EnumHand?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (worldIn!!.getBlockState(pos!!).block !== ModBlocks.blueprint && worldIn!!.getBlockState(pos!!).block !== ModBlocks.foundation) return EnumActionResult.FAIL
        val chalks = Lists.newArrayList<PositionedBlock>()
        buildChalksForPlacer(chalks)
        for (block in chalks)
            worldIn!!.setBlockState(pos!!.add(block.getPos()), block.state)
        return EnumActionResult.SUCCESS
    }

    companion object {

        fun buildChalksForPlacer(chalks: MutableList<PositionedBlock>) {
            chalks.add(PositionedBlockChalk(EnumDyeColor.PINK, BlockPos(0, 0, -2)))
            chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-1, 0, -1)))
            chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(0, 0, -1)))
            chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(1, 0, -1)))
            chalks.add(PositionedBlockChalk(EnumDyeColor.PINK, BlockPos(-2, 0, 0)))
            chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-1, 0, 0)))
            chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(1, 0, 0)))
            chalks.add(PositionedBlockChalk(EnumDyeColor.PINK, BlockPos(2, 0, 0)))
            chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-1, 0, 1)))
            chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(0, 0, 1)))
            chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(1, 0, 1)))
            chalks.add(PositionedBlockChalk(EnumDyeColor.PINK, BlockPos(0, 0, 2)))
        }
    }
}
