package eladkay.quaeritum.common.item.base

import baubles.api.BaublesApi
import com.teamwizardry.librarianlib.common.base.item.IItemColorProvider
import eladkay.quaeritum.api.spell.ISpellProvider
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * @author WireSegal
 * Created at 4:37 PM on 11/5/16.
 */
abstract class ItemSpellBauble(name: String, vararg variants: String) : ItemModBauble(name, *variants), ISpellProvider, IItemColorProvider {
    override fun canUnequip(stack: ItemStack, player: EntityLivingBase): Boolean {
        return ISpellProvider.Helper.getCooldown(stack) == 0
    }

    override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
        super<ItemModBauble>.onWornTick(stack, player)
        if (player is EntityPlayer) {
            val baubles = BaublesApi.getBaublesHandler(player)
            var slot = 0
            for (i in 0 until 7) if (baubles.getStackInSlot(i) === stack) {
                slot = i
                break
            }
            ISpellProvider.Helper.tickCooldown(player, stack, slot)
        }
    }

    override fun onUpdate(stack: ItemStack, worldIn: World?, entityIn: Entity?, itemSlot: Int, isSelected: Boolean) {
        ISpellProvider.Helper.setCooldown(stack, 0)
    }

    override fun onEntityItemUpdate(entityItem: EntityItem): Boolean {
        ISpellProvider.Helper.setCooldown(entityItem.entityItem, 0)
        return super.onEntityItemUpdate(entityItem)
    }

    @SideOnly(Side.CLIENT)
    override fun getItemColor(): IItemColor? {
        return IItemColor { itemStack, i ->
            if (ISpellProvider.Helper.getCooldown(itemStack) != 0) 0xFF4040 else 0xFFFFFF
        }
    }
}
