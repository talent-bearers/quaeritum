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
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemPassionateSoulstone extends ItemMod implements INetworkProvider, IFuelHandler {

    public ItemPassionateSoulstone() {
        super(LibNames.PASSIONATE_SOULSTONE);
        setMaxStackSize(1);
        GameRegistry.registerFuelHandler(this);
    }

    @Override
    public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
        if (getPlayer(itemStack) == null) return null;
        AnimusHelper.Network.addAnimus(getPlayer(itemStack), -4);
        ItemStack copiedStack = itemStack.copy();
        copiedStack.stackSize = 1;
        return copiedStack;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        AnimusHelper.Network.addInformation(stack, tooltip, advanced);
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean isProvider(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public boolean isReceiver(@Nonnull ItemStack stack) {
        return false;
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

    @Override
    public int getBurnTime(ItemStack fuel) {
        return fuel.getItem() == this && AnimusHelper.Network.getAnimus(getPlayer(fuel)) >= 4 ? 200 : 0;
    }
}

