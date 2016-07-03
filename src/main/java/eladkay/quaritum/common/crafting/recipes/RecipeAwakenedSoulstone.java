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

import javax.annotation.Nonnull;
import java.util.List;

public class RecipeAwakenedSoulstone implements IRecipe {
    @Override
    public boolean matches(@Nonnull InventoryCrafting inventoryCrafting, @Nonnull World world) {
        boolean foundSoulstone = false;
        for (int index = 0; index < inventoryCrafting.getSizeInventory(); ++index) {
            ItemStack stack = inventoryCrafting.getStackInSlot(index);
            if (stack != null && stack.getItem() instanceof ItemDormantSoulstone) foundSoulstone = true;
        }
        return foundSoulstone;
    }

    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inventoryCrafting) {
        int animus = 0;
        int rarity = Integer.MAX_VALUE;
        for (int index = 0; index < inventoryCrafting.getSizeInventory(); ++index) {
            ItemStack stack = inventoryCrafting.getStackInSlot(index);
            if (stack != null && Block.getBlockFromItem(stack.getItem()) instanceof IFlower) {
                animus += ((IFlower) Block.getBlockFromItem(stack.getItem())).getAnimusFromStack(stack);
                rarity = Math.min(((IFlower) Block.getBlockFromItem(stack.getItem())).getRarity(stack), rarity);
            }

        }
        return ItemAwakenedSoulstone.withAnimus(animus, rarity);
    }

    @Override
    public int getRecipeSize() {
        return 10;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Nonnull
    @Override
    public ItemStack[] getRemainingItems(@Nonnull InventoryCrafting inventoryCrafting) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inventoryCrafting);
    }
}
