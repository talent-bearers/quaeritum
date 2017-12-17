package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.core.LibrarianLib
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.kotlin.isNotEmpty
import com.teamwizardry.librarianlib.features.kotlin.sendSpamlessMessage
import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.api.contract.ContractRegistry
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import net.minecraftforge.oredict.OreIngredient
import net.minecraftforge.registries.IForgeRegistryEntry

/**
 * @author WireSegal
 * Created at 5:33 PM on 2/26/17.
 */
class ItemContractScroll : ItemMod(LibNames.SCROLL, LibNames.SCROLL, LibNames.SEALED_SCROLL), IItemColorProvider {
    companion object {
        init {
            ContractRegistry.registerOath("ea", 4, { player, stack, world, pos -> })
            ContractRegistry.registerOath("seven", 4, { player, stack, world, pos -> })
        }
    }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { stack, i ->
            if (i == 1)
                AnimusHelper.Network.getAnimusColor(LibrarianLib.PROXY.getClientPlayer()) // Temporary, will be done via animus binding
            else -1
        }

    /*
        May secrets be kept in the shadows of light,
        may truth be found in the apex of night.
        May life flow through all flesh and blood,
        through soul and storm, let our hope be a flood.
     */
    val WORDS_OF_AGES = 0x2F0E38FE

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val itemStackIn = playerIn.getHeldItem(hand)

        if (!playerIn.isSneaking)
            return ActionResult(EnumActionResult.PASS, itemStackIn)

        if (!worldIn.isRemote) {
            val oathIndex = itemStackIn.oath
            var newOathIndex = (oathIndex + 1) % ContractRegistry.getMaxId()
            var newOath = ContractRegistry.getOathFromId(newOathIndex)!!
            while (!newOath.unlocked(playerIn)) {
                newOathIndex = (newOathIndex + 1) % ContractRegistry.getMaxId()
                newOath = ContractRegistry.getOathFromId(newOathIndex)!!
            }
            itemStackIn.oath = newOathIndex
            ItemNBTHelper.setUUID(itemStackIn, "uuid", playerIn.uniqueID)
            val component = TextComponentTranslation(newOath.getUnlocName(itemStackIn)).setStyle(Style().setBold(true))
            for (line in newOath.getUnlocText(itemStackIn)) {
                component.appendSibling(TextComponentString("\n | ").setStyle(Style().setBold(false)))
                component.appendSibling(TextComponentTranslation(line).setStyle(Style().setBold(false)))
            }

            playerIn.sendSpamlessMessage(component, WORDS_OF_AGES)
        }

        return ActionResult(EnumActionResult.SUCCESS, itemStackIn)
    }

}

var ItemStack.oath: Int
    get() = ItemNBTHelper.getInt(this, "oath", -1)
    set(value) = if (value == -1) ItemNBTHelper.removeEntry(this, "oath") else ItemNBTHelper.setInt(this, "oath", value)

object SigningRecipe : IForgeRegistryEntry.Impl<IRecipe>(), IRecipe {

    val dye = OreIngredient("dyeBlack")
    val scroll: Ingredient = Ingredient.fromStacks(ItemStack(ModItems.scroll))
    val feather = OreIngredient("feather")

    override fun canFit(width: Int, height: Int): Boolean {
        return true
    }

    override fun getRecipeOutput(): ItemStack {
        return ItemStack(ModItems.scroll, 1, 1)
    }

    override fun getCraftingResult(inv: InventoryCrafting): ItemStack {
        var foundDye = ItemStack.EMPTY
        var foundScroll = ItemStack.EMPTY
        var foundFeather = ItemStack.EMPTY
        for (i in 0 until inv.sizeInventory) {
            val stack = inv.getStackInSlot(i)
            if (stack.isNotEmpty) {
                if (dye.test(stack)) {
                    if (foundDye.isNotEmpty) return ItemStack.EMPTY
                    foundDye = stack
                } else if (scroll.test(stack)) {
                    if (foundScroll.isNotEmpty) return ItemStack.EMPTY
                    foundScroll = stack
                } else if (feather.test(stack)) {
                    if (foundFeather.isNotEmpty) return ItemStack.EMPTY
                    foundFeather = stack
                } else return ItemStack.EMPTY
            }
        }

        val result = foundScroll.copy()
        result.itemDamage = 1
        return result
    }

    override fun matches(inv: InventoryCrafting, worldIn: World?): Boolean {
        var foundDye = false
        var foundScroll = false
        var foundFeather = false
        for (i in 0 until inv.sizeInventory) {
            val stack = inv.getStackInSlot(i)
            if (stack.isNotEmpty) {
                if (dye.test(stack)) {
                    if (foundDye) return false
                    foundDye = true
                } else if (scroll.test(stack)) {
                    if (foundScroll) return false
                    foundScroll = true
                } else if (feather.test(stack)) {
                    if (foundFeather) return false
                    foundFeather = true
                } else return false
            }
        }
        return foundDye && foundScroll && foundFeather
    }

    override fun getIngredients(): NonNullList<Ingredient> {
        val list: NonNullList<Ingredient> = NonNullList.create()
        list.add(dye)
        list.add(scroll)
        list.add(feather)
        return list
    }
}
