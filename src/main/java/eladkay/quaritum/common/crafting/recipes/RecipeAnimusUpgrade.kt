package eladkay.quaritum.common.crafting.recipes

import com.google.common.collect.Lists
import eladkay.quaritum.api.animus.AnimusHelper
import eladkay.quaritum.api.animus.ISoulstone
import eladkay.quaritum.common.item.ModItems
import eladkay.quaritum.common.lib.stream
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.ShapedOreRecipe
import org.apache.commons.lang3.mutable.MutableInt

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

            val rarity = MutableInt(Integer.MAX_VALUE)

            if (out.item !is ISoulstone)
                return out

            val outItem = out.item as ISoulstone
            list.stream().filter({ stack -> stack.item is ISoulstone }).forEach({ stack ->
                AnimusHelper.addAnimus(out, stack.item.getAnimusLevel(stack))
                rarity.setValue(Math.min(rarity.toInt(), outItem.getRarityLevel(stack)))
            })
            AnimusHelper.minimizeRarity(out, rarity.toInt())
            return out
        }
    }
}
