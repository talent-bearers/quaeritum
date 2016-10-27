package eladkay.quaeritum.common.crafting.recipes

import eladkay.quaeritum.api.animus.IFlower
import eladkay.quaeritum.common.item.soulstones.ItemAwakenedSoulstone
import eladkay.quaeritum.common.item.soulstones.ItemDormantSoulstone
import net.minecraft.block.Block
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks

class RecipeAwakenedSoulstone : IRecipe {
    override fun matches(inventoryCrafting: InventoryCrafting, world: World): Boolean {
        var foundSoulstone = false
        for (index in 0..inventoryCrafting.sizeInventory - 1) {
            val stack = inventoryCrafting.getStackInSlot(index)
            if (stack != null && stack.item is ItemDormantSoulstone) foundSoulstone = true
        }
        return foundSoulstone
    }

    override fun getCraftingResult(inventoryCrafting: InventoryCrafting): ItemStack? {
        var animus = 0
        var rarity = Integer.MAX_VALUE
        for (index in 0..inventoryCrafting.sizeInventory - 1) {
            val stack = inventoryCrafting.getStackInSlot(index)
            if (stack != null && Block.getBlockFromItem(stack.item) is IFlower) {
                animus += (Block.getBlockFromItem(stack.item) as IFlower).getAnimusFromStack(stack)
                rarity = Math.min((Block.getBlockFromItem(stack.item) as IFlower).getRarity(stack), rarity)
            }

        }
        return ItemAwakenedSoulstone.withAnimus(animus, rarity)
    }

    override fun getRecipeSize(): Int {
        return 10
    }

    override fun getRecipeOutput(): ItemStack? {
        return null
    }

    override fun getRemainingItems(inventoryCrafting: InventoryCrafting): Array<ItemStack> {
        return ForgeHooks.defaultRecipeGetRemainingItems(inventoryCrafting)
    }
}
