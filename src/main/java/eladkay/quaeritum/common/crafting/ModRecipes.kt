package eladkay.quaeritum.common.crafting

import com.teamwizardry.librarianlib.core.common.RecipeGeneratorHandler
import eladkay.quaeritum.api.alchemy.AlchemicalCompositions
import eladkay.quaeritum.api.alchemy.Dessications
import eladkay.quaeritum.api.animus.EnumAnimusTier
import eladkay.quaeritum.api.machines.CentrifugeRecipes
import eladkay.quaeritum.common.block.ModBlocks
import eladkay.quaeritum.common.fluid.ModFluids
import eladkay.quaeritum.common.item.ItemEssence
import eladkay.quaeritum.common.item.ItemResource.Resources
import eladkay.quaeritum.common.item.ModItems
import eladkay.quaeritum.common.potions.ModPotionTypes
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.init.PotionTypes
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.item.crafting.Ingredient
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.oredict.OreDictionary

object ModRecipes {

    private val dyeColors = arrayOf("White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink", "Gray", "LightGray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black")

    fun init() {
        addShapelessOreDictRecipe("blueprint", ItemStack(ModBlocks.blueprint), "dyeBlue", ItemStack(Items.PAPER), ItemStack(Blocks.STONE_SLAB))
        addShapelessOreDictRecipe("fertilizer", ItemStack(ModItems.fertilizer, 2), "dyePink", ItemStack(Items.DYE, 1, 15), ItemStack(ModItems.altas))
        addOreDictRecipe("passion", ItemStack(ModItems.passionate),
                "Y Y",
                "XZX",
                "YHY",
                'Y', ItemStack(Items.BLAZE_POWDER),
                'H', ItemStack(Items.LAVA_BUCKET),
                'X', ItemStack(Items.FIRE_CHARGE),
                'Z', ItemStack(ModItems.dormant))
        for (i in dyeColors.indices) {
            addShapelessOreDictRecipe("chalk$i", ItemStack(ModItems.chalk, 1, i),
                    ItemStack(Items.CLAY_BALL),
                    "dye" + dyeColors[i])
        }

        addOreDictRecipe("jet", ItemStack(ModBlocks.jet),
                " B ",
                " R ",
                "TRT",
                'R', "dustRedstone",
                'B', ItemStack(Items.BUCKET),
                'T', "bitumen")

        addOreDictRecipe("chamber", ItemStack(ModBlocks.fluidHolder),
                "TRT",
                "G G",
                "TRT",
                'R', "dustRedstone",
                'G', "blockGlass",
                'T', "bitumen")

        addOreDictRecipe("centrifuge", ItemStack(ModBlocks.centrifuge),
                "LGL",
                "R R",
                "LLL",
                'R', "dustRedstone",
                'L', "leather",
                'G', "ingotGold")

        addOreDictRecipe("distill", ItemStack(ModBlocks.spiralDistillate),
                " G ",
                "RGR",
                "TTT",
                'R', "dustRedstone",
                'T', "bitumen",
                'G', "blockGlass")

        addOreDictRecipe("compound", ItemStack(ModBlocks.compoundCrucible),
                "I I",
                "ITI",
                "RIR",
                'R', "dustRedstone",
                'I', "ingotIron",
                'T', "bitumen")

        addOreDictRecipe("desiccator", ItemStack(ModBlocks.desiccator),
                "R R",
                "ITI",
                "RIR",
                'R', "dustRedstone",
                'I', "ingotIron",
                'T', "bitumen")

        CentrifugeRecipes.registerRecipe("stone", Items.CLAY_BALL, ItemStack(ModItems.dormant))
        CentrifugeRecipes.registerRecipe(ItemEssence.stackOf(EnumAnimusTier.VERDIS), "ingotGold", Resources.VICTIUM_INGOT.stackOf())
                .setRequiresHeat(true)
        CentrifugeRecipes.registerRecipe(ItemStack(Items.COAL), null, Resources.BITUMEN.stackOf())
        CentrifugeRecipes.registerRecipe(ItemStack(Blocks.RED_FLOWER), null, ItemEssence.stackOf(EnumAnimusTier.VERDIS))
        CentrifugeRecipes.registerRecipe(Resources.PERFECT_MATRIX.stackOf(), null, ItemEssence.stackOf(EnumAnimusTier.ARGENTUS))

        Dessications.registerRecipe(ModFluids.BITUMEN.getActual(), Resources.BITUMEN.stackOf(2))
        Dessications.registerRecipe(ModFluids.SWEET.getActual(), ItemStack(Items.SUGAR))
        Dessications.registerRecipe(ModFluids.LIGHT.getActual(), ItemStack(Items.GLOWSTONE_DUST))
        Dessications.registerRecipe(ModFluids.SLURRY.getActual(), Resources.SLURRY.stackOf())
        Dessications.registerRecipe(FluidRegistry.LAVA, ItemStack(Blocks.OBSIDIAN))
        Dessications.registerRecipe(FluidRegistry.WATER, ItemStack.EMPTY)

        AlchemicalCompositions.registerRecipe(ModFluids.BITUMEN.getActual(),
                ingredientOf(Resources.BITUMEN.stackOf()),
                ingredientOf(ItemStack(Items.COAL), ItemStack(Items.COAL, 1, 1)))
        AlchemicalCompositions.registerRecipe(ModFluids.SWEET.getActual(),
                Resources.FLOWER_DUST.stackOf(),
                ItemEssence.stackOf(EnumAnimusTier.VERDIS))
        AlchemicalCompositions.registerRecipe(ModFluids.LIGHT.getActual(),
                ItemStack(Items.BLAZE_POWDER),
                ItemEssence.stackOf(EnumAnimusTier.LUCIS))
        AlchemicalCompositions.registerRecipe(ModFluids.LIGHT.getActual(),
                ingredientOf("nuggetIron"),
                ingredientOf(Resources.SLURRY.stackOf()))
        AlchemicalCompositions.registerRecipe(ModFluids.SLURRY.getActual(),
                Resources.MIXTURE_MATRIX.stackOf(),
                ItemEssence.stackOf(EnumAnimusTier.FERRUS))
        AlchemicalCompositions.registerRecipe(ModFluids.BITUMEN.getActual(),
                Resources.ALLOY_MATRIX.stackOf(),
                Resources.RUSTED_MATRIX.stackOf())
        AlchemicalCompositions.registerRecipe(ModFluids.LIGHT.getActual(),
                Resources.RUSTED_MATRIX.stackOf(),
                Resources.PERFECT_MATRIX.stackOf())

        FurnaceRecipes.instance().addSmeltingRecipe(Resources.MIXTURE_MATRIX.stackOf(), Resources.ALLOY_MATRIX.stackOf(), 0.7f)

        ModPotionTypes.addCompletePotionRecipes(ModPotionTypes.potionPredicate("dyeBlack"), PotionTypes.AWKWARD, ModPotionTypes.BLINDNESS, ModPotionTypes.BLINDNESS_LONG, null)
    }

    fun ingredientOf(vararg itemStacks: ItemStack) = ingredientOf(itemStacks)

    fun ingredientOf(any: Any): Ingredient {
        if (any is ItemStack)
            return Ingredient.fromStacks(any)
        else if (any is String)
            return ingredientOf(OreDictionary.getOres(any))
        else if (any is Iterable<*>)
            return Ingredient.fromStacks(*any.filterIsInstance<ItemStack>().toTypedArray())
        else if (any is Array<*>)
            return Ingredient.fromStacks(*any.filterIsInstance<ItemStack>().toTypedArray())
        return Ingredient.EMPTY
    }

    private fun addOreDictRecipe(name: String, output: ItemStack, vararg recipe: Any) {
        RecipeGeneratorHandler.addShapedRecipe(name, output, *recipe)
    }

    private fun addShapelessOreDictRecipe(name: String, output: ItemStack, vararg recipe: Any) {
        RecipeGeneratorHandler.addShapelessRecipe(name, output, *recipe)

    }
}
