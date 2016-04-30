package eladkay.quaritum.common.item.soulstones;

import eladkay.quaritum.api.animus.ISoulstone;
import eladkay.quaritum.client.core.TooltipHelper;
import eladkay.quaritum.common.core.ItemNBTHelper;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNBT;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public class ItemWorldSoulstone extends ItemMod implements ISoulstone {

    public ItemWorldSoulstone() {
        super(LibNames.STONE_OF_THE_WORLDSOUL);
        setMaxStackSize(1);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (worldIn.getTotalWorldTime() % 60 == 0)
            doPassive(stack);
    }

    @Override
    public void doPassive(ItemStack stack) {
        addAnimus(stack, 1);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean par4) {
        tooltipIfShift(list, () ->
                list.add("Animus: " + getAnimusLevel(itemStack)));
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }


    @Override
    public int getAnimusLevel(ItemStack stack) {
        return ItemNBTHelper.getInt(stack, LibNBT.TAG_ANIMUS, 0);
    }

    @Override
    public ItemStack addAnimus(ItemStack stack, int amount) {
        ItemNBTHelper.setInt(stack, LibNBT.TAG_ANIMUS,
                Math.min(getMaxAnimus(stack), Math.max(0,
                        ItemNBTHelper.getInt(stack, LibNBT.TAG_ANIMUS, 0) + amount)));
        return stack;
    }

    @Override
    public int getMaxAnimus(ItemStack stack) {
        return 800;
    }

    @Override
    public boolean isRechargeable(ItemStack stack) {
        return true;
    }

}
