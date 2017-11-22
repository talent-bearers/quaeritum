package eladkay.quaeritum.common.potions

import com.teamwizardry.librarianlib.core.common.RegistrationHandler
import eladkay.quaeritum.api.lib.LibMisc
import net.minecraft.init.Items
import net.minecraft.init.MobEffects
import net.minecraft.init.PotionTypes
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.potion.PotionEffect
import net.minecraft.potion.PotionHelper.addMix
import net.minecraft.potion.PotionType
import net.minecraft.util.ResourceLocation
import net.minecraftforge.oredict.OreDictionary

/**
 * @author WireSegal
 * Created at 10:01 PM on 2/6/17.
 */
object ModPotionTypes {
    val predicateRedstone = potionPredicate(ItemStack(Items.REDSTONE))
    val predicateGlowstone = potionPredicate(ItemStack(Items.GLOWSTONE_DUST))

    fun potionPredicate(stack: ItemStack): Ingredient {
        return Ingredient.fromStacks(stack)
    }

    fun potionPredicate(input: String): Ingredient {
        val ores = OreDictionary.getOres(input)
        return Ingredient.fromStacks(*ores.toTypedArray())
    }

    fun addCompletePotionRecipes(predicate: Ingredient, fromType: PotionType, normalType: PotionType, longType: PotionType?, strongType: PotionType?) {
        if (fromType == PotionTypes.AWKWARD)
            addMix(PotionTypes.WATER, predicate, PotionTypes.MUNDANE)
        addMix(fromType, predicate, normalType)
        if (longType != null) addMix(normalType, predicateRedstone, longType)
        if (strongType != null) addMix(normalType, predicateGlowstone, strongType)
    }

    fun addPotionConversionRecipes(predicate: Ingredient, fromTypeNormal: PotionType, fromTypeLong: PotionType?, fromTypeStrong: PotionType?, normalType: PotionType, longType: PotionType?, strongType: PotionType?) {
        addCompletePotionRecipes(predicate, fromTypeNormal, normalType, longType, strongType)
        if (longType != null && fromTypeLong != null)
            addMix(fromTypeLong, predicate, longType)
        if (strongType != null && fromTypeStrong != null)
            addMix(fromTypeStrong, predicate, strongType)
    }

    private class PotionTypeMod(name: String, vararg potionEffects: PotionEffect) : PotionType(name, *potionEffects) {
        init {
            RegistrationHandler.register(this, ResourceLocation(LibMisc.MOD_ID, name))
        }
    }

    val BLINDNESS: PotionType = PotionTypeMod("quaeritum_blindness", PotionEffect(MobEffects.BLINDNESS, 900))
    val BLINDNESS_LONG: PotionType = PotionTypeMod("quaeritum_blindness_long", PotionEffect(MobEffects.BLINDNESS, 1800))
}
