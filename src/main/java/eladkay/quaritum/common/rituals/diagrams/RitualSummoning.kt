package eladkay.quaritum.common.rituals.diagrams

import amerifrance.guideapi.api.IPage
import amerifrance.guideapi.page.PageText
import com.google.common.collect.Lists
import com.teamwizardry.librarianlib.client.util.TooltipHelper
import eladkay.quaritum.api.animus.ISoulstone
import eladkay.quaritum.api.lib.LibBook
import eladkay.quaritum.api.rituals.IDiagram
import eladkay.quaritum.api.rituals.PositionedBlock
import eladkay.quaritum.api.rituals.PositionedBlockChalk
import eladkay.quaritum.common.block.ModBlocks
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower
import eladkay.quaritum.common.book.ModBook
import eladkay.quaritum.common.book.PageDiagram
import eladkay.quaritum.common.entity.EntityChaosborn
import eladkay.quaritum.common.item.ModItems
import eladkay.quaritum.common.networking.LightningEffectPacket
import eladkay.quaritum.common.networking.NetworkHelper
import net.minecraft.init.Items
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

class RitualSummoning : IDiagram {

    override fun getUnlocalizedName(): String {
        return "rituals.quaritum.templeoftherift"
    }

    override fun run(world: World, pos: BlockPos, te: TileEntity) {
        val x = pos.x + 0.5
        val y = (pos.y + 2).toDouble()
        val z = pos.z + 0.5
        var rarity = 0
        for (item in IDiagram.Helper.entitiesAroundAltar(te, 4.0)) {
            val stack = item.entityItem
            if (stack.item !is ISoulstone) continue
            val ss = stack.item as ISoulstone
            rarity = Math.max(ss.getRarityLevel(stack), rarity)
            NetworkHelper.tellEveryoneAround(LightningEffectPacket(item.posX, item.posY, item.posZ), world.provider.dimension, pos.x, pos.y, pos.z, 4)
            item.setDead()
        }
        val chaosborn = EntityChaosborn(world, rarity, x, y, z)

        world.worldTime = 23000
        world.spawnEntityInWorld(chaosborn)
    }

    private fun op(`in`: Double): Double {
        return if (Math.random() >= 0.5) `in` else -`in`
    }

    override fun getPrepTime(world: World, pos: BlockPos, tile: TileEntity): Int {
        return 50
    }

    override fun onPrepUpdate(world: World, pos: BlockPos, tile: TileEntity, ticksRemaining: Int): Boolean {
        NetworkHelper.tellEveryoneAround(LightningEffectPacket(pos.x + op(Math.random() * 4), pos.y.toDouble(), pos.z + op(Math.random() * 4)), world.provider.dimension, pos.x, pos.y, pos.z, 4)
        for (stack in IDiagram.Helper.stacksAroundAltar(tile, 4.0))
            if (IDiagram.Helper.isStackInList(stack, requiredItems)) return true
        return false
    }

    override fun canRitualRun(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        return true
    }

    override fun hasRequiredItems(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        return IDiagram.Helper.matches(IDiagram.Helper.stacksAroundAltar(tile, 4.0), requiredItems)
    }

    val requiredItems: ArrayList<ItemStack>
        get() {
            val list = Lists.newArrayList<ItemStack>()
            list.add(ItemStack(ModBlocks.flower, 1, BlockAnimusFlower.Variants.COMMON_ARCANE.ordinal))
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

    override fun constructBook() {
        pages.add(PageText(TooltipHelper.local(LibBook.ENTRY_SUMMONING_PAGE1)))
        pages.add(PageText(TooltipHelper.local(LibBook.ENTRY_SUMMONING_PAGE2)))
        val l = Lists.newArrayList<PositionedBlock>()
        buildChalks(l)
        pages.add(PageDiagram(l, requiredItems))
        ModBook.register(ModBook.pagesDiagrams, LibBook.ENTRY_SUMMONING_NAME, pages, ItemStack(ModItems.altas))
    }

    companion object {
        var pages: MutableList<IPage> = ArrayList()
    }
}
