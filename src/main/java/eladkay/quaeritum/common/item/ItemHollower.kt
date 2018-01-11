package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.kotlin.plus
import eladkay.quaeritum.api.lib.LibNBT
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World

class ItemHollower : ItemMod(LibNames.HOLLOWER) {
    init {
        setMaxStackSize(1)
    }

    override fun canDestroyBlockInCreative(world: World?, pos: BlockPos?, stack: ItemStack?, player: EntityPlayer?): Boolean {
        return false
    }

    override fun onItemUse(playerIn: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        val stack = playerIn.getHeldItem(hand)
        val tag = if (playerIn.isSneaking) LibNBT.CORNER2 else LibNBT.CORNER1
        ItemNBTHelper.setLong(stack, tag, pos.toLong())
        if (!worldIn.isRemote)
            playerIn.sendMessage(TextComponentString(TextFormatting.GREEN + "$tag set to: " + pos))
        return EnumActionResult.SUCCESS
    }

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val stack = playerIn.getHeldItem(hand)

        if (!worldIn.isRemote) {
            val pos1 = ItemNBTHelper.getLong(stack, LibNBT.CORNER1, -1)
            val pos2 = ItemNBTHelper.getLong(stack, LibNBT.CORNER2, -1)
            if (pos1 == -1L || pos2 == -1L) return ActionResult.newResult(EnumActionResult.FAIL, stack)
            val from = BlockPos.fromLong(pos1)
            val to = BlockPos.fromLong(pos2)
            val keep = shellProgress(worldIn, from, to)
            val count = BlockPos.getAllInBox(from, to)
                    .filter { it !in keep }
                    .filter { worldIn.setBlockToAir(it) }.size

            playerIn.sendMessage(TextComponentString(TextFormatting.GREEN + "Done! Blocks affected: " + count))
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack)
    }

    private fun shellProgress(world: World, from: BlockPos, to: BlockPos): Set<BlockPos> {
        val minX = Math.min(from.x, to.x)
        val maxX = Math.max(from.x, to.x)
        val minY = Math.min(from.y, to.y)
        val maxY = Math.max(from.y, to.y)
        val minZ = Math.min(from.z, to.z)
        val maxZ = Math.max(from.z, to.z)

        val toVisit = shellIterable(minX - 1, maxX + 1, minY - 1, maxY + 1, minZ - 1, maxZ + 1).toMutableSet()
        val visited = toVisit.toMutableSet()

        while (toVisit.isNotEmpty()) {
            val visitable = toVisit.first()
            toVisit.remove(visitable)

            visited.add(visitable)

            val state = if (!inBox(minX, maxX, minY, maxY, minZ, maxZ,
                    visitable.x, visitable.y, visitable.z))
                Blocks.AIR.defaultState else world.getBlockState(visitable)
            val material = state.material

            val allClear = state.block.isReplaceable(world, visitable) ||
                    material.isLiquid || material == Material.AIR || material == Material.FIRE
            EnumFacing.VALUES
                    .filter { allClear || state.getBlockFaceShape(world, visitable, it) != BlockFaceShape.SOLID }
                    .map { visitable.offset(it) }
                    .filterTo(toVisit) {
                        it !in visited &&
                                inBox(minX - 1, maxX + 1, minY - 1, maxY + 1, minZ - 1, maxZ + 1,
                                        it.x, it.y, it.z)
                    }
        }

        return visited
    }

    fun inShell(minX: Int, maxX: Int, minY: Int, maxY: Int, minZ: Int, maxZ: Int, x: Int, y: Int, z: Int): Boolean {
        return (x == minX || x == maxX) ||
                (y == minY || y == maxY) ||
                (z == minZ || z == maxZ)
    }

    fun inBox(minX: Int, maxX: Int, minY: Int, maxY: Int, minZ: Int, maxZ: Int, x: Int, y: Int, z: Int): Boolean {
        return (x in minX..maxX) &&
                (y in minY..maxY) &&
                (z in minZ..maxZ)
    }

    private fun shellIterable(minX: Int, maxX: Int, minY: Int, maxY: Int, minZ: Int, maxZ: Int): Iterable<BlockPos> {
        return BlockPos.getAllInBox(minX, minY, minZ, maxX, maxY, maxZ)
                .filter { inShell(minX, maxX, minY, maxY, minZ, maxZ, it.x, it.y, it.z) }
    }
}
