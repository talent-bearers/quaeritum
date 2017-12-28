package eladkay.quaeritum.common.rituals.diagrams

import com.teamwizardry.librarianlib.features.kotlin.sendSpamlessMessage
import com.teamwizardry.librarianlib.features.network.PacketHandler
import com.teamwizardry.librarianlib.features.network.sendToAllAround
import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.api.animus.INetworkProvider
import eladkay.quaeritum.api.animus.ISoulstone
import eladkay.quaeritum.api.rituals.IDiagram
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.rituals.PositionedBlockChalk
import eladkay.quaeritum.common.networking.PuffMessage
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.EnumDyeColor
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import java.awt.Color
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
        var entity: EntityItem? = null

        var player: UUID? = null
        for (item in entities) {
            val stack = item.item
            if (stack.item !is INetworkProvider || !(stack.item as INetworkProvider).isReceiver(stack))
                continue
            entity = item
            player = (stack.item as INetworkProvider).getPlayer(stack)
            break
        }
        if (player == null || entity == null) return

        var total = 0

        for (item in entities) {
            val stack = item.item
            if (stack.item !is ISoulstone) continue
            val ss = stack.item as ISoulstone
            if (ss.getAnimusTier(stack).ordinal >= AnimusHelper.Network.getTier(player).ordinal) {
                val animus = ss.getAnimusLevel(stack)
                total += animus
                AnimusHelper.Network.addAnimus(player, animus)
                AnimusHelper.Network.addTier(player, ss.getAnimusTier(stack))
                ss.setAnimus(stack, 0)
                item.item = ss.drainedStack(stack)
                if (item.item.isEmpty) item.setDead()
                PacketHandler.NETWORK.sendToAllAround(PuffMessage(item.positionVector, amount = animus / 1000, color = Color(0x40FF40)),
                        world, item.positionVector, 64)
                flag = true
            }
        }
        if (flag) {
            world.playerEntities
                    .filter { it.uniqueID == player }
                    .forEach { it.sendSpamlessMessage(TextComponentTranslation("misc.quaeritum.rush_of_energy"), 1005) }
            PacketHandler.NETWORK.sendToAllAround(PuffMessage(entity.positionVector, amount = total / 1000, color = Color(0xFF4040)),
                    world, entity.positionVector, 64)
        }
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
