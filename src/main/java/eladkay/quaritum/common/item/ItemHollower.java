package eladkay.quaritum.common.item;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.lib.LibNBT;
import eladkay.quaritum.api.util.ItemNBTHelper;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemHollower extends ItemMod {
    public ItemHollower() {
        super(LibNames.HOLLOWER);
        setMaxStackSize(1);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        ItemNBTHelper.setLong(stack, LibNBT.CORNER1, pos.toLong());
        if(entityLiving instanceof EntityPlayer) ((EntityPlayer) entityLiving).addChatComponentMessage(new TextComponentString(TextFormatting.GREEN + "First corner set to: " + pos));
        return false;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemNBTHelper.setLong(stack, LibNBT.CORNER2, pos.toLong());
        playerIn.addChatComponentMessage(new TextComponentString(TextFormatting.GREEN + "Second corner set to: " + pos));
        return EnumActionResult.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        long pos1 = ItemNBTHelper.getLong(stack, LibNBT.CORNER1, -1);
        long pos2 = ItemNBTHelper.getLong(stack, LibNBT.CORNER2, -1);
        int flag = 0;
        List<BlockPos> poses = Lists.newArrayList();
        if(pos1 == -1 || pos2 == -1) return ActionResult.newResult(EnumActionResult.FAIL, stack);
        for(BlockPos pos : BlockPos.getAllInBox(BlockPos.fromLong(pos1), BlockPos.fromLong(pos2))) {
            boolean flag0 = true;
            for(EnumFacing facing : EnumFacing.values()) if(worldIn.getBlockState(pos.offset(facing)).getBlock() == Blocks.AIR) flag0 = false;
            if(flag0) {
                poses.add(pos);
                flag++;
            }
        }
        poses.forEach(worldIn::setBlockToAir);

        playerIn.addChatComponentMessage(new TextComponentString(TextFormatting.GREEN + "Done! Blocks affected: " + flag));
        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }
}
