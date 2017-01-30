package eladkay.quaeritum.common.item

import baubles.api.BaublesApi
import com.teamwizardry.librarianlib.client.core.ClientTickHandler
import com.teamwizardry.librarianlib.common.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.common.base.item.ItemMod
import com.teamwizardry.librarianlib.common.util.ItemNBTHelper
import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.api.spell.ISpellProvider
import eladkay.quaeritum.client.render.RemainingItemsRenderHandler
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.World
import java.awt.Color

/**
 * @author WireSegal
 * Created at 5:18 PM on 11/5/16.
 */
class ItemSoulEvoker() : ItemMod(LibNames.SOUL_EVOKER), IItemColorProvider {

    /*
        todo:
        scroll support for turning wheel
        showing cooldowns/spells in HUD while equipped
     */

    companion object {
        val TAG_SLOT = "slot"
        val MAX_SLOT = 7
    }

    init {
        addPropertyOverride(ResourceLocation(LibMisc.MOD_ID, "evocation")) { itemStack, world, entityLivingBase ->
            ItemNBTHelper.getInt(itemStack, TAG_SLOT, 0).toFloat()
        }
    }

    private val COLORS_FROM_SLOT = arrayOf(
            Color(0x73CECE), // Amulet
            Color(0x8B9E9B), // Ring 1
            Color(0x8B9E9B), // Ring 2
            Color(0x6B3F2E), // Belt
            Color(0xD36363), // Head
            Color(0x404454), // Body
            Color(0x75AA77)  // Charm
    )

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { itemStack, i ->
            if (i == 1) {
                COLORS_FROM_SLOT[ItemNBTHelper.getInt(itemStack, TAG_SLOT, 0) % COLORS_FROM_SLOT.size].pulseColor().rgb
            } else 0xFFFFFF
        }

    fun Color.pulseColor(variance: Int = 24, pulseSpeed: Float = 0.2f): Color {
        val add = (MathHelper.sin(ClientTickHandler.ticksInGame * pulseSpeed) * variance).toInt()
        val newColor = Color(
                Math.max(Math.min(red + add, 255), 0),
                Math.max(Math.min(green + add, 255), 0),
                Math.max(Math.min(blue + add, 255), 0))
        return newColor
    }

    override fun onItemRightClick(itemStackIn: ItemStack, worldIn: World, playerIn: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val slot = ItemNBTHelper.getInt(itemStackIn, TAG_SLOT, 0)
        val baubles = BaublesApi.getBaublesHandler(playerIn)

        if (playerIn.isSneaking) {
            val newSlot = (slot + 1) % MAX_SLOT
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.PLAYERS, 0.6F, (1.0F + (playerIn.worldObj.rand.nextFloat() - playerIn.worldObj.rand.nextFloat()) * 0.2F) * 0.7F)
            if (!worldIn.isRemote)
                ItemNBTHelper.setInt(itemStackIn, TAG_SLOT, newSlot)

            val stack = baubles.getStackInSlot(newSlot)
            if (stack != null && stack.item is ISpellProvider) {
                //debug
                if (!worldIn.isRemote) playerIn.addChatComponentMessage(stack.textComponent)

                val spell = (stack.item as ISpellProvider).getSpell(stack, slot)
                if (spell != null)
                    RemainingItemsRenderHandler.set(spell.getIconStack(stack, slot), spell.getSpellName(stack, slot))
            }
            //debug
            if (!worldIn.isRemote) playerIn.addChatComponentMessage(TextComponentString("slot $newSlot"))

            return ActionResult(EnumActionResult.SUCCESS, itemStackIn)
        } else {
            val stack = baubles.getStackInSlot(slot)
            if (stack != null && stack.item is ISpellProvider) {
                val spell = (stack.item as ISpellProvider).getSpell(stack, slot)
                if (spell != null) {
                    if (ISpellProvider.Helper.getCooldown(stack) == 0) {
                        val success = spell.onCast(playerIn, stack, slot)
                        if (success) {
                            val cooldown = spell.getCooldown(playerIn, stack, slot)
                            if (cooldown > 0) ISpellProvider.Helper.setCooldown(stack, cooldown)
                            return ActionResult(EnumActionResult.SUCCESS, itemStackIn)
                        }
                    }
                }
            }
        }

        return ActionResult(EnumActionResult.PASS, itemStackIn)
    }

    override fun onItemUse(stack: ItemStack, playerIn: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        return onItemRightClick(stack, worldIn, playerIn, hand).type
    }
}
