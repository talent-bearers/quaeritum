package eladkay.quaeritum.common.item.base

import baubles.api.BaublesApi
import baubles.api.IBauble
import com.teamwizardry.librarianlib.client.util.TooltipHelper
import com.teamwizardry.librarianlib.common.base.item.ItemMod
import com.teamwizardry.librarianlib.common.util.ItemNBTHelper
import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.common.core.QuaeritumSoundEvents
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.world.World

/**
 * @author WireSegal
 * Created at 4:37 PM on 11/5/16.
 */
abstract class ItemModBauble(name: String, vararg variants: String) : ItemMod(name, *variants), IBauble {
    companion object {
        val TAG_HASHCODE = "playerHashcode"

        fun getLastPlayerHashcode(stack: ItemStack) = ItemNBTHelper.getInt(stack, TAG_HASHCODE, 0)
        fun setLastPlayerHashcode(stack: ItemStack, hash: Int) = ItemNBTHelper.setInt(stack, TAG_HASHCODE, hash)
    }

    init {
        maxStackSize = 1
    }

    override fun onItemRightClick(par1ItemStack: ItemStack, par2World: World, par3EntityPlayer: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        if (canEquip(par1ItemStack, par3EntityPlayer)) {
            val baubles = BaublesApi.getBaublesHandler(par3EntityPlayer) ?: return ActionResult(EnumActionResult.FAIL, par1ItemStack)
            for (i in 0..baubles.slots - 1) {
                if (baubles.isItemValidForSlot(i, par1ItemStack, par3EntityPlayer)) {
                    val stackInSlot = baubles.getStackInSlot(i)
                    if (stackInSlot == null || (stackInSlot.item as IBauble).canUnequip(stackInSlot, par3EntityPlayer)) {
                        if (!par2World.isRemote) {
                            baubles.setStackInSlot(i, par1ItemStack.copy())
                            if (!par3EntityPlayer.capabilities.isCreativeMode)
                                par3EntityPlayer.inventory.setInventorySlotContents(par3EntityPlayer.inventory.currentItem, null)
                        }

                        if (stackInSlot != null) {
                            (stackInSlot.item as IBauble).onUnequipped(stackInSlot, par3EntityPlayer)
                            return ActionResult.newResult(EnumActionResult.SUCCESS, stackInSlot.copy())
                        }
                        break
                    }
                }
            }
        }

        return ActionResult.newResult(EnumActionResult.PASS, par1ItemStack)
    }

    open fun onEquippedOrLoadedIntoWorld(stack: ItemStack, player: EntityLivingBase) {
        //NO-OP
    }

    override fun onEquipped(stack: ItemStack, player: EntityLivingBase?) {
        if (player != null) {
            if (!player.worldObj.isRemote)
                player.worldObj.playSound(null, player.posX, player.posY, player.posZ, QuaeritumSoundEvents.baubleEquip, SoundCategory.PLAYERS, 0.1F, 1.3F)


            onEquippedOrLoadedIntoWorld(stack, player)
            setLastPlayerHashcode(stack, player.hashCode())
        }

    }

    override fun onUnequipped(stack: ItemStack, player: EntityLivingBase) {
        //NO-OP
    }

    override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
        if (getLastPlayerHashcode(stack) != player.hashCode()) {
            onEquippedOrLoadedIntoWorld(stack, player)
            setLastPlayerHashcode(stack, player.hashCode())
        }
    }

    override fun addInformation(stack: ItemStack, player: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.tooltipIfShift(tooltip) {
            addHiddenTooltip(stack, player, tooltip, advanced)
        }
    }

    override fun canUnequip(stack: ItemStack, player: EntityLivingBase) = true
    override fun canEquip(stack: ItemStack, player: EntityLivingBase) = true


    open fun addHiddenTooltip(stack: ItemStack, player: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        var key: String? = null
        val keys = Minecraft.getMinecraft().gameSettings.keyBindings
        for (otherKey in keys) if (otherKey.keyDescription == "Baubles Inventory") {
            key = otherKey.displayName
            break
        }

        if (key != null) {
            TooltipHelper.addToTooltip(tooltip, "misc.${LibMisc.MOD_ID}.baubletooltip", key)
        }
    }
}
