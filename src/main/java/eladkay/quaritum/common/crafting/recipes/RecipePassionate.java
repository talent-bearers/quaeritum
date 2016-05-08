package eladkay.quaritum.common.crafting.recipes;

import eladkay.quaritum.common.core.ItemNBTHelper;
import eladkay.quaritum.common.lib.LibNBT;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static eladkay.quaritum.common.item.ModItems.awakened;
import static eladkay.quaritum.common.item.ModItems.passionate;

public class RecipePassionate extends ShapedOreRecipe {
    public RecipePassionate() {
        super(new ItemStack(passionate), "Y Y", "XZX", "YHY", 'Y', new ItemStack(Items.blaze_powder), 'H', new ItemStack(Items.lava_bucket), 'X', new ItemStack(Items.fire_charge), 'Z', new ItemStack(awakened));
    }

    private Item getItem(InventoryCrafting crafting, int i) {
        return crafting.getStackInSlot(i) == null ? null : crafting.getStackInSlot(i).getItem();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        ItemStack stack = new ItemStack(passionate);
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
