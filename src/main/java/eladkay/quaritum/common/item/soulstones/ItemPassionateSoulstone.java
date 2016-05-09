package eladkay.quaritum.common.item.soulstones;

import eladkay.quaritum.api.animus.IFunctionalSoulstone;
import eladkay.quaritum.common.core.ItemNBTHelper;
import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNBT;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemPassionateSoulstone extends ItemMod implements IFunctionalSoulstone, IFuelHandler {

    public ItemPassionateSoulstone() {
        super(LibNames.PASSIONATE_SOULSTONE);
        setMaxStackSize(1);
        GameRegistry.registerFuelHandler(this);
    }

    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
        tooltipIfShift(list, () ->
                list.add("Animus: " + getAnimusLevel(itemStack)));
    }

    @Override
    public Item getContainerItem() {
        return ModItems.passionate;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        addAnimus(itemStack, -4);
        ItemStack copiedStack = itemStack.copy();
        copiedStack.setItemDamage(copiedStack.getItemDamage());
        copiedStack.stackSize = 1;
        return copiedStack;
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack) {
        return true;
    }
    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
    }

    @Override
    public int getAnimusLevel(ItemStack stack) {
        return ItemNBTHelper.getInt(stack, LibNBT.TAG_ANIMUS, 0);
    }


    @Override
    public ItemStack addAnimus(ItemStack stack, int amount) {
        if (getAnimusLevel(stack) + amount > getMaxAnimus(stack)) return stack;
        ItemNBTHelper.setInt(stack, LibNBT.TAG_ANIMUS, getAnimusLevel(stack) + amount);
        return stack;
    }

    @Nullable
    @Override
    public int getMaxAnimus(@Nonnull ItemStack stack) {
        return 800;
    }

    @Nullable
    @Override
    public boolean isRechargeable(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public void doPassive(ItemStack stack) {
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        itemStackIn = addAnimus(itemStackIn, 200);
        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    public int getBurnTime(ItemStack fuel) {
        if (getAnimusLevel(fuel) >= 4) {
            return 200;
        }
        return 0;

    }
}

