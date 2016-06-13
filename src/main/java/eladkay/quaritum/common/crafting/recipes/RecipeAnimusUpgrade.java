package eladkay.quaritum.common.crafting.recipes;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.animus.ISoulstone;
import eladkay.quaritum.api.misc.NonPrimInt;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class RecipeAnimusUpgrade extends ShapedOreRecipe {
    public RecipeAnimusUpgrade(ItemStack output, Object... inputs) {
        super(output, inputs);
    }

    public static ItemStack output(ItemStack output, InventoryCrafting var1) {
        ItemStack out = output.copy();
        List<Integer> rarities = Lists.newArrayList();
        if (!(out.getItem() instanceof ISoulstone))
            return out;
        ISoulstone outItem = (ISoulstone) out.getItem();
        for (int i = 0; i < var1.getSizeInventory(); i++) {
            ItemStack stack = var1.getStackInSlot(i);
            if (stack != null) {
                if (stack.getItem() instanceof ISoulstone) {
                    ISoulstone item = (ISoulstone) stack.getItem();
                    outItem.addAnimus(out, item.getAnimusLevel(stack));
                    rarities.add(item.getRarityLevel(stack));
                }
            }
        }
        //outItem.addRarity(out, getSmallestInList(rarities));
        outItem.addRarity(out, getAverageOfList(rarities));
        return out;
    }

    private static int getSmallestInList(List<Integer> l) {
        int ret = Integer.MAX_VALUE;
        for (Integer inte : l)
            if (ret > inte)
                ret = inte;
        return ret;
    }

    private static int getAverageOfList(List<Integer> l) {
        final NonPrimInt i = new NonPrimInt(0);
        l.forEach(i::add);
        return i.value / l.size();
    }


    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1) {
        return output(output, var1);
    }
}
