package eladkay.quaeritum.common.rituals.diagrams


//import com.teamwizardry.librarianlib.client.book.gui.PageText
import com.google.common.collect.Lists
import eladkay.quaeritum.api.rituals.IDiagram
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.rituals.PositionedBlockChalk
import net.minecraft.init.Items
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import java.util.*

class ShardedSkiesTier2Diagram : IDiagram {

    override fun getUnlocalizedName(): String {
        return "rituals.quaeritum.shardedsky"
    }

    override fun run(world: World, pos: BlockPos, te: TileEntity) {
//        val item = EntityItem(world, pos.animX.toDouble(), (pos.y + 2).toDouble(), pos.z.toDouble(), ItemStack(ModBlocks.flower, 1, BlockFiresoulFlower.Variants.IRONHEART.ordinal))
//        world.spawnEntity(item)
        for (stack in IDiagram.Helper.entitiesAroundAltar(te, 4.0)) {
            val server = te.world as WorldServer
            server.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, stack.position.x + 0.5, stack.position.y + 1.0, stack.position.z + 0.5, 1, 0.1, 0.0, 0.1, 0.0)
            stack.setDead()
        }
    }

    override fun canRitualRun(world: World, pos: BlockPos, tile: TileEntity): Boolean {
        return true
    }

    override fun hasRequiredItems(world: World, pos: BlockPos, tile: TileEntity): Boolean {
        return IDiagram.Helper.matches(IDiagram.Helper.stacksAroundAltar(tile, 4.0), requiredItems)
    }

    override fun getPrepTime(world: World, pos: BlockPos, tile: TileEntity): Int {
        return 50
    }

    override fun onPrepUpdate(world: World, pos: BlockPos, tile: TileEntity, ticksRemaining: Int): Boolean {
        return requiredItems.all { IDiagram.Helper.isStackInList(it, IDiagram.Helper.stacksAroundAltar(tile, 4.0)) }
    }

    val requiredItems: ArrayList<ItemStack>
        get() {
            val list = Lists.newArrayList<ItemStack>()
//            list.add(ItemStack(ModBlocks.flower, 1, BlockFiresoulFlower.Variants.FIRESOUL.ordinal))
            list.add(ItemStack(Items.NETHER_WART))
            list.add(ItemStack(Items.BLAZE_POWDER))
            return list
        }

    override fun buildChalks(chalks: MutableList<PositionedBlock>) {
        chalks.add(PositionedBlockChalk(EnumDyeColor.GREEN, BlockPos(-1, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(0, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GREEN, BlockPos(1, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(-1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GREEN, BlockPos(-1, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(0, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GREEN, BlockPos(1, 0, 1)))
    }

    companion object {
        //var pages: MutableList<IPage> = ArrayList()
    }

}
