package eladkay.quaritum.common.item.soulstones;

import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Worldsoul extends Awakened {
    int counter = 0;

    public Worldsoul() {
        super(LibNames.STONE_OF_THE_WORLDSOUL);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        counter++;
        if (counter % 60 == 1) doPassive(stack);
    }

    @Override
    public void doPassive(ItemStack stack) {
        stack = addAnimus(stack, 1);
    }
}
