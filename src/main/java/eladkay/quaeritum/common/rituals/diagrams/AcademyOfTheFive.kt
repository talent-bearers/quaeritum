package eladkay.quaeritum.common.rituals.diagrams

import eladkay.quaeritum.api.rituals.IDiagram
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.rituals.PositionedBlockChalk
import net.minecraft.item.EnumDyeColor
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World


/**
 * @author WireSegal
 * Created at 12:04 AM on 12/17/17.
 */
class AcademyOfTheFive : IDiagram {
    override fun getUnlocalizedName(): String {
        return "academy_of_the_five"
    }

    override fun run(world: World, pos: BlockPos, tile: TileEntity) {
        throw UnsupportedOperationException()
    }

    override fun canRitualRun(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        throw UnsupportedOperationException()
    }

    override fun hasRequiredItems(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        throw UnsupportedOperationException()
    }

    override fun buildChalks(chalks: MutableList<PositionedBlock>) {
        chalks.add(PositionedBlockChalk(EnumDyeColor.CYAN, BlockPos(-1, 0, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.ORANGE, BlockPos(0, 0, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.CYAN, BlockPos(1, 0, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-2, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-1, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.ORANGE, BlockPos(0, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(1, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(2, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.PURPLE, BlockPos(-3, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-2, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.ORANGE, BlockPos(0, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(2, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(3, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIGHT_BLUE, BlockPos(-3, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIGHT_BLUE, BlockPos(-2, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIGHT_BLUE, BlockPos(-1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GRAY, BlockPos(1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GRAY, BlockPos(2, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GRAY, BlockPos(3, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.PURPLE, BlockPos(-3, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-2, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLUE, BlockPos(0, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(2, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(3, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-2, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-1, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLUE, BlockPos(0, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(1, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(2, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BROWN, BlockPos(-1, 0, 3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLUE, BlockPos(0, 0, 3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BROWN, BlockPos(1, 0, 3)))
    }
}
