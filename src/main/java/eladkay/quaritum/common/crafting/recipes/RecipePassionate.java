package eladkay.quaritum.common.crafting.recipes;

import eladkay.quaritum.common.core.ItemNBTHelper;
import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.lib.LibNBT;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class RecipePassionate implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        return check(inventoryCrafting);
    }

    private Item getItem(InventoryCrafting crafting, int i) {
        return crafting.getStackInSlot(i) == null ? null : crafting.getStackInSlot(i).getItem();
    }

    private boolean check(InventoryCrafting crafting) {
        boolean powder = getItem(crafting, 0) == Items.blaze_powder && getItem(crafting, 2) == Items.blaze_powder && getItem(crafting, 6) == Items.blaze_powder && getItem(crafting, 8) == Items.blaze_powder;
        boolean nothing = getItem(crafting, 1) == null;
        boolean firecharge = getItem(crafting, 3) == Items.fire_charge && getItem(crafting, 5) == Items.fire_charge;
        boolean lava = getItem(crafting, 7) == Items.lava_bucket;
        boolean awakened = getItem(crafting, 4) == ModItems.awakened;
        return powder && nothing && firecharge && lava && awakened;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        if (!check(inventoryCrafting)) return null;
        ItemStack stack = new ItemStack(ModItems.passionate);
        ItemNBTHelper.setInt(stack, LibNBT.TAG_ANIMUS, ItemNBTHelper.getInt(inventoryCrafting.getStackInSlot(4), LibNBT.TAG_ANIMUS, 0));
        return stack;
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
