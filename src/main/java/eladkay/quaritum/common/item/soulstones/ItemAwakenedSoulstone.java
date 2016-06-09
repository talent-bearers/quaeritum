package eladkay.quaritum.common.item.soulstones;

import eladkay.quaritum.api.animus.ISoulstone;
import eladkay.quaritum.common.core.ItemNBTHelper;
import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNBT;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
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
    public int getAnimusLevel(@Nonnull ItemStack stack) {
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
    public boolean isRechargeable(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public void doPassive(@Nonnull ItemStack stack) {
    }
}
