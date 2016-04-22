package eladkay.quaritum.common.item.soulstones;

import eladkay.quaritum.api.animus.IFlower;
import eladkay.quaritum.common.core.ItemNBTHelper;
import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class RecipeAwakenedSoulstone implements IRecipe {
    private ItemStack resultItem = null;

    public RecipeAwakenedSoulstone() {
    }

    public boolean matches(InventoryCrafting inv, World worldIn) {
        System.out.println("STEP 1");
        this.resultItem = null;
        int indexOfSoulstone = -1;
        int totalAnimus = 0;

        for (int list1 = 0; list1 < inv.getSizeInventory(); ++list1) {
            System.out.println("STEP 2");
            ItemStack aint = inv.getStackInSlot(list1);
            if (aint != null) {
                if (aint.getItem() == ModItems.awakened) {
                    System.out.println("STEP 3");
                    if (indexOfSoulstone == -1) {
                        indexOfSoulstone = list1;
                        resultItem = inv.getStackInSlot(indexOfSoulstone);
                    } else {
                        System.out.println("FAIL 1");
                        return false;
                    }
                } else if (aint.getItem() instanceof IFlower) {
                    System.out.println("STEP 4");
                    totalAnimus += ((IFlower) aint.getItem()).getAnimusFromStack(aint);
                }/* else {
                    System.out.println("FAIL 2");
                  //  return false;
                }*/
            }
        }

        if (totalAnimus > 0) {
            resultItem = inv.getStackInSlot(indexOfSoulstone);
            int currentAnimus = ItemNBTHelper.getInt(resultItem, LibNames.TAG_ANIMUS, 0);
            ItemNBTHelper.setInt(resultItem, LibNames.TAG_ANIMUS, currentAnimus + totalAnimus);
            System.out.println("SUCESS");
            return true;
        }
        System.out.println("FAIL 3");
        return true;

    }

    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return this.resultItem.copy() == null ? new ItemStack(Items.potato) : this.resultItem.copy();
    }

    public int getRecipeSize() {
        return 10;
    }

    public ItemStack getRecipeOutput() {
        return this.resultItem;
    }

    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];

        for (int i = 0; i < aitemstack.length; ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            aitemstack[i] = ForgeHooks.getContainerItem(itemstack);
        }

        return aitemstack;
    }
}
