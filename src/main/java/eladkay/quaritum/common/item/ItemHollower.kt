package eladkay.quaritum.common.item

import com.google.common.collect.Lists
import com.teamwizardry.librarianlib.common.base.item.ItemMod
import com.teamwizardry.librarianlib.common.util.plus
import eladkay.quaritum.api.lib.LibNBT
import eladkay.quaritum.api.util.ItemNBTHelper
import eladkay.quaritum.common.lib.LibNames
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
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
import java.util.function.Consumer

class ItemHollower : ItemMod(LibNames.HOLLOWER) {
    init {
        setMaxStackSize(1)
    }

    override fun onBlockDestroyed(stack: ItemStack?, worldIn: World?, state: IBlockState?, pos: BlockPos?, entityLiving: EntityLivingBase?): Boolean {
        ItemNBTHelper.setLong(stack, LibNBT.CORNER1, pos!!.toLong())
        if (entityLiving is EntityPlayer) entityLiving.addChatComponentMessage(TextComponentString(TextFormatting.GREEN + "First corner set to: " + pos))
        return false
    }

    override fun onItemUse(stack: ItemStack?, playerIn: EntityPlayer?, worldIn: World?, pos: BlockPos?, hand: EnumHand?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        ItemNBTHelper.setLong(stack, LibNBT.CORNER2, pos!!.toLong())
        playerIn!!.addChatComponentMessage(TextComponentString(TextFormatting.GREEN + "Second corner set to: " + pos))
        return EnumActionResult.PASS
    }

    override fun onItemRightClick(stack: ItemStack, worldIn: World?, playerIn: EntityPlayer?, hand: EnumHand?): ActionResult<ItemStack> {
        val pos1 = ItemNBTHelper.getLong(stack, LibNBT.CORNER1, -1)
        val pos2 = ItemNBTHelper.getLong(stack, LibNBT.CORNER2, -1)
        var flag = 0
        val poses = Lists.newArrayList<BlockPos>()
        if (pos1 == -1.toLong() || pos2 == -1.toLong()) return ActionResult.newResult(EnumActionResult.FAIL, stack)
        for (pos in BlockPos.getAllInBox(BlockPos.fromLong(pos1), BlockPos.fromLong(pos2))) {
            var flag0 = true
            for (facing in EnumFacing.values()) if (worldIn!!.getBlockState(pos.offset(facing)).block === Blocks.AIR) flag0 = false
            if (flag0) {
                poses.add(pos)
                flag++
            }
        }
        poses.forEach(Consumer<BlockPos> { worldIn!!.setBlockToAir(it) })

        playerIn!!.addChatComponentMessage(TextComponentString(TextFormatting.GREEN + "Done! Blocks affected: " + flag))
        return ActionResult.newResult(EnumActionResult.PASS, stack)
    }
}
