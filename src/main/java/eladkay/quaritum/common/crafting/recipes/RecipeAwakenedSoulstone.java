package eladkay.quaritum.common.crafting.recipes;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.animus.IFlower;
import eladkay.quaritum.common.item.soulstones.ItemAwakenedSoulstone;
import eladkay.quaritum.common.item.soulstones.ItemDormantSoulstone;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;

public class RecipeAwakenedSoulstone implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        boolean foundSoulstone = false;
        for (int index = 0; index < inventoryCrafting.getSizeInventory(); ++index) {
            ItemStack stack = inventoryCrafting.getStackInSlot(index);
            if (stack != null && stack.getItem() instanceof ItemDormantSoulstone) foundSoulstone = true;
        }
        return foundSoulstone;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        int animus = 0;
        //int rarity = 0;
        List<Integer> rarities = Lists.newArrayList();
        for (int index = 0; index < inventoryCrafting.getSizeInventory(); ++index) {
            ItemStack stack = inventoryCrafting.getStackInSlot(index);
            if (stack != null && Block.getBlockFromItem(stack.getItem()) instanceof IFlower) {
                animus += ((IFlower) Block.getBlockFromItem(stack.getItem())).getAnimusFromStack(stack);
                rarities.add(((IFlower) Block.getBlockFromItem(stack.getItem())).getRarity(stack));
            }

        }
        return ItemAwakenedSoulstone.withAnimus(animus, RecipeAnimusUpgrade.getSmallestInList(rarities));
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
