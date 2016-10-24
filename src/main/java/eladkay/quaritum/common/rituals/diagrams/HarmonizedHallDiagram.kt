package eladkay.quaritum.common.rituals.diagrams

import eladkay.quaritum.api.animus.ISoulstone
import eladkay.quaritum.api.rituals.IDiagram
import eladkay.quaritum.api.rituals.PositionedBlock
import eladkay.quaritum.api.rituals.PositionedBlockChalk
import eladkay.quaritum.common.crafting.recipes.RecipeAnimusUpgrade
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.EnumDyeColor
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HarmonizedHallDiagram : IDiagram {
    override fun getUnlocalizedName(): String {
        return "rituals.quaritum.merger"
    }

    override fun run(world: World, pos: BlockPos, tile: TileEntity) {
        for (item in IDiagram.Helper.entitiesAroundAltar(tile, 4.0)) {
            val stack = item.entityItem
            if (stack.item is ISoulstone)
                item.setDead()
        }

        val item = EntityItem(world, pos.x.toDouble(), (pos.y + 2).toDouble(), pos.z.toDouble(), RecipeAnimusUpgrade.output(IDiagram.Helper.stacksAroundAltar(tile, 4.0)))
        world.spawnEntityInWorld(item)
    }

    override fun hasRequiredItems(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        var flag = false
        val stacks = IDiagram.Helper.stacksAroundAltar(tile, 4.0)
        for (stack in stacks) {
            if (stack.item is ISoulstone) {
                if (flag) return true
                flag = true
            }
        }
        return false
    }

    override fun canRitualRun(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        return true
    }

    override fun buildChalks(chalks: MutableList<PositionedBlock>) {
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIGHT_BLUE, BlockPos(-2, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GREEN, BlockPos(-1, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GREEN, BlockPos(0, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GREEN, BlockPos(1, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIGHT_BLUE, BlockPos(2, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GREEN, BlockPos(-2, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(-1, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(0, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(1, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GREEN, BlockPos(2, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GREEN, BlockPos(-2, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(-1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GREEN, BlockPos(2, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GREEN, BlockPos(-2, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(-1, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(0, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.MAGENTA, BlockPos(1, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GREEN, BlockPos(2, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIGHT_BLUE, BlockPos(-2, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GREEN, BlockPos(-1, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GREEN, BlockPos(0, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GREEN, BlockPos(1, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIGHT_BLUE, BlockPos(2, 0, 2)))
    }
}
