package eladkay.quaritum.common.item;

import eladkay.quaritum.api.animus.AnimusHelper;
import eladkay.quaritum.common.core.ChatHelper;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDebug extends ItemMod {

    public ItemDebug() {
        super(LibNames.DEBUG);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
     /*   if (!worldIn.isRemote)
            if (!playerIn.isSneaking())
                playerIn.addChatComponentMessage(new TextComponentString("Animus levels: " + playerIn.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger(LibNBT.TAG_ANIMUS_ON_ENTITY)));
            else {
                NBTTagCompound data = playerIn.getEntityData();
                if (!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
                    data.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
                NBTTagCompound persist = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
                persist.setInteger(LibNBT.TAG_ANIMUS_ON_ENTITY, playerIn.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger(LibNBT.TAG_ANIMUS_ON_ENTITY) + 50);
                playerIn.addChatComponentMessage(new TextComponentString("Added 50, current animus level for " + playerIn.getName() + " is: " + playerIn.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger(LibNBT.TAG_ANIMUS_ON_ENTITY)));
            }*/
        if (!worldIn.isRemote)
            if (GuiScreen.isShiftKeyDown()) {
                AnimusHelper.Network.addAnimus(playerIn, 50);
                ChatHelper.sendNoSpam2(playerIn, "Added 50, current animus level for " + playerIn.getName() + " is: " + AnimusHelper.Network.getAnimus(playerIn));
            } else if (GuiScreen.isCtrlKeyDown()) {
                AnimusHelper.Network.addAnimus(playerIn, -50);
                ChatHelper.sendNoSpam2(playerIn, "Took 50, current animus level for " + playerIn.getName() + " is: " + AnimusHelper.Network.getAnimus(playerIn));
            } else
                ChatHelper.sendNoSpam2(playerIn, "Current animus level for " + playerIn.getName() + " is: " + AnimusHelper.Network.getAnimus(playerIn));

        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        //ChatHelper.sendNoSpam1(playerIn, worldIn.getBlockState(pos).toString());
        ChatHelper.sendChat(playerIn, worldIn.getBlockState(pos).toString());
        return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
