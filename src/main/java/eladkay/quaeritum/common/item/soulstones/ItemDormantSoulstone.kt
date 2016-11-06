package eladkay.quaeritum.common.item.soulstones

import eladkay.quaeritum.common.item.ModItems
import eladkay.quaeritum.common.item.base.ItemQuaeritum
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.world.World

class ItemDormantSoulstone : ItemQuaeritum(LibNames.DORMANT_SOULSTONE) {
    init {
        setMaxStackSize(1)
    }

    override fun onItemRightClick(itemStackIn: ItemStack, worldIn: World?, playerIn: EntityPlayer?, hand: EnumHand?): ActionResult<ItemStack> {
        if (playerIn!!.isSneaking) {
            worldIn!!.playSound(playerIn, playerIn.position, SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYERS, 1f, 1f)
            return ActionResult(EnumActionResult.SUCCESS, ItemStack(ModItems.attuned))
        }

        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand)
    }

    override fun getEntityLifespan(itemStack: ItemStack?, world: World?): Int {
        return Integer.MAX_VALUE
    }
}
