package eladkay.quaeritum.common.rituals.works

import eladkay.quaeritum.api.animus.EnumAnimusTier
import eladkay.quaeritum.api.rituals.IDiagram
import eladkay.quaeritum.api.rituals.IWork
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.rituals.PositionedBlockChalk
import eladkay.quaeritum.common.networking.FancyParticlePacket
import eladkay.quaeritum.common.networking.NetworkHelper
import eladkay.quaeritum.common.rituals.diagrams.CircleOfTheFinalMomentDiagram
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.EnumDyeColor
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class CircleOfTheFinalMomentWork : IWork {
    override fun getUnlocalizedName(): String {
        return "circleofthefinalmoment"
    }

    override fun updateTick(world: World, pos: BlockPos, tile: TileEntity, ticksExisted: Long): Boolean {
        for (pos0 in BlockPos.getAllInBox(BlockPos(-4, 1, -4).add(pos), BlockPos(4, 8, 4).add(pos)))
            if (world.getTileEntity(pos0) != null && world.getTileEntity(pos0) is ITickable) {
                for (i in 0..3) (world.getTileEntity(pos0) as ITickable).update()
                NetworkHelper.tellEveryoneAround(FancyParticlePacket(pos0.x + 0.25, pos0.y - 0.5, pos0.z + 0.25, 5), world.provider.dimension, pos, 32)
            }
        NetworkHelper.tellEveryoneAround(FancyParticlePacket(pos.x + 0.25, pos.y.toDouble(), pos.z + 0.25, 50), world.provider.dimension, pos, 32)
        return IDiagram.Helper.consumeAnimusForRitual(tile, true, 1, EnumAnimusTier.QUAERITUS)
        //ticksExisted < 1200;
    }

    override fun initialTick(world: World, pos: BlockPos, tile: TileEntity, player: EntityPlayer?): Boolean {
        return true
    }

    override fun canRitualRun(world: World, pos: BlockPos, tile: TileEntity): Boolean {
        return true
    }

    override fun buildPositions(chalks: MutableList<PositionedBlock>) {
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-3, 1, -7)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-2, 1, -7)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-1, 1, -7)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(0, 1, -7)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(1, 1, -7)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(2, 1, -7)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(3, 1, -7)))
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(-6, 1, -6), null)) // minecraft:obsidian
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-5, 1, -6)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-4, 1, -6)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-3, 1, -6)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(3, 1, -6)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(4, 1, -6)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(5, 1, -6)))
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(6, 1, -6), null)) // minecraft:obsidian
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(-6, 2, -6), null)) // minecraft:obsidian
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(6, 2, -6), null)) // minecraft:obsidian
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(-6, 3, -6), null)) // minecraft:obsidian
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(6, 3, -6), null)) // minecraft:obsidian
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(-6, 4, -6)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(6, 4, -6)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-6, 1, -5)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-5, 1, -5)))
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(0, 1, -5), null)) // minecraft:obsidian
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(5, 1, -5)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(6, 1, -5)))
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(0, 2, -5), null)) // minecraft:obsidian
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(0, 3, -5), null)) // minecraft:obsidian
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(0, 4, -5), null)) // minecraft:obsidian
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(0, 5, -5)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-6, 1, -4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(-4, 1, -4)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-2, 1, -4)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-1, 1, -4)))
        chalks.add(PositionedBlockChalk(null, BlockPos(0, 1, -4)))
        chalks.add(PositionedBlockChalk(null, BlockPos(1, 1, -4)))
        chalks.add(PositionedBlockChalk(null, BlockPos(2, 1, -4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(4, 1, -4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(6, 1, -4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-7, 1, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-6, 1, -3)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-3, 1, -3)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-2, 1, -3)))
        chalks.add(PositionedBlockChalk(null, BlockPos(2, 1, -3)))
        chalks.add(PositionedBlockChalk(null, BlockPos(3, 1, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(6, 1, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(7, 1, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-7, 1, -2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-4, 1, -2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-3, 1, -2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(3, 1, -2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(4, 1, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(7, 1, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-7, 1, -1)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-4, 1, -1)))
        chalks.add(PositionedBlockChalk(null, BlockPos(4, 1, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(7, 1, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-7, 1, 0)))
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(-5, 1, 0), null)) // minecraft:obsidian
        chalks.add(PositionedBlockChalk(null, BlockPos(-4, 1, 0)))
        chalks.add(PositionedBlockChalk(null, BlockPos(4, 1, 0)))
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(5, 1, 0), null)) // minecraft:obsidian
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(7, 1, 0)))
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(-5, 2, 0), null)) // minecraft:obsidian
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(5, 2, 0), null)) // minecraft:obsidian
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(-5, 3, 0), null)) // minecraft:obsidian
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(5, 3, 0), null)) // minecraft:obsidian
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(-5, 4, 0), null)) // minecraft:obsidian
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(5, 4, 0), null)) // minecraft:obsidian
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(-5, 5, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(5, 5, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-7, 1, 1)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-4, 1, 1)))
        chalks.add(PositionedBlockChalk(null, BlockPos(4, 1, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(7, 1, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-7, 1, 2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-4, 1, 2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-3, 1, 2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(3, 1, 2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(4, 1, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(7, 1, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-7, 1, 3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-6, 1, 3)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-3, 1, 3)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-2, 1, 3)))
        chalks.add(PositionedBlockChalk(null, BlockPos(2, 1, 3)))
        chalks.add(PositionedBlockChalk(null, BlockPos(3, 1, 3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(6, 1, 3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(7, 1, 3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-6, 1, 4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(-4, 1, 4)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-2, 1, 4)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-1, 1, 4)))
        chalks.add(PositionedBlockChalk(null, BlockPos(0, 1, 4)))
        chalks.add(PositionedBlockChalk(null, BlockPos(1, 1, 4)))
        chalks.add(PositionedBlockChalk(null, BlockPos(2, 1, 4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(4, 1, 4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(6, 1, 4)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-6, 1, 5)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-5, 1, 5)))
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(0, 1, 5), null)) // minecraft:obsidian
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(5, 1, 5)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(6, 1, 5)))
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(0, 2, 5), null)) // minecraft:obsidian
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(0, 3, 5), null)) // minecraft:obsidian
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(0, 4, 5), null)) // minecraft:obsidian
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(0, 5, 5)))
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(-6, 1, 6), null)) // minecraft:obsidian
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-5, 1, 6)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-4, 1, 6)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-3, 1, 6)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(3, 1, 6)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(4, 1, 6)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(5, 1, 6)))
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(6, 1, 6), null)) // minecraft:obsidian
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(-6, 2, 6), null)) // minecraft:obsidian
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(6, 2, 6), null)) // minecraft:obsidian
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(-6, 3, 6), null)) // minecraft:obsidian
        chalks.add(PositionedBlock(Blocks.OBSIDIAN.defaultState, BlockPos(6, 3, 6), null)) // minecraft:obsidian
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(-6, 4, 6)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.RED, BlockPos(6, 4, 6)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-3, 1, 7)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-2, 1, 7)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(-1, 1, 7)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(0, 1, 7)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(1, 1, 7)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(2, 1, 7)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.YELLOW, BlockPos(3, 1, 7)))
    }

    override fun getDiagramCounterpart(): IDiagram? {
        return CircleOfTheFinalMomentDiagram()
    }
}
