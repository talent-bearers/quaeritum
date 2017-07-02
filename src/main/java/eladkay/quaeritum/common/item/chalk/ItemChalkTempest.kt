package eladkay.quaeritum.common.item.chalk

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper
import eladkay.quaeritum.api.lib.LibNBT
import eladkay.quaeritum.common.block.ModBlocks

import eladkay.quaeritum.common.lib.LibLocations
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ItemChalkTempest : ItemMod(LibNames.CHALK_TEMPEST) {
    init {
        addPropertyOverride(LibLocations.FLAT_CHALK) { stack, world, entityLivingBase -> if (ItemNBTHelper.getBoolean(stack.copy(), LibNBT.FLAT, false)) 1.0f else 0.0f }
        setMaxStackSize(1)
    }

    val block = ModBlocks.chalk


    override fun onItemUse(player: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        var pos = pos
        val iblockstate = worldIn.getBlockState(pos)
        val block = iblockstate.block

        if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(facing)
        }

        val itemstack = player.getHeldItem(hand)

        if (!itemstack.isEmpty && player.canPlayerEdit(pos, facing, itemstack) && worldIn.mayPlace(this.block, pos, false, facing, null as Entity?)) {
            val i = this.getMetadata(itemstack.metadata)
            val iblockstate1 = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand)

            if (placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1)) {
                val soundtype = worldIn.getBlockState(pos).block.getSoundType(worldIn.getBlockState(pos), worldIn, pos, player)
                worldIn.playSound(player, pos, soundtype.placeSound, SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f)
            }

            return EnumActionResult.SUCCESS
        } else {
            return EnumActionResult.FAIL
        }
    }

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.addToTooltip(tooltip, getUnlocalizedName(stack) + ".desc")
    }

    private fun placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, newState: IBlockState): Boolean {
        if (!world.setBlockState(pos, newState, 3)) return false

        val state = world.getBlockState(pos)
        if (state.block === ModBlocks.tempest) {
            ItemBlock.setTileEntityNBT(world, player, pos, stack)
            ModBlocks.tempest.onBlockPlacedBy(world, pos, state, player, stack)
        }

        return true
    }
}
