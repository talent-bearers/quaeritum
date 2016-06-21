package eladkay.quaritum.common.item.soulstones;

import eladkay.quaritum.api.animus.AnimusHelper;
import eladkay.quaritum.api.animus.ISoulstone;
import eladkay.quaritum.common.core.ItemNBTHelper;
import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNBT;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemAwakenedSoulstone extends ItemMod implements ISoulstone {
    public ItemAwakenedSoulstone() {
        this(LibNames.AWAKENED_SOULSTONE);
    }

    public ItemAwakenedSoulstone(String name) {
        super(name);
        setMaxStackSize(1);
    }

    public static ItemStack withAnimus(int animus) {
        ItemStack stack = new ItemStack(ModItems.awakened);
        ItemNBTHelper.setInt(stack, LibNBT.TAG_ANIMUS, animus);
        return stack;
    }

    public static ItemStack withAnimus(int animus, int rarity) {
        ItemStack stack = new ItemStack(ModItems.awakened);
        ModItems.awakened.addAnimus(stack, animus);
        ModItems.awakened.addRarity(stack, rarity);
        return stack;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1 - (double) getAnimusLevel(stack) / (double) getMaxAnimus(stack);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public int getDamage(ItemStack stack) {
        if (super.getDamage(stack) != 0)
            super.setDamage(stack, 0);
        return 0;
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        subItems.add(new ItemStack(itemIn));
        ItemStack stack2 = new ItemStack(itemIn, 1);
        AnimusHelper.setAnimus(getMaxAnimus(stack2), stack2);
        subItems.add(stack2);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return getAnimusLevel(stack);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean par4) {
        AnimusHelper.addInformation(itemStack, list);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getAnimusLevel(@Nullable ItemStack stack) {
        return ItemNBTHelper.getInt(stack, LibNBT.TAG_ANIMUS, 0);
    }

    @Nonnull
    @Override
    public ItemStack addAnimus(@Nonnull ItemStack stack, int amount) {
        ItemNBTHelper.setInt(stack, LibNBT.TAG_ANIMUS,
                Math.min(getMaxAnimus(stack), Math.max(0,
                        ItemNBTHelper.getInt(stack, LibNBT.TAG_ANIMUS, 0) + amount)));
        return stack;
    }

    @Override
    public int getMaxAnimus(@Nonnull ItemStack stack) {
        return 800;
    }

    @Override
    public int getRarityLevel(@Nonnull ItemStack stack) {
        return AnimusHelper.getRarity(stack);
    }

    @Nonnull
    @Override
    public ItemStack addRarity(@Nonnull ItemStack stack, int amount) {
        ItemNBTHelper.setInt(stack, LibNBT.TAG_RARITY,
                Math.min(getMaxRarity(stack), Math.max(0,
                        ItemNBTHelper.getInt(stack, LibNBT.TAG_RARITY, 0) + amount)));
        return stack;
    }

    @Override
    public int getMaxRarity(@Nonnull ItemStack stack) {
        return 100;
    }

    @Override
    public boolean isRechargeable(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public void doPassive(@Nonnull ItemStack stack) {
    }

    public String toString(ItemStack stack) {
        return "awakened: animus=" + getAnimusLevel(stack) + "&rarity=" + getRarityLevel(stack);
    }
}
