package eladkay.quaeritum.common.item.soulstones

import com.teamwizardry.librarianlib.common.base.item.ItemMod
import com.teamwizardry.librarianlib.common.util.ItemNBTHelper
import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.api.animus.ISoulstone
import eladkay.quaeritum.api.lib.LibNBT
import eladkay.quaeritum.client.core.ClientUtils
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class ItemWorldSoulstone : ItemMod(LibNames.STONE_OF_THE_WORLDSOUL), ISoulstone {

    init {
        setMaxStackSize(1)
    }

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity?, itemSlot: Int, isSelected: Boolean) {
        if (worldIn.totalWorldTime % 60 == 0L)
            AnimusHelper.addAnimus(stack, 1)
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

}
