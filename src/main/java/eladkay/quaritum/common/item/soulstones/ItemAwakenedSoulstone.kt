package eladkay.quaritum.common.item.soulstones

import com.teamwizardry.librarianlib.common.base.item.ItemMod
import eladkay.quaritum.api.animus.AnimusHelper
import eladkay.quaritum.api.animus.ISoulstone
import eladkay.quaritum.api.lib.LibNBT
import eladkay.quaritum.api.util.ItemNBTHelper
import eladkay.quaritum.client.core.ClientUtils
import eladkay.quaritum.common.item.ModItems
import eladkay.quaritum.common.lib.LibNames
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class ItemAwakenedSoulstone @JvmOverloads constructor(name: String = LibNames.AWAKENED_SOULSTONE) : ItemMod(name), ISoulstone {

    init {
        setMaxStackSize(1)
    }

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
        return 800
    }

    override fun getMaxRarity(stack: ItemStack): Int {
        return 10
    }

    override fun isRechargeable(stack: ItemStack): Boolean {
        return true
    }

    companion object {

        @JvmOverloads fun withAnimus(animus: Int, rarity: Int = 0): ItemStack {
            val stack = ItemStack(ModItems.awakened)
            ModItems.awakened.setAnimus(stack, animus)
            ModItems.awakened.setRarity(stack, rarity)
            return stack
        }
    }
}
