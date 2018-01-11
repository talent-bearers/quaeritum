package eladkay.quaeritum.common.item

import com.google.common.collect.Lists
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.kotlin.plus
import eladkay.quaeritum.api.lib.LibNBT
import eladkay.quaeritum.common.lib.LibNames
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
            var flag = 0
            val poses = Lists.newArrayList<BlockPos>()
            if (pos1 == -1L || pos2 == -1L) return ActionResult.newResult(EnumActionResult.FAIL, stack)
            for (pos in BlockPos.getAllInBox(BlockPos.fromLong(pos1), BlockPos.fromLong(pos2))) {
                if (EnumFacing.values().none { worldIn.getBlockState(pos.offset(it)).block === Blocks.AIR }) {
                    poses.add(pos)
                    flag++
                }
            }
            poses.forEach { worldIn.setBlockToAir(it) }

            playerIn.sendMessage(TextComponentString(TextFormatting.GREEN + "Done! Blocks affected: " + flag))
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack)
    }
}
