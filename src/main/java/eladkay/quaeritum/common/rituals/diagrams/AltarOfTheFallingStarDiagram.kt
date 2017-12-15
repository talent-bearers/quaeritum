package eladkay.quaeritum.common.rituals.diagrams

import com.teamwizardry.librarianlib.features.kotlin.sendSpamlessMessage
import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.api.animus.INetworkProvider
import eladkay.quaeritum.api.animus.ISoulstone
import eladkay.quaeritum.api.rituals.IDiagram
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.rituals.PositionedBlockChalk
import net.minecraft.item.EnumDyeColor
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import java.util.*

class AltarOfTheFallingStarDiagram : IDiagram {

    override fun getUnlocalizedName(): String {
        return "rituals.quaeritum.altarofthefallingstar"
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
            val stack = item.item
            if (stack.item !is INetworkProvider || !(stack.item as INetworkProvider).isReceiver(stack))
                continue
            player = (stack.item as INetworkProvider).getPlayer(stack)
            break
        }
        if (player == null) return

        for (item in entities) {
            val stack = item.item
            if (stack.item !is ISoulstone) continue
            val ss = stack.item as ISoulstone
            AnimusHelper.Network.addAnimus(player, ss.getAnimusLevel(stack))
            AnimusHelper.Network.addTier(player, ss.getAnimusTier(stack))
            item.setDead()
            flag = true
        }
        if (flag)
            for (playerEntity in world.playerEntities)
                if (playerEntity.uniqueID == player)
                    playerEntity.sendSpamlessMessage(TextComponentTranslation("misc.quaeritum.rushOfEnergy"), 1005)
    }


    override fun canRitualRun(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        val entities = IDiagram.Helper.entitiesAroundAltar(tile, 4.0)
        for (entity in entities) {
            val stack = entity.item
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
        //var pages: MutableList<IPage> = ArrayList()
    }
}
