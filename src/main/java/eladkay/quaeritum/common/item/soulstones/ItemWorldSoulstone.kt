package eladkay.quaeritum.common.item.soulstones

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.api.animus.ISoulstone
import eladkay.quaeritum.api.lib.LibNBT
import eladkay.quaeritum.client.core.ClientUtils
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class ItemWorldSoulstone : ItemMod(LibNames.STONE_OF_THE_WORLDSOUL), ISoulstone {

    init {
        setMaxStackSize(1)
    }

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity?, itemSlot: Int, isSelected: Boolean) {
        if (worldIn.totalWorldTime % 60 == 0L)
            AnimusHelper.addAnimus(stack, 1)
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
        return 800
    }

}
