package eladkay.quaritum.common.crafting.recipes;

import eladkay.quaritum.api.animus.ISoulstone;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeAnimusUpgrade extends ShapedOreRecipe {
    public RecipeAnimusUpgrade(ItemStack output, Object... inputs) {
        super(output, inputs);
    }

    public static ItemStack output(ItemStack output, InventoryCrafting var1) {
        ItemStack out = output.copy();
        if (!(out.getItem() instanceof ISoulstone))
            return out;
        ISoulstone outItem = (ISoulstone) out.getItem();
        for (int i = 0; i < var1.getSizeInventory(); i++) {
            ItemStack stack = var1.getStackInSlot(i);
            if (stack != null) {
                if (stack.getItem() instanceof ISoulstone) {
                    ISoulstone item = (ISoulstone) stack.getItem();
                    outItem.addAnimus(out, item.getAnimusLevel(stack));
                    outItem.addRarity(out, item.getRarityLevel(stack));
                }
            }
        }
        return out;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1) {
        return output(output, var1);
    }
}
