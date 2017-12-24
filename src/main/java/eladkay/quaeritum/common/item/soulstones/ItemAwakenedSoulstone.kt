package eladkay.quaeritum.common.item.soulstones

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import eladkay.quaeritum.api.animus.EnumAnimusTier
import eladkay.quaeritum.api.animus.IAnimusResource
import eladkay.quaeritum.api.animus.ISoulstone
import eladkay.quaeritum.api.lib.LibNBT
import eladkay.quaeritum.client.core.ClientUtils
import eladkay.quaeritum.common.item.ModItems
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class ItemAwakenedSoulstone(name: String = LibNames.AWAKENED_SOULSTONE) : ItemMod(name), ISoulstone, IAnimusResource {

    init {
        setMaxStackSize(1)
    }

    val MAX_ANIMUS = 800

    override fun getAnimus(stack: ItemStack) = getAnimusLevel(stack)
    @Suppress("RedundantOverride")
    override fun getAnimusTier(stack: ItemStack) = super.getAnimusTier(stack)

    override fun drainedStack(stack: ItemStack): ItemStack {
        return ItemStack(ModItems.dormant)
    }

    override fun getDurabilityForDisplay(stack: ItemStack): Double {
        return 1 - getAnimusLevel(stack).toDouble() / getMaxAnimus(stack).toDouble()
    }

    override fun showDurabilityBar(stack: ItemStack): Boolean {
        return true
    }

    override fun getSubItems(tab: CreativeTabs, subItems: NonNullList<ItemStack>) {
        if (isInCreativeTab(tab)) {
            for (tier in EnumAnimusTier.values()) {
                val example = ItemStack(this)
                setAnimusTier(example, tier)
                setAnimus(example, getMaxAnimus(example))
                subItems.add(example)
            }
        }
    }

    @SideOnly(Side.CLIENT)
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<String>, advanced: ITooltipFlag) {
        ClientUtils.addInformation(stack, tooltip, advanced.isAdvanced)
    }

    override fun getEntityLifespan(itemStack: ItemStack?, world: World?): Int {
        return Integer.MAX_VALUE
    }

    override fun getAnimusLevel(stack: ItemStack): Int {
        return ItemNBTHelper.getInt(stack, LibNBT.TAG_ANIMUS, 0)
    }

    override fun getMaxAnimus(stack: ItemStack): Int {
        return 8 * (getAnimusTier(stack).awakenedFillPercentage * MAX_ANIMUS).toInt()
    }

    companion object {

        @JvmOverloads
        fun withAnimus(animus: Int, rarity: Int = 0): ItemStack {
            val stack = ItemStack(ModItems.awakened)
            ModItems.awakened.setAnimusTier(stack, EnumAnimusTier.fromMeta(rarity))
            ModItems.awakened.setAnimus(stack, animus)
            return stack
        }
    }
}
