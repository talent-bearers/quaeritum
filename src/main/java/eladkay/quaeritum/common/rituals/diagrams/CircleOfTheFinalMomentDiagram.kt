package eladkay.quaeritum.common.rituals.diagrams




import eladkay.quaeritum.api.rituals.IDiagram
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.rituals.PositionedBlockChalk
import eladkay.quaeritum.common.networking.FancyParticlePacket
import eladkay.quaeritum.common.networking.NetworkHelper
import net.minecraft.item.EnumDyeColor
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class CircleOfTheFinalMomentDiagram : IDiagram {

    override fun getUnlocalizedName(): String {
        return "circleofthefinalmoment"
    }

    override fun run(world: World, pos: BlockPos, tile: TileEntity) {
        //noop lol
    }

    override fun constructBook() {
        /*pages.add(PageText(TooltipHelper.local(LibBook.ENTRY_CIRCLE_OF_THE_FINAL_MOMENT_PAGE1)))
        val list = ArrayList<PositionedBlock>()
        buildChalks(list)
        pages.add(PageDiagram(list, ImmutableList.of<ItemStack>()))
        ModBook.register(ModBook.pagesDiagrams, LibBook.ENTRY_CIRCLE_OF_THE_FINAL_MOMENT, pages, ItemStack(Items.CLOCK))*/
    }

    override fun getPrepTime(world: World, pos: BlockPos, tile: TileEntity): Int {
        return 600
    }

    override fun onPrepUpdate(world: World, pos: BlockPos, tile: TileEntity, ticksRemaining: Int): Boolean {
        NetworkHelper.tellEveryoneAround(FancyParticlePacket(pos.x + 0.25, pos.up().y.toDouble(), pos.z + 0.25, 50), world.provider.dimension, pos, 32)
        incrementAllWorldTimes(world, 20)
        return true
    }

    override fun canRitualRun(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        return true
    }

    override fun hasRequiredItems(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        return true
    }

    override fun buildChalks(chalks: MutableList<PositionedBlock>) {
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(-4, 0, -4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-2, 0, -4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-1, 0, -4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(0, 0, -4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(1, 0, -4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(2, 0, -4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(4, 0, -4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-3, 0, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-2, 0, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(0, 0, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(2, 0, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(3, 0, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-4, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-3, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(-2, 0, -2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-1, 0, -2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(0, 0, -2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(1, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(2, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(3, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(4, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-4, 0, -1)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-2, 0, -1)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-1, 0, -1)))
        chalks.add(PositionedBlockChalk(null, BlockPos(1, 0, -1)))
        chalks.add(PositionedBlockChalk(null, BlockPos(2, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(4, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-4, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(-3, 0, 0)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-2, 0, 0)))
        chalks.add(PositionedBlockChalk(null, BlockPos(2, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(3, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(4, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-4, 0, 1)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-2, 0, 1)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-1, 0, 1)))
        chalks.add(PositionedBlockChalk(null, BlockPos(1, 0, 1)))
        chalks.add(PositionedBlockChalk(null, BlockPos(2, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(4, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-4, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-3, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(-2, 0, 2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-1, 0, 2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(0, 0, 2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(1, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(2, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(3, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(4, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-3, 0, 3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-2, 0, 3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(0, 0, 3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(2, 0, 3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(3, 0, 3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(-4, 0, 4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-2, 0, 4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-1, 0, 4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(0, 0, 4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(1, 0, 4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(2, 0, 4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(4, 0, 4)))
    }

    companion object {
        fun incrementAllWorldTimes(worldserver: World, amount: Int) {
            worldserver.worldTime = worldserver.worldTime + amount.toLong()
        }

        //var pages: MutableList<IPage> = ArrayList()
    }
}
