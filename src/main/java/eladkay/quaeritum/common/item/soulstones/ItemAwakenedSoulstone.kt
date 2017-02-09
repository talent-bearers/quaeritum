package eladkay.quaeritum.common.item.soulstones

import com.teamwizardry.librarianlib.common.base.item.ItemMod
import com.teamwizardry.librarianlib.common.util.ItemNBTHelper
import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.api.animus.EnumAnimusTier
import eladkay.quaeritum.api.animus.IAnimusResource
import eladkay.quaeritum.api.animus.ISoulstone
import eladkay.quaeritum.api.lib.LibNBT
import eladkay.quaeritum.client.core.ClientUtils
import eladkay.quaeritum.common.item.ModItems

import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class ItemAwakenedSoulstone @JvmOverloads constructor(name: String = LibNames.AWAKENED_SOULSTONE) : ItemMod(name), ISoulstone, IAnimusResource {

    init {
        setMaxStackSize(1)
    }

    val MAX_ANIMUS = 800

    override fun getAnimus(stack: ItemStack) = getAnimusLevel(stack)
    override fun getAnimusTier(stack: ItemStack) = super.getAnimusTier(stack)

    override fun getDurabilityForDisplay(stack: ItemStack): Double {
        return 1 - getAnimusLevel(stack).toDouble() / getMaxAnimus(stack).toDouble()
    }

    override fun showDurabilityBar(stack: ItemStack): Boolean {
        return true
    }

    override fun getDamage(stack: ItemStack): Int {
        if (super.getDamage(stack) != 0)
            super.setDamage(stack, 0)
        return 0
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
        subItems.add(ItemStack(itemIn))
        val stack2 = ItemStack(itemIn, 1)
        AnimusHelper.setAnimus(stack2, getMaxAnimus(stack2))
        subItems.add(stack2)
    }

    override fun getMaxDamage(stack: ItemStack): Int {
        return getAnimusLevel(stack)
    }

    override fun addInformation(itemStack: ItemStack, player: EntityPlayer?, list: MutableList<String>, par4: Boolean) {
        ClientUtils.addInformation(itemStack, list, par4)
    }

    override fun getEntityLifespan(itemStack: ItemStack?, world: World?): Int {
        return Integer.MAX_VALUE
    }

    override fun getAnimusLevel(stack: ItemStack): Int {
        return ItemNBTHelper.getInt(stack, LibNBT.TAG_ANIMUS, 0)
    }

    override fun getMaxAnimus(stack: ItemStack): Int {
        return MAX_ANIMUS
    }

    companion object {

        @JvmOverloads fun withAnimus(animus: Int, rarity: Int = 0): ItemStack {
            val stack = ItemStack(ModItems.awakened)
            ModItems.awakened.setAnimus(stack, animus)
            ModItems.awakened.setAnimusTier(stack, EnumAnimusTier.fromMeta(rarity))
            return stack
        }
    }
}
