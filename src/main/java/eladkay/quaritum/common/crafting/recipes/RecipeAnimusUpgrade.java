package eladkay.quaritum.common.crafting.recipes;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.animus.AnimusHelper;
import eladkay.quaritum.api.animus.ISoulstone;
import eladkay.quaritum.common.item.ModItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.List;

public class RecipeAnimusUpgrade extends ShapedOreRecipe {

    public RecipeAnimusUpgrade(ItemStack output, Object... inputs) {
        super(output, inputs);
    }

    public static ItemStack output(ItemStack output, InventoryCrafting inv) {
        List<ItemStack> use = Lists.newArrayList();
        for (int i = 0; i < inv.getSizeInventory(); i++) use.add(inv.getStackInSlot(i));
        return output(output, use);
    }

    public static ItemStack output(List<ItemStack> list) {
        return output(new ItemStack(ModItems.awakened), list);
    }

    public static ItemStack output(ItemStack output, List<ItemStack> list) {
        ItemStack out = output.copy();

        MutableInt rarity = new MutableInt(Integer.MAX_VALUE);

        if (!(out.getItem() instanceof ISoulstone))
            return out;

        ISoulstone outItem = (ISoulstone) out.getItem();
        list.stream().filter(stack -> stack != null).filter(stack -> stack.getItem() instanceof ISoulstone).forEach(stack -> {
            ISoulstone item = (ISoulstone) stack.getItem();
            AnimusHelper.addAnimus(out, item.getAnimusLevel(stack));
            rarity.setValue(Math.min(rarity.intValue(), outItem.getRarityLevel(stack)));
        });
        AnimusHelper.minimizeRarity(out, rarity.intValue());
        return out;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1) {
        return output(output, var1);
    }
}
