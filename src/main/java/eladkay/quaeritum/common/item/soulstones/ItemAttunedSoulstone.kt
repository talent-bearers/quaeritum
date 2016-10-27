package eladkay.quaeritum.common.item.soulstones

import com.teamwizardry.librarianlib.common.base.item.ItemMod
import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.api.animus.INetworkProvider
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.world.World

class ItemAttunedSoulstone : ItemMod(LibNames.ATTUNED_SOULSTONE), INetworkProvider {
    init {
        setMaxStackSize(1)
    }

    override fun isProvider(stack: ItemStack): Boolean {
        return true
    }

    override fun isReceiver(stack: ItemStack): Boolean {
        return true
    }

    override fun addInformation(stack: ItemStack?, playerIn: EntityPlayer?, tooltip: List<String>?, advanced: Boolean) {
        AnimusHelper.Network.addInformation(stack, tooltip, advanced)
    }

    override fun getEntityLifespan(itemStack: ItemStack?, world: World?): Int {
        return Integer.MAX_VALUE
    }

    override fun onUpdate(stack: ItemStack?, worldIn: World?, entityIn: Entity?, itemSlot: Int, isSelected: Boolean) {
        if (getPlayer(stack!!) == null && entityIn is EntityPlayer)
            setPlayer(stack, entityIn.uniqueID)
    }

    override fun onItemRightClick(itemStackIn: ItemStack, worldIn: World?, playerIn: EntityPlayer?, hand: EnumHand?): ActionResult<ItemStack> {
        if (playerIn!!.isSneaking) {
            setPlayer(itemStackIn, playerIn.uniqueID)
            worldIn!!.playSound(playerIn, playerIn.position, SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYERS, 1f, 1f)
            return ActionResult(EnumActionResult.SUCCESS, itemStackIn)
        }
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand)
    }
}
