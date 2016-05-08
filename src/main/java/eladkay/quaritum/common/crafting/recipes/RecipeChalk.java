package eladkay.quaritum.common.crafting.recipes;

import eladkay.quaritum.common.item.ModItems;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class RecipeChalk implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        boolean dye = false;
        boolean clay = false;
        for (int index = 0; index < inventoryCrafting.getSizeInventory(); ++index) {

            ItemStack stack = inventoryCrafting.getStackInSlot(index);
            if (stack == null) continue;
            if (stack.getItem() == Items.clay_ball) {
                if (clay) return false;
                clay = true;
            }
            if (stack.getItem() == Items.dye) {
                if (dye) return false;
                dye = true;
            }
        }
        return clay && dye;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        int dye = 0;
        for (int index = 0; index < inventoryCrafting.getSizeInventory(); ++index) {
            ItemStack stack = inventoryCrafting.getStackInSlot(index);
            if (stack == null) continue;
            if (stack.getItem() == Items.dye) dye = 15 - stack.getItemDamage();
        }
        return new ItemStack(ModItems.chalk, 1, dye);
    }

    @Override
    public int getRecipeSize() {
        return 10;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inventoryCrafting) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inventoryCrafting);
    }
}
