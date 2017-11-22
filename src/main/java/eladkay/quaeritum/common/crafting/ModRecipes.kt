package eladkay.quaeritum.common.crafting

import com.teamwizardry.librarianlib.core.common.RecipeGeneratorHandler
import com.teamwizardry.librarianlib.core.common.RegistrationHandler
import com.teamwizardry.librarianlib.features.kotlin.toRl
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

object ModRecipes {

    private val dyeColors = arrayOf("White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink", "Gray", "LightGray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black")

    fun init() {

        RegistrationHandler.register(RecipeAwakenedSoulstone(), "${LibMisc.MOD_ID}:awakened".toRl())
        RegistrationHandler.register(EvocationRecipe, "${LibMisc.MOD_ID}:evoker".toRl())
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

        CentrifugeRecipes.registerRecipe("stone", Items.CLAY_BALL, ItemStack(ModItems.dormant))
        CentrifugeRecipes.registerRecipe(ItemEssence.stackOf(EnumAnimusTier.VERDIS), "ingotGold", Resources.VICTIUM_INGOT.stackOf())
                .setRequiresHeat(true)

        ModPotionTypes.addCompletePotionRecipes(ModPotionTypes.potionPredicate("dyeBlack"), PotionTypes.AWKWARD, ModPotionTypes.BLINDNESS, ModPotionTypes.BLINDNESS_LONG, null)
    }

    private fun addOreDictRecipe(name: String, output: ItemStack, vararg recipe: Any) {
        RecipeGeneratorHandler.addShapedRecipe(name, output, *recipe)
    }

    private fun addShapelessOreDictRecipe(name: String, output: ItemStack, vararg recipe: Any) {
        RecipeGeneratorHandler.addShapelessRecipe(name, output, *recipe)

    }
}
