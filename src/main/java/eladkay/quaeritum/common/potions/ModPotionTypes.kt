package eladkay.quaeritum.common.potions

import com.teamwizardry.librarianlib.common.util.MethodHandleHelper
import eladkay.quaeritum.api.lib.LibMisc
import net.minecraft.init.Items
import net.minecraft.init.MobEffects
import net.minecraft.init.PotionTypes
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.potion.PotionHelper
import net.minecraft.potion.PotionType
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary
import java.util.function.Predicate

/**
 * @author WireSegal
 * Created at 10:01 PM on 2/6/17.
 */
object ModPotionTypes {
    val predicateRedstone = potionPredicate(ItemStack(Items.REDSTONE))
    val predicateGlowstone = potionPredicate(ItemStack(Items.GLOWSTONE_DUST))

    fun potionPredicate(stack: ItemStack): Predicate<ItemStack> {
        return Predicate { OreDictionary.itemMatches(stack, it!!, false) }
    }

    fun potionPredicate(input: String): Predicate<ItemStack> {
        val ores = OreDictionary.getOres(input)
        return Predicate { ores.any { ore -> OreDictionary.itemMatches(ore, it!!, false) } }
    }

    val potionRegister = MethodHandleHelper.wrapperForStaticMethod(PotionHelper::class.java,
            arrayOf("a", "func_185204_a", "registerPotionTypeConversion"),
            PotionType::class.java, Predicate::class.java, PotionType::class.java)

    fun registerPotionTypeConversion(from: PotionType, predicate: Predicate<ItemStack>, to: PotionType) {
        potionRegister(arrayOf(from, predicate, to))
    }

    fun addCompletePotionRecipes(predicate: Predicate<ItemStack>, fromType: PotionType, normalType: PotionType, longType: PotionType?, strongType: PotionType?) {
        if (fromType == PotionTypes.AWKWARD)
            registerPotionTypeConversion(PotionTypes.WATER, predicate, PotionTypes.MUNDANE)
        registerPotionTypeConversion(fromType, predicate, normalType)
        if (longType != null) registerPotionTypeConversion(normalType, predicateRedstone, longType)
        if (strongType != null) registerPotionTypeConversion(normalType, predicateGlowstone, strongType)
    }

    fun addPotionConversionRecipes(predicate: Predicate<ItemStack>, fromTypeNormal: PotionType, fromTypeLong: PotionType?, fromTypeStrong: PotionType?, normalType: PotionType, longType: PotionType?, strongType: PotionType?) {
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
