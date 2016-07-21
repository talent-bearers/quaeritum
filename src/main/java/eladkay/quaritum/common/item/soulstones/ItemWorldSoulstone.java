package eladkay.quaritum.common.item.soulstones;

import eladkay.quaritum.api.animus.AnimusHelper;
import eladkay.quaritum.api.animus.ISoulstone;
import eladkay.quaritum.api.lib.LibNBT;
import eladkay.quaritum.api.util.ItemNBTHelper;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
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
    public void doPassive(@Nonnull ItemStack stack) {
        AnimusHelper.addAnimus(stack, 1);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean par4) {
        AnimusHelper.addInformation(itemStack, list, par4);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getAnimusLevel(@Nonnull ItemStack stack) {
        return ItemNBTHelper.getInt(stack, LibNBT.TAG_ANIMUS, 0);
    }

    @Override
    public int getMaxAnimus(@Nonnull ItemStack stack) {
        return 800;
    }

    @Override
    public int getMaxRarity(@Nonnull ItemStack stack) {
        return 20;
    }

    @Override
    public boolean isRechargeable(@Nonnull ItemStack stack) {
        return true;
    }

}
