package eladkay.quaeritum.common.rituals.diagrams

import eladkay.quaeritum.api.rituals.IDiagram
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.rituals.PositionedBlockChalk
import eladkay.quaeritum.common.potions.PotionRooted
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.item.EnumDyeColor
import net.minecraft.potion.PotionEffect
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World


class SealOfTheFive : IDiagram {

    override fun getUnlocalizedName(): String {
        return "eaeye"
    }

    override fun hasRequiredItems(world: World, pos: BlockPos, tile: TileEntity): Boolean {
        return true
    }

    override fun run(world: World, pos: BlockPos, te: TileEntity) {
        val endermen = world.getEntitiesWithinAABB(EntityEnderman::class.java, AxisAlignedBB(pos).grow(7.0))
        for ((index, man) in endermen.withIndex()) {
            man.playEndermanSound()
            val angle = 2 * Math.PI.toFloat() * index / endermen.size
            val x = pos.x + MathHelper.cos(angle) * 2.0 + 0.5
            val y = pos.y + 0.1
            val z = pos.z + MathHelper.sin(angle) * 2.0 + 0.5
            man.setLocationAndAngles(x, y, z, angle * 180 / Math.PI.toFloat(), 0f)
            man.addPotionEffect(PotionEffect(PotionRooted, 100))
        }
    }


    override fun canRitualRun(world: World, pos: BlockPos, tile: TileEntity): Boolean {
        return true
    }

    override fun buildChalks(chalks: MutableList<PositionedBlock>) {
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLUE, BlockPos(0, 0, -3)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-2, 0, -2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-1, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLUE, BlockPos(0, 0, -2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(1, 0, -2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(2, 0, -2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-2, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLUE, BlockPos(0, 0, -1)))
        chalks.add(PositionedBlockChalk(null, BlockPos(2, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIGHT_BLUE, BlockPos(-3, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIGHT_BLUE, BlockPos(-2, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIGHT_BLUE, BlockPos(-1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GRAY, BlockPos(1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GRAY, BlockPos(2, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GRAY, BlockPos(3, 0, 0)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-2, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.ORANGE, BlockPos(0, 0, 1)))
        chalks.add(PositionedBlockChalk(null, BlockPos(2, 0, 1)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-2, 0, 2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(-1, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.ORANGE, BlockPos(0, 0, 2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(1, 0, 2)))
        chalks.add(PositionedBlockChalk(null, BlockPos(2, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.ORANGE, BlockPos(0, 0, 3)))
    }
}
