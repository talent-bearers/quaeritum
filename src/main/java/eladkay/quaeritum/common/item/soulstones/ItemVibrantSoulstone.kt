package eladkay.quaeritum.common.item.soulstones

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.api.animus.EnumAnimusTier
import eladkay.quaeritum.api.animus.INetworkProvider
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.BlockDispenser
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.dispenser.IBlockSource
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Bootstrap
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemDye
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * @author WireSegal
 * Created at 10:39 AM on 12/25/17.
 */
class ItemVibrantSoulstone : ItemMod(LibNames.VIBRANT_SOULSTONE), INetworkProvider {

    init {
        setMaxStackSize(1)
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, object : Bootstrap.BehaviorDispenseOptional() {
            override fun dispenseStack(source: IBlockSource, stack: ItemStack): ItemStack {
                this.successful = true
                val world = source.world
                val pos = source.blockPos.offset(source.blockState.getValue(BlockDispenser.FACING))
                val oriCount = stack.count
                if (ItemDye.applyBonemeal(stack, world, pos)) {
                    if (!world.isRemote) world.playEvent(2005, pos, 0)
                } else this.successful = false
                stack.count = oriCount

                return stack
            }
        })
    }

    @SideOnly(Side.CLIENT)
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<String>, advanced: ITooltipFlag) {
        AnimusHelper.Network.addInformation(stack, tooltip, advanced.isAdvanced)
    }

    override fun isProvider(stack: ItemStack): Boolean {
        return false
    }

    override fun isReceiver(stack: ItemStack): Boolean {
        return false
    }

    override fun getEntityLifespan(itemStack: ItemStack?, world: World?): Int {
        return Integer.MAX_VALUE
    }

    override fun onUpdate(stack: ItemStack, worldIn: World?, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
        if (getPlayer(stack) == null && entityIn is EntityPlayer)
            setPlayer(stack, entityIn.uniqueID)
    }

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val itemStackIn = playerIn.getHeldItem(hand)

        if (playerIn.isSneaking) {
            setPlayer(itemStackIn, playerIn.uniqueID)
            worldIn.playSound(playerIn, playerIn.position, SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYERS, 1f, 1f)
            return ActionResult(EnumActionResult.SUCCESS, itemStackIn)
        }
        return super.onItemRightClick(worldIn, playerIn, hand)
    }

    override fun onItemUse(player: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        val stack = player.getHeldItem(hand)
        val uuid = getPlayer(stack)
        val oriCount = stack.count
        if (AnimusHelper.Network.requestAnimus(uuid, 4, EnumAnimusTier.VERDIS, false) &&
                ItemDye.applyBonemeal(stack, worldIn, pos, player, hand)) {
            AnimusHelper.Network.requestAnimus(uuid, 4, EnumAnimusTier.VERDIS, true)
            if (!worldIn.isRemote)
                worldIn.playEvent(2005, pos, 0)
            stack.count = oriCount
            return EnumActionResult.SUCCESS
        }
        return EnumActionResult.PASS
    }
}
