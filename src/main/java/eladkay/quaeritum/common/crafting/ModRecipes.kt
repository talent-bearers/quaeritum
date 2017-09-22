package eladkay.quaeritum.common.crafting

import eladkay.quaeritum.api.animus.EnumAnimusTier
import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.api.machines.CentrifugeRecipes
import eladkay.quaeritum.common.block.ModBlocks
import eladkay.quaeritum.common.crafting.recipes.RecipeAwakenedSoulstone
import eladkay.quaeritum.common.item.EvocationRecipe
import eladkay.quaeritum.common.item.ItemEssence
import eladkay.quaeritum.common.item.ItemResource.Resources
import eladkay.quaeritum.common.item.ModItems
import eladkay.quaeritum.common.potions.ModPotionTypes
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.init.PotionTypes
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.item.crafting.IRecipe
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.RecipeSorter
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraftforge.oredict.ShapelessOreRecipe

object ModRecipes {

    private val dyeColors = arrayOf("White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink", "Gray", "LightGray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black")

    fun init() {
        RecipeSorter.register(LibMisc.MOD_ID + ":awakened", RecipeAwakenedSoulstone::class.java, RecipeSorter.Category.SHAPELESS, "")
        RecipeSorter.register(LibMisc.MOD_ID + ":evoker", EvocationRecipe::class.java, RecipeSorter.Category.SHAPELESS, "")


        CraftingManager.getInstance().addRecipe(RecipeAwakenedSoulstone())
        CraftingManager.getInstance().addRecipe(EvocationRecipe)
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
        for (i in dyeColors.indices) {
            addShapelessOreDictRecipe(ItemStack(ModItems.chalk, 1, i),
                    ItemStack(Items.CLAY_BALL),
                    "dye" + dyeColors[i])
        }

        CentrifugeRecipes.registerRecipe("stone", Items.CLAY_BALL, ItemStack(ModItems.dormant))
        CentrifugeRecipes.registerRecipe(ItemEssence.stackOf(EnumAnimusTier.VERDIS), "ingotGold", Resources.VICTIUM_INGOT.stackOf())
                .setRequiresHeat(true)

        ModPotionTypes.addCompletePotionRecipes(ModPotionTypes.potionPredicate("dyeBlack"), PotionTypes.AWKWARD, ModPotionTypes.BLINDNESS, ModPotionTypes.BLINDNESS_LONG, null)
    }

    private fun addOreDictRecipe(output: ItemStack, vararg recipe: Any): IRecipe {
        val obj = ShapedOreRecipe(output, *recipe)
        GameRegistry.addRecipe(obj)
        return obj
    }

    private fun addShapelessOreDictRecipe(output: ItemStack, vararg recipe: Any): IRecipe {
        val obj = ShapelessOreRecipe(output, *recipe)
        GameRegistry.addRecipe(obj)
        return obj
    }
}
