package eladkay.quaeritum.common.potions

import eladkay.quaeritum.api.lib.LibMisc
import net.minecraft.init.Items
import net.minecraft.init.MobEffects
import net.minecraft.init.PotionTypes
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.potion.PotionHelper.registerPotionTypeConversion
import net.minecraft.potion.PotionType
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary

/**
 * @author WireSegal
 * Created at 10:01 PM on 2/6/17.
 */
object ModPotionTypes {
    val predicateRedstone = potionPredicate(ItemStack(Items.REDSTONE))
    val predicateGlowstone = potionPredicate(ItemStack(Items.GLOWSTONE_DUST))

    fun potionPredicate(stack: ItemStack): (ItemStack?) -> Boolean {
        return { OreDictionary.itemMatches(stack, it!!, false) }
    }

    fun potionPredicate(input: String): (ItemStack?) -> Boolean {
        val ores = OreDictionary.getOres(input)
        return { ores.any { ore -> OreDictionary.itemMatches(ore, it!!, false) } }
    }

    fun addCompletePotionRecipes(predicate: (ItemStack?) -> Boolean, fromType: PotionType, normalType: PotionType, longType: PotionType?, strongType: PotionType?) {
        if (fromType == PotionTypes.AWKWARD)
            registerPotionTypeConversion(PotionTypes.WATER, predicate, PotionTypes.MUNDANE)
        registerPotionTypeConversion(fromType, predicate, normalType)
        if (longType != null) registerPotionTypeConversion(normalType, predicateRedstone, longType)
        if (strongType != null) registerPotionTypeConversion(normalType, predicateGlowstone, strongType)
    }

    fun addPotionConversionRecipes(predicate: (ItemStack?) -> Boolean, fromTypeNormal: PotionType, fromTypeLong: PotionType?, fromTypeStrong: PotionType?, normalType: PotionType, longType: PotionType?, strongType: PotionType?) {
        addCompletePotionRecipes(predicate, fromTypeNormal, normalType, longType, strongType)
        if (longType != null && fromTypeLong != null)
            registerPotionTypeConversion(fromTypeLong, predicate, longType)
        if (strongType != null && fromTypeStrong != null)
            registerPotionTypeConversion(fromTypeStrong, predicate, strongType)
    }

    private class PotionTypeMod(name: String, vararg potionEffects: PotionEffect) : PotionType(name, *potionEffects) {
        init {
            GameRegistry.register(this, ResourceLocation(LibMisc.MOD_ID, name))
        }
    }

    val BLINDNESS: PotionType = PotionTypeMod("quaeritum_blindness", PotionEffect(MobEffects.BLINDNESS, 900))
    val BLINDNESS_LONG: PotionType = PotionTypeMod("quaeritum_blindness_long", PotionEffect(MobEffects.BLINDNESS, 1800))
}
