package eladkay.quaritum.common.item.soulstones;

import eladkay.quaritum.api.animus.IFunctionalSoulstone;
import eladkay.quaritum.common.core.ItemNBTHelper;
import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNBT;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

public class ItemPassionateSoulstone extends ItemMod implements IFuelHandler, IFunctionalSoulstone {

    public ItemPassionateSoulstone() {
        super(LibNames.PASSIONATE_SOULSTONE);
        setMaxStackSize(1);
        GameRegistry.registerFuelHandler(this);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean par4) {
        tooltipIfShift(list, () -> list.add("Animus: " + getAnimusLevel(itemStack)));
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return addAnimus(itemStack, -4);
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack) {
        return false;
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
        return 6400;
    }

    @Override
    public boolean isRechargeable(ItemStack stack) {
        return true;
    }

    @Override
    public void doPassive(ItemStack stack) {
    }

    @Override
    public Item getContainerItem() {
        return ModItems.passionate;
    }

    @Override
    public int getBurnTime(ItemStack fuel) {
        return /*getAnimusLevel(fuel) > 20 && fuel.getItem() instanceof ItemPassionateSoulstone ? 200 : 0*/ getAnimusLevel(fuel) * 10;
    }
}
