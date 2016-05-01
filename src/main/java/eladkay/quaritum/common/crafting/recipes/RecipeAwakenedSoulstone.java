package eladkay.quaritum.common.crafting.recipes;

import eladkay.quaritum.api.animus.IFlower;
import eladkay.quaritum.common.core.ItemNBTHelper;
import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.lib.LibNBT;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class RecipeAwakenedSoulstone implements IRecipe {

    public boolean matches(InventoryCrafting inv, World worldIn) {
        boolean foundSoulstone = false;
        int totalAnimus = 0;

        for (int index = 0; index < inv.getSizeInventory(); ++index) {
            ItemStack stack = inv.getStackInSlot(index);
            if (stack != null) {
                if (stack.getItem() == ModItems.dormant) {
                    if (!foundSoulstone) {
                        foundSoulstone = true;
                    } else {
                        return false;
                    }
                } else if (stack.getItem() instanceof ItemBlock && Block.getBlockFromItem(stack.getItem()) instanceof IFlower) {
                    totalAnimus += ((IFlower) Block.getBlockFromItem(stack.getItem())).getAnimusFromStack(stack);
                } else
                    return false;
            }
        }
        System.out.println(totalAnimus + " A");
        System.out.println(foundSoulstone + " S");
        return foundSoulstone;

    }

    public ItemStack getCraftingResult(InventoryCrafting inv) {
        int lowestRarity = Integer.MAX_VALUE;
        int totalAnimus = 0;

        for (int index = 0; index < inv.getSizeInventory(); ++index) {
            ItemStack stack = inv.getStackInSlot(index);
            if (stack != null && stack.getItem() instanceof IFlower) {
                totalAnimus += ((IFlower) Block.getBlockFromItem(stack.getItem())).getAnimusFromStack(stack);
                lowestRarity = Math.min(((IFlower) Block.getBlockFromItem(stack.getItem())).getRarity(stack), lowestRarity);
            }
        }

        ItemStack ret = new ItemStack(ModItems.awakened);
        ItemNBTHelper.setInt(ret, LibNBT.TAG_ANIMUS, totalAnimus);
        ItemNBTHelper.setInt(ret, LibNBT.TAG_ANIMUS_LEVEL, lowestRarity);
        return ret;
    }

    public int getRecipeSize() {
        return 10;
    }

    public ItemStack getRecipeOutput() {
        return null;
    }

    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }
}
