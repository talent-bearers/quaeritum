package eladkay.quaeritum.common.item.misc

import com.teamwizardry.librarianlib.features.base.item.ItemModSword
import eladkay.quaeritum.api.contract.ContractRegistry
import eladkay.quaeritum.common.item.ModItems
import eladkay.quaeritum.common.item.oath
import eladkay.quaeritum.common.lib.LibMaterials
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

/**
 * @author WireSegal
 * Created at 4:15 PM on 1/10/18.
 */
class ItemOathScar : ItemModSword("oath_scar", LibMaterials.SCAR) {

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        val stackInOtherHand = playerIn.getHeldItem(if (handIn == EnumHand.MAIN_HAND) EnumHand.OFF_HAND else EnumHand.MAIN_HAND)
        if (stackInOtherHand.item == ModItems.scroll && stackInOtherHand.itemDamage == 1 && !worldIn.isRemote) {
            val oath = ContractRegistry.getOathFromId(stackInOtherHand.oath)
            if (oath != null && ContractRegistry.getNameFromOath(oath).toString() == "quaeritum:connection") {
                oath.fireContract(playerIn, stackInOtherHand, worldIn, playerIn.position)
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn)
    }

    override fun getRarity(stack: ItemStack): EnumRarity {
        return EnumRarity.EPIC
    }
}
