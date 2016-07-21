package eladkay.quaritum.common.item.soulstones;

import eladkay.quaritum.api.animus.AnimusHelper;
import eladkay.quaritum.api.animus.INetworkProvider;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemAttunedSoulstone extends ItemMod implements INetworkProvider {
    public ItemAttunedSoulstone() {
        super(LibNames.ATTUNED_SOULSTONE);
        setMaxStackSize(1);
    }

    @Override
    public boolean isProvider(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public boolean isReceiver(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        AnimusHelper.Network.addInformation(stack, tooltip, advanced);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (getPlayer(stack) == null && entityIn instanceof EntityPlayer)
            setPlayer(stack, entityIn.getUniqueID());
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (playerIn.isSneaking()) {
            setPlayer(itemStackIn, playerIn.getUniqueID());
            worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYERS, 1f, 1f);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
        }
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }
}
