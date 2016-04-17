package eladkay.quaritum.common.item;

import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class DebugItem extends ItemMod {

    public DebugItem() {
        super(LibNames.DEBUG);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (worldIn.isRemote) {
            if (playerIn.isSneaking())
                playerIn.addChatComponentMessage(new TextComponentString("Animus levels: " + playerIn.getEntityData().getInteger(LibNames.TAG_ANIMUS_ON_ENTITY)));
            else
                playerIn.getEntityData().setInteger(LibNames.TAG_ANIMUS_ON_ENTITY, playerIn.getEntityData().getInteger(LibNames.TAG_ANIMUS_ON_ENTITY) + 50);
        }
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
            playerIn.addChatComponentMessage(new TextComponentString(worldIn.getBlockState(pos).toString()));
        return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}