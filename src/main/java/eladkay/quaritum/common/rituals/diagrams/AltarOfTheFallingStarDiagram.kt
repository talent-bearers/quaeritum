package eladkay.quaritum.common.rituals.diagrams

import amerifrance.guideapi.api.IPage
import amerifrance.guideapi.page.PageText
import com.google.common.collect.ImmutableList
import com.teamwizardry.librarianlib.client.util.TooltipHelper
import eladkay.quaritum.api.animus.AnimusHelper
import eladkay.quaritum.api.animus.INetworkProvider
import eladkay.quaritum.api.animus.ISoulstone
import eladkay.quaritum.api.lib.LibBook
import eladkay.quaritum.api.rituals.IDiagram
import eladkay.quaritum.api.rituals.PositionedBlock
import eladkay.quaritum.api.rituals.PositionedBlockChalk
import eladkay.quaritum.common.book.ModBook
import eladkay.quaritum.common.book.PageDiagram
import eladkay.quaritum.common.core.ChatHelper
import eladkay.quaritum.common.item.ModItems
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import java.util.*

class AltarOfTheFallingStarDiagram : IDiagram {

    override fun constructBook() {
        pages.add(PageText(TooltipHelper.local(LibBook.ENTRY_CIRCLE_OF_THE_FINAL_MOMENT_PAGE1)))
        val list = ArrayList<PositionedBlock>()
        buildChalks(list)
        pages.add(PageDiagram(list, ImmutableList.of<ItemStack>()))
        ModBook.register(ModBook.pagesDiagrams, LibBook.ENTRY_ALTAR_OF_THE_FALLING_STAR, pages, ItemStack(ModItems.awakened))
    }

    override fun getUnlocalizedName(): String {
        return "rituals.quaritum.altarofthefallingstar"
    }

    override fun hasRequiredItems(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        val stacks = IDiagram.Helper.stacksAroundAltar(tile, 4.0)
        for (stack in stacks) {
            if (stack.item is ISoulstone) return true
        }
        return false
    }

    override fun run(world: World, pos: BlockPos, te: TileEntity) {
        var flag = false

        val entities = IDiagram.Helper.entitiesAroundAltar(te, 4.0)

        var player: UUID? = null
        for (item in entities) {
            val stack = item.entityItem
            if (stack.item !is INetworkProvider || !(stack.item as INetworkProvider).isReceiver(stack))
                continue
            player = (stack.item as INetworkProvider).getPlayer(stack)
            break
        }
        if (player == null) return

        for (item in entities) {
            val stack = item.entityItem
            if (stack.item !is ISoulstone) continue
            val ss = stack.item as ISoulstone
            AnimusHelper.Network.addAnimus(player, ss.getAnimusLevel(stack))
            AnimusHelper.Network.addRarity(player, ss.getRarityLevel(stack))
            item.setDead()
            flag = true
        }
        if (flag)
            for (playerEntity in world.playerEntities)
                if (playerEntity.uniqueID == player)
                    ChatHelper.sendNoSpam2(playerEntity, TextComponentTranslation("misc.quaritum.rushOfEnergy"))
    }


    override fun canRitualRun(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        val entities = IDiagram.Helper.entitiesAroundAltar(tile, 4.0)
        for (entity in entities) {
            val stack = entity.entityItem
            if (stack.item is INetworkProvider && (stack.item as INetworkProvider).isReceiver(stack))
                return true
        }
        return false
    }

    override fun buildChalks(chalks: MutableList<PositionedBlock>) {
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

    companion object {
        var pages: MutableList<IPage> = ArrayList()
    }
}
