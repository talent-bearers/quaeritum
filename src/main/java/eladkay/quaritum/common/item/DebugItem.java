package eladkay.quaritum.common.item;

import eladkay.quaritum.common.item.base.ItemMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class DebugItem extends ItemMod {
    public DebugItem(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
            playerIn.addChatComponentMessage(new TextComponentString(worldIn.getBlockState(pos).toString()));
        return EnumActionResult.SUCCESS;
    }
}
