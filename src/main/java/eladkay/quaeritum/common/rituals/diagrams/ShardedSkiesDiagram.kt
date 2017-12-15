package eladkay.quaeritum.common.rituals.diagrams

//import com.teamwizardry.librarianlib.client.book.gui.PageText
import eladkay.quaeritum.api.rituals.IDiagram
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.rituals.PositionedBlockChalk
import eladkay.quaeritum.common.block.ModBlocks
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.Blocks
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldServer

class ShardedSkiesDiagram : IDiagram {

    override fun getUnlocalizedName(): String {
        return "rituals.quaeritum.shardedsky"
    }

    override fun run(world: World, pos: BlockPos, tes: TileEntity) {

        for (stack in IDiagram.Helper.entitiesAroundAltar(tes, 4.0)) {
            if (!IDiagram.Helper.isEntityItemInList(stack, requiredItems)) continue
            val server = tes.world as WorldServer
            server.spawnParticle(EnumParticleTypes.FLAME, stack.posX, stack.posY + 0.25, stack.posZ, 1, 0.1, 0.0, 0.1, 0.0)
            stack.item.shrink(1)
            if (stack.item.isEmpty)
                stack.setDead()

            val item = EntityItem(world, stack.posX, stack.posY + 0.125, stack.posZ, ItemStack(ModBlocks.flower))
            world.spawnEntity(item)
        }

    }

    override fun onPrepUpdate(world: World, pos: BlockPos, tile: TileEntity, ticksRemaining: Int): Boolean {
        for (stack in IDiagram.Helper.entitiesAroundAltar(tile, 4.0)) {
            if (!IDiagram.Helper.isEntityItemInList(stack, requiredItems)) continue
            val server = tile.world as WorldServer
            server.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, stack.posX, stack.posY + 0.25, stack.posZ, 1, 0.1, 0.0, 0.1, 0.0)
        }
        return hasRequiredItems(world, pos, tile)
    }

    override fun getPrepTime(world: World, pos: BlockPos, tile: TileEntity): Int {
        return 50
    }

    override fun hasRequiredItems(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        return IDiagram.Helper.matches(IDiagram.Helper.stacksAroundAltar(tile, 4.0), requiredItems)
    }

    override fun canRitualRun(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        return true
    }

    val requiredItems: List<ItemStack>
        get() = listOf(ItemStack(Blocks.RED_FLOWER))

    override fun buildChalks(chalks: MutableList<PositionedBlock>) {
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(0, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(-1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(0, 0, 1)))
    }

    companion object {

        //var pages: MutableList<IPage> = ArrayList()
    }

}
