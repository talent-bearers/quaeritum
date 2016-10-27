package eladkay.quaeritum.common.crafting

import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.common.block.ModBlocks
import eladkay.quaeritum.common.block.flowers.BlockAnimusFlower
import eladkay.quaeritum.common.crafting.recipes.RecipeAnimusUpgrade
import eladkay.quaeritum.common.crafting.recipes.RecipeAwakenedSoulstone
import eladkay.quaeritum.common.crafting.recipes.RecipeShapelessAnimusUpgrade
import eladkay.quaeritum.common.item.ModItems
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.item.crafting.IRecipe
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.RecipeSorter
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraftforge.oredict.ShapelessOreRecipe
import java.util.*

object ModRecipes {

    private val dyeColors = arrayOf("White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink", "Gray", "LightGray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black")

    fun init() {
        RecipeSorter.register(LibMisc.MOD_ID + ":animusUpgrade", RecipeAnimusUpgrade::class.java, RecipeSorter.Category.SHAPED, "")
        RecipeSorter.register(LibMisc.MOD_ID + ":animusUpgradeShapless", RecipeShapelessAnimusUpgrade::class.java, RecipeSorter.Category.SHAPELESS, "")
        RecipeSorter.register(LibMisc.MOD_ID + ":awakened", RecipeAwakenedSoulstone::class.java, RecipeSorter.Category.SHAPELESS, "")

        CraftingManager.getInstance().addRecipe(RecipeAwakenedSoulstone())
        addShapelessOreDictRecipe(ItemStack(ModBlocks.crystal), ItemStack(ModBlocks.flower), ItemStack(Blocks.GOLD_BLOCK))
        addShapelessOreDictRecipe(ItemStack(ModBlocks.blueprint), "dyeBlue", ItemStack(Items.PAPER), ItemStack(Blocks.STONE_SLAB))
        addShapelessOreDictRecipe(ItemStack(ModItems.fertilizer, 2), "dyePink", ItemStack(Items.DYE, 1, 15), ItemStack(ModItems.altas))
        addOreDictRecipe(ItemStack(ModItems.passionate),
                "Y Y",
                "XZX",
                "YHY",
                'Y', ItemStack(Items.BLAZE_POWDER),
                'H', ItemStack(Items.LAVA_BUCKET),
                'X', ItemStack(Items.FIRE_CHARGE),
                'Z', ItemStack(ModItems.dormant))
        addOreDictRecipe(ItemStack(ModItems.dormant),
                "XYX",
                "XZX",
                "XYX",
                'X', ItemStack(Blocks.SOUL_SAND),
                'Z', "gemDiamond",
                'Y', "blockGlass")
        for (i in dyeColors.indices) {
            addShapelessOreDictRecipe(ItemStack(ModItems.chalk, 1, i),
                    ItemStack(Items.CLAY_BALL),
                    "dye" + dyeColors[i])
        }
        for (i in 1..9) {
            val recipe = Array(i) { ItemStack(ModItems.awakened) }
            addShapelessAnimusRecipe(ItemStack(ModItems.awakened), *recipe)
        }
        addOreDictRecipe(ItemStack(ModItems.worldBlade),
                " FI",
                "CWF",
                "SC ",
                'F', ItemStack(ModItems.riftmakerPart),
                'I', "ingotIron",
                'C', ItemStack(ModItems.riftmakerPart, 1, 2),
                'W', ItemStack(ModItems.passive),
                'S', ItemStack(Items.STICK))
        addShapelessAnimusRecipe(ItemStack(ModItems.passive), ItemStack(ModItems.awakened), ItemStack(ModBlocks.crystal), ItemStack(ModBlocks.flower, 1, BlockAnimusFlower.Variants.ARCANE.ordinal))
    }

    private fun addOreDictRecipe(output: ItemStack, vararg recipe: Any): IRecipe {
        val obj = ShapedOreRecipe(output, *recipe)
        GameRegistry.addRecipe(obj)
        return obj
    }

    private fun addAnimusRecipe(output: ItemStack, vararg recipe: Any): IRecipe {
        val obj = RecipeAnimusUpgrade(output, *recipe)
        GameRegistry.addRecipe(obj)
        return obj
    }

    private fun addShapelessOreDictRecipe(output: ItemStack, vararg recipe: Any): IRecipe {
        val obj = ShapelessOreRecipe(output, *recipe)
        GameRegistry.addRecipe(obj)
        return obj
    }

    private fun addShapelessAnimusRecipe(output: ItemStack, vararg recipe: Any): IRecipe {
        val obj = RecipeShapelessAnimusUpgrade(output, *recipe)
        GameRegistry.addRecipe(obj)
        return obj
    }
}
