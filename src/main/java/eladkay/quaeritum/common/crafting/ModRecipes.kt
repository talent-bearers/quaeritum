package eladkay.quaeritum.common.crafting

import com.teamwizardry.librarianlib.core.common.RecipeGeneratorHandler
import eladkay.quaeritum.api.alchemy.AlchemicalCompositions
import eladkay.quaeritum.api.alchemy.Desiccations
import eladkay.quaeritum.api.animus.EnumAnimusTier
import eladkay.quaeritum.api.machines.CentrifugeRecipes
import eladkay.quaeritum.common.block.ModBlocks
import eladkay.quaeritum.common.fluid.ModFluids
import eladkay.quaeritum.common.item.ItemEssence
import eladkay.quaeritum.common.item.ItemResource.Resources.*
import eladkay.quaeritum.common.item.ModItems
import eladkay.quaeritum.common.potions.ModPotionTypes
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.init.PotionTypes
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.OreIngredient
import java.util.*

object ModRecipes {

    private val dyeColors = arrayOf("White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink", "Gray", "LightGray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black")

    fun init() {
        addShapelessOreDictRecipe("blueprint", ItemStack(ModBlocks.blueprint),
                "dyeBlue", "paper", ItemStack(Blocks.STONE_SLAB), "essenceVerdis")
        addOreDictRecipe("starmap", ItemStack(ModItems.starMap),
                " P ",
                "PVP",
                " P ",
                'P', "paper",
                'V', "essenceVerdis")
        addOreDictRecipe("passion", ItemStack(ModItems.passionate),
                "Y Y",
                "XZX",
                "YHY",
                'Y', ItemStack(Items.BLAZE_POWDER),
                'H', ItemStack(Items.LAVA_BUCKET),
                'X', ItemStack(Items.FIRE_CHARGE),
                'Z', ItemStack(ModItems.dormant))
        addOreDictRecipe("passion_drive", ItemStack(ModItems.passionDrive),
                "YYX",
                "ZHY",
                'Y', ItemStack(Items.BLAZE_POWDER),
                'H', ItemStack(Items.LAVA_BUCKET),
                'X', "essenceLucis",
                'Z', ItemStack(ModItems.dormant))
        addOreDictRecipe("vibrant", ItemStack(ModItems.vibrant),
                "Y Y",
                "XZX",
                "YHY",
                'Y', "essenceVerdis",
                'H', FluidUtil.getFilledBucket(FluidStack(ModFluids.SWEET.getActual(), 1000)),
                'X', ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.dyeDamage),
                'Z', ItemStack(ModItems.dormant))
        addOreDictRecipe("vibrancy_drive", ItemStack(ModItems.vibrancyDrive),
                "YYX",
                "ZHY",
                'Y', "essenceVerdis",
                'H', FluidUtil.getFilledBucket(FluidStack(ModFluids.SWEET.getActual(), 1000)),
                'X', ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.dyeDamage),
                'Z', ItemStack(ModItems.dormant))
        addOreDictRecipe("twinsoul_drive", ItemStack(ModItems.twinDrive),
                "YYX",
                "ZFL",
                "BBE",
                'Y', "essenceVerdis",
                'B', ItemStack(Items.BLAZE_POWDER),
                'L', FluidUtil.getFilledBucket(FluidStack(ModFluids.LIGHT.getActual(), 1000)),
                'X', ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.dyeDamage),
                'E', "essenceLucis",
                'F', "essenceFerrus",
                'Z', ItemStack(ModItems.dormant))
        for (i in dyeColors.indices)
            addShapelessOreDictRecipe("chalk_" + dyeColors[i].toLowerCase(Locale.ROOT), ItemStack(ModBlocks.chalk, 1, i),
                    ItemStack(Items.CLAY_BALL),
                    "dye" + dyeColors[i])

        addShapelessOreDictRecipe("chalk_tempest", ItemStack(ModBlocks.tempest),
                ItemStack(Items.CLAY_BALL), "essenceVerdis", "essenceLucis", "essenceFerrus", "essenceArgentus", "essenceAtlas")

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

        addShapelessOreDictRecipe("ironheart", ItemStack(ModItems.resourceSeed),
                ModBlocks.flower, SLURRY.stackOf())

        addOreDictRecipe("blossom", ItemStack(ModItems.wakingBlossom),
                "OAO",
                "ALA",
                "OAO",
                'O', "dustOpium",
                'A', "essenceArgentus",
                'L', "enderpearl")

        addShapelessOreDictRecipe("scroll", ItemStack(ModItems.scroll),
                "FLI",
                "PPP",
                'F', "feather",
                'L', "essenceLucis",
                'I', "dyeBlack",
                'P', "paper")

        addOreDictRecipe("pipe", ItemStack(ModBlocks.pipe, 16),
                "BBB",
                "RWR",
                "BBB",
                'B', "bitumen",
                'R', "dustRedstone",
                'W', ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE))

        for (i in dyeColors.indices.toList().takeLast(dyeColors.size - 1)) {
            addShapelessOreDictRecipe("pipe_" + dyeColors[i].toLowerCase(Locale.ROOT), ItemStack(ModBlocks.pipe, 1, i),
                    ItemStack(ModBlocks.pipe),
                    "dye" + dyeColors[i])
            addOreDictRecipe("pipe_8x_" + dyeColors[i].toLowerCase(Locale.ROOT), ItemStack(ModBlocks.pipe, 8, i),
                    "PPP",
                    "PDP",
                    "PPP",
                    'P', ItemStack(ModBlocks.pipe),
                    'D', "dye" + dyeColors[i])
        }

        CentrifugeRecipes.registerRecipe("stone", Items.CLAY_BALL, ItemStack(ModItems.dormant))
        CentrifugeRecipes.registerRecipe(ItemStack(ModItems.dormant), "essenceArgentus", ItemStack(ModItems.tempestArc))
        CentrifugeRecipes.registerRecipe(Items.COAL, null, BITUMEN.stackOf())
        CentrifugeRecipes.registerRecipe(ItemStack(Blocks.RED_FLOWER), null, ItemEssence.stackOf(EnumAnimusTier.VERDIS))
        CentrifugeRecipes.registerRecipe(ModBlocks.flower, null, ItemEssence.stackOf(EnumAnimusTier.LUCIS))
        CentrifugeRecipes.registerRecipe(ModBlocks.ironheart, null, ItemEssence.stackOf(EnumAnimusTier.FERRUS))
        CentrifugeRecipes.registerRecipe(PERFECT_MATRIX.oreName, null, ItemEssence.stackOf(EnumAnimusTier.ARGENTUS))
        CentrifugeRecipes.registerRecipe(AWOKEN_BLOSSOM.oreName, null, ItemEssence.stackOf(EnumAnimusTier.ATLAS))

        Desiccations.registerRecipe(ModFluids.BITUMEN.getActual(), BITUMEN.stackOf(2))
        Desiccations.registerRecipe(ModFluids.SWEET.getActual(), ItemStack(Items.SUGAR))
        Desiccations.registerRecipe(ModFluids.LIGHT.getActual(), ItemStack(Items.GLOWSTONE_DUST))
        Desiccations.registerRecipe(ModFluids.SLURRY.getActual(), SLURRY.stackOf())
        Desiccations.registerRecipe(FluidRegistry.LAVA, ItemStack(Blocks.OBSIDIAN))
        val salt = OreDictionary.getOres("dustSalt").firstOrNull() ?: ItemStack.EMPTY
        Desiccations.registerRecipe(FluidRegistry.WATER, salt)

        AlchemicalCompositions.registerRecipe(ModFluids.BITUMEN.getActual(),
                ingredientOf(BITUMEN.stackOf()),
                ingredientOf(ItemStack(Items.COAL), ItemStack(Items.COAL, 1, 1)))
        AlchemicalCompositions.registerRecipe(ModFluids.SWEET.getActual(),
                ItemStack(ModItems.opium),
                ItemEssence.stackOf(EnumAnimusTier.VERDIS))
        AlchemicalCompositions.registerRecipe(ModFluids.LIGHT.getActual(),
                ItemStack(Items.BLAZE_POWDER),
                ItemEssence.stackOf(EnumAnimusTier.LUCIS))
        AlchemicalCompositions.registerRecipe(ModFluids.LIGHT.getActual(),
                ingredientOf("nuggetIron"),
                ingredientOf(SLURRY.stackOf()))
        AlchemicalCompositions.registerRecipe(ModFluids.SLURRY.getActual(),
                MIXTURE_MATRIX.stackOf(),
                ItemEssence.stackOf(EnumAnimusTier.FERRUS))
        AlchemicalCompositions.registerRecipe(ModFluids.BITUMEN.getActual(),
                ALLOY_MATRIX.stackOf(),
                RUSTED_MATRIX.stackOf())
        AlchemicalCompositions.registerRecipe(ModFluids.LIGHT.getActual(),
                RUSTED_MATRIX.stackOf(),
                PERFECT_MATRIX.stackOf())

        if (Loader.isModLoaded("wizardry")) {
            val fairyItem = Item.REGISTRY.getObject(ResourceLocation("wizardry", "fairy_dust"))
            val devilItem = Item.REGISTRY.getObject(ResourceLocation("wizardry", "devil_dust"))

            AlchemicalCompositions.registerRecipe(FluidRegistry.getFluid("mana_fluid"),
                    ingredientOf(devilItem),
                    ingredientOf(fairyItem))

            Desiccations.registerRecipe(FluidRegistry.getFluid("mana_fluid"), ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.dyeDamage))
        }

        FurnaceRecipes.instance().addSmeltingRecipe(MIXTURE_MATRIX.stackOf(), ALLOY_MATRIX.stackOf(), 0.7f)

        ModPotionTypes.addCompletePotionRecipes(ModPotionTypes.potionPredicate("dyeBlack"), PotionTypes.AWKWARD, ModPotionTypes.BLINDNESS, ModPotionTypes.BLINDNESS_LONG, null)
    }

    fun ingredientOf(vararg itemStacks: ItemStack) = ingredientOf(itemStacks)

    fun ingredientOf(any: Any?): Ingredient = when (any) {
        is Item -> Ingredient.fromItem(any)
        is Block -> Ingredient.fromItem(Item.getItemFromBlock(any))
        is ItemStack -> Ingredient.fromStacks(any)
        is String -> OreIngredient(any)
        is Iterable<*> -> Ingredient.fromStacks(*any.filterIsInstance<ItemStack>().toTypedArray())
        is Array<*> -> Ingredient.fromStacks(*any.filterIsInstance<ItemStack>().toTypedArray())
        else -> Ingredient.EMPTY
    }

    private fun addOreDictRecipe(name: String, output: ItemStack, vararg recipe: Any) {
        RecipeGeneratorHandler.addShapedRecipe(name, output, *recipe)
    }

    private fun addShapelessOreDictRecipe(name: String, output: ItemStack, vararg recipe: Any) {
        RecipeGeneratorHandler.addShapelessRecipe(name, output, *recipe)

    }
}
