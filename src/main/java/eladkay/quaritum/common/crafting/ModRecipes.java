package eladkay.quaritum.common.crafting;

import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.crafting.recipes.RecipeAnimusUpgrade;
import eladkay.quaritum.common.crafting.recipes.RecipeAwakenedSoulstone;
import eladkay.quaritum.common.crafting.recipes.RecipeShapelessAnimusUpgrade;
import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.lib.LibMisc;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModRecipes {

    private static String[] dyeColors = new String[] {
            "White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink", "Gray", "LightGray",
            "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black"
    };

    public static void init() {
        RecipeSorter.register(LibMisc.MOD_ID + ":animusUpgrade", RecipeAnimusUpgrade.class, RecipeSorter.Category.SHAPED, "");
        RecipeSorter.register(LibMisc.MOD_ID + ":animusUpgradeShapless", RecipeShapelessAnimusUpgrade.class, RecipeSorter.Category.SHAPELESS, "");
        RecipeSorter.register(LibMisc.MOD_ID + ":awakened", RecipeAwakenedSoulstone.class, RecipeSorter.Category.SHAPELESS, "");

        CraftingManager.getInstance().addRecipe(new RecipeAwakenedSoulstone());
        addShapelessOreDictRecipe(new ItemStack(ModBlocks.crystal), new ItemStack(ModBlocks.flower), new ItemStack(Blocks.GOLD_BLOCK));
        addShapelessOreDictRecipe(new ItemStack(ModBlocks.blueprint), "dyeBlue", new ItemStack(Items.PAPER));
        addShapelessOreDictRecipe(new ItemStack(ModItems.fertilizer, 2), "dyePink", new ItemStack(Items.DYE, 1, 15), new ItemStack(ModItems.altas));
        addAnimusRecipe(new ItemStack(ModItems.passionate),
                "Y Y",
                "XZX",
                "YHY",
                'Y', new ItemStack(Items.BLAZE_POWDER),
                'H', new ItemStack(Items.LAVA_BUCKET),
                'X', new ItemStack(Items.FIRE_CHARGE),
                'Z', new ItemStack(ModItems.awakened));
        addOreDictRecipe(new ItemStack(ModItems.dormant),
                "XYX",
                "XZX",
                "XYX",
                'X', new ItemStack(Blocks.SOUL_SAND),
                'Z', "gemDiamond",
                'Y', "blockGlass");
        for (int i = 0; i < dyeColors.length; i++) {
            addShapelessOreDictRecipe(new ItemStack(ModItems.chalk, 1, i),
                    new ItemStack(Items.CLAY_BALL),
                    "dye" + dyeColors[i]);
        }
    }

    private static IRecipe addOreDictRecipe(ItemStack output, Object... recipe) {
        IRecipe obj = new ShapedOreRecipe(output, recipe);
        GameRegistry.addRecipe(obj);
        return obj;
    }

    private static IRecipe addAnimusRecipe(ItemStack output, Object... recipe) {
        IRecipe obj = new RecipeAnimusUpgrade(output, recipe);
        GameRegistry.addRecipe(obj);
        return obj;
    }

    private static IRecipe addShapelessOreDictRecipe(ItemStack output, Object... recipe) {
        IRecipe obj = new ShapelessOreRecipe(output, recipe);
        GameRegistry.addRecipe(obj);
        return obj;
    }

    private static IRecipe addShapelessAnimusRecipe(ItemStack output, Object... recipe) {
        IRecipe obj = new RecipeShapelessAnimusUpgrade(output, recipe);
        GameRegistry.addRecipe(obj);
        return obj;
    }
}
