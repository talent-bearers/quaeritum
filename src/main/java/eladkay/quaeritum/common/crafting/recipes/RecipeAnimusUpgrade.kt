package eladkay.quaeritum.common.crafting.recipes

import com.google.common.collect.Lists
import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.api.animus.EnumAnimusTier
import eladkay.quaeritum.api.animus.ISoulstone
import eladkay.quaeritum.common.item.ModItems
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.ShapedOreRecipe

class RecipeAnimusUpgrade(output: ItemStack, vararg inputs: Any) : ShapedOreRecipe(output, inputs) {

    override fun getCraftingResult(var1: InventoryCrafting): ItemStack? {
        return output(output, var1)
    }

    companion object {

        fun output(output: ItemStack, inv: InventoryCrafting): ItemStack {
            val use = Lists.newArrayList<ItemStack>()
            for (i in 0..inv.sizeInventory - 1) use.add(inv.getStackInSlot(i))
            return output(output, use)
        }

        fun output(list: List<ItemStack>): ItemStack {
            return output(ItemStack(ModItems.awakened), list)
        }

        fun output(output: ItemStack, list: List<ItemStack>): ItemStack {
            val out = output.copy()

            var rarity = Integer.MAX_VALUE

            if (out.item !is ISoulstone)
                return out

            val outItem = out.item as ISoulstone
            list.filter({ stack -> stack.item is ISoulstone }).forEach({ stack ->
                AnimusHelper.addAnimus(out, (stack.item as ISoulstone).getAnimusLevel(stack))
                rarity = Math.min(rarity, outItem.getAnimusTier(stack).ordinal)
            })

            AnimusHelper.setTier(out, EnumAnimusTier.fromMeta(rarity))
            return out
        }
    }
}
