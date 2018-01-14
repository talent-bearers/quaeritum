package eladkay.quaeritum.common.crafting.recipes

import com.teamwizardry.librarianlib.features.kotlin.isNotEmpty
import eladkay.quaeritum.api.animus.IAnimusResource
import eladkay.quaeritum.common.item.soulstones.ItemAwakenedSoulstone
import eladkay.quaeritum.common.item.soulstones.ItemDormantSoulstone
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.registries.IForgeRegistryEntry

class RecipeAwakenedSoulstone : IForgeRegistryEntry.Impl<IRecipe>(), IRecipe {
    override fun matches(inventoryCrafting: InventoryCrafting, world: World): Boolean {
        var foundSoulstone = false
        var foundResource = false
        (0 until inventoryCrafting.sizeInventory)
                .map { inventoryCrafting.getStackInSlot(it) }
                .filter { it.isNotEmpty }
                .forEach {
                    if (it.item is ItemDormantSoulstone && !foundSoulstone) foundSoulstone = true
                    else if (it.item is IAnimusResource && (it.item as IAnimusResource).getAnimus(it) > 0) foundResource = true
                    else return false
                }

        return foundSoulstone && foundResource
    }

    override fun getCraftingResult(inventoryCrafting: InventoryCrafting): ItemStack? {
        var animus = 0
        var rarity = Integer.MAX_VALUE
        for (index in 0 until inventoryCrafting.sizeInventory) {
            val stack = inventoryCrafting.getStackInSlot(index)
            if (stack != null && stack.item is IAnimusResource) {
                animus += (stack.item as IAnimusResource).getAnimus(stack)
                rarity = Math.min((stack.item as IAnimusResource).getAnimusTier(stack).ordinal, rarity)
            }
        }
        return ItemAwakenedSoulstone.withAnimus(animus, rarity)
    }

    override fun canFit(width: Int, height: Int): Boolean {
        return true
    }

    override fun getRecipeOutput(): ItemStack {
        return ItemStack.EMPTY
    }

    override fun isDynamic(): Boolean {
        return true
    }

    override fun getRemainingItems(inventoryCrafting: InventoryCrafting): NonNullList<ItemStack> {
        return ForgeHooks.defaultRecipeGetRemainingItems(inventoryCrafting)
    }
}
