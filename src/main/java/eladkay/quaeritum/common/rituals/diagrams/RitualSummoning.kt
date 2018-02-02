package eladkay.quaeritum.common.rituals.diagrams

//import com.teamwizardry.librarianlib.client.book.gui.PageText
import com.google.common.collect.Lists
import eladkay.quaeritum.api.animus.ISoulstone
import eladkay.quaeritum.api.rituals.IDiagram
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.rituals.PositionedBlockChalk
import eladkay.quaeritum.common.block.ModBlocks
import eladkay.quaeritum.common.entity.EntityChaosborn
import eladkay.quaeritum.common.item.ModItems
import net.minecraft.init.Items
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*


class RitualSummoning : IDiagram {

    override fun getUnlocalizedName(): String {
        return "rituals.quaeritum.templeoftherift"
    }

    override fun run(world: World, pos: BlockPos, te: TileEntity) {
        val x = pos.x + 0.5
        val y = (pos.y + 2).toDouble()
        val z = pos.z + 0.5
        var rarity = 0
        for (item in IDiagram.Helper.entitiesAroundAltar(te, 4.0)) {
            val stack = item.item
            if (stack.item !is ISoulstone) continue
            val ss = stack.item as ISoulstone
            rarity = Math.max(ss.getAnimusTier(stack).ordinal, rarity)
            item.setDead()
        }
        val chaosborn = EntityChaosborn(world, rarity, x, y, z)

        world.worldTime = 23000
        world.spawnEntity(chaosborn)
    }

    private fun op(`in`: Double): Double {
        return if (Math.random() >= 0.5) `in` else -`in`
    }

    override fun getPrepTime(world: World, pos: BlockPos, tile: TileEntity): Int {
        return 50
    }

    override fun onPrepUpdate(world: World, pos: BlockPos, tile: TileEntity, ticksRemaining: Int): Boolean {
        for (stack in IDiagram.Helper.stacksAroundAltar(tile, 4.0))
            if (IDiagram.Helper.isStackInList(stack, requiredItems)) return true
        return false
    }

    override fun canRitualRun(world: World, pos: BlockPos, tile: TileEntity): Boolean {
        return true
    }

    override fun hasRequiredItems(world: World, pos: BlockPos, tile: TileEntity): Boolean {
        return IDiagram.Helper.matches(IDiagram.Helper.stacksAroundAltar(tile, 4.0), requiredItems)
    }

    val requiredItems: ArrayList<ItemStack>
        get() {
            val list = Lists.newArrayList<ItemStack>()
//            list.add(ItemStack(ModBlocks.flower, 1, BlockFiresoulFlower.Variants.IRONHEART.ordinal))
            list.add(ItemStack(ModBlocks.crystal))
            list.add(ItemStack(Items.NETHER_WART))
            list.add(ItemStack(ModItems.awakened))
            return list
        }

    override fun buildChalks(chalks: MutableList<PositionedBlock>) {
        chalks.add(PositionedBlockChalk(EnumDyeColor.PINK, BlockPos(-2, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(-1, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(0, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(1, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.PINK, BlockPos(2, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(-2, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.SILVER, BlockPos(-1, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(0, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.SILVER, BlockPos(1, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(2, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(-2, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(-1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(2, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(-2, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.SILVER, BlockPos(-1, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(0, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.SILVER, BlockPos(1, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(2, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.PINK, BlockPos(-2, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(-1, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(0, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(1, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.PINK, BlockPos(2, 0, 2)))

    }

    companion object {
        //var pages: MutableList<IPage> = ArrayList()
    }
}
