package eladkay.quaritum.common.animus;

import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SoulstoneAwakened extends DormantSoulstone {

    public SoulstoneAwakened() {
        super(LibNames.AWAKENED_SOULSTONE);
        DormantSoulstone.variants.add(this.getUnlocalizedName());
    }


    @Override
    public int getAnimusLevel(ItemStack stack) {
        return stack.getTagCompound().getInteger(TAG_ANIMUS);
    }

    @Override
    public ItemStack deductAnimus(ItemStack stack, int amount) {
        if (stack.getTagCompound().getInteger(TAG_ANIMUS) + amount < 0) return stack;

        NBTTagCompound tag = stack.getTagCompound();
        tag.setInteger(TAG_ANIMUS, tag.getInteger(TAG_ANIMUS) + amount);
        stack.setTagCompound(tag);
        return stack;
    }

    @Override
    public ItemStack addAnimus(ItemStack stack, int amount) {
        if (stack.getTagCompound().getInteger(TAG_ANIMUS) + amount > getMaxAnimus()) return stack;

        NBTTagCompound tag = stack.getTagCompound();
        tag.setInteger(TAG_ANIMUS, tag.getInteger(TAG_ANIMUS) + amount);
        stack.setTagCompound(tag);
        return stack;
    }

    @Override
    public int getMaxAnimus() {
        return 800;
    }

    @Override
    public boolean isRechargeable() {
        return true;
    }

    @Override
    public void doPassive(ItemStack stack) {
    }
}
