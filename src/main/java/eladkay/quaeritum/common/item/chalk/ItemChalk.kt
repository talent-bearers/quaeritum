package eladkay.quaeritum.common.item.chalk

import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper
import eladkay.quaeritum.api.lib.LibNBT
import eladkay.quaeritum.common.block.ModBlocks
import eladkay.quaeritum.common.lib.LibLocations
import eladkay.quaeritum.common.lib.LibNames
import eladkay.quaeritum.common.lib.arrayOfStrings
import eladkay.quaeritum.common.lib.capitalizeFirst
import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemDye
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class ItemChalk : ItemMod(LibNames.CHALK, *ItemChalk.COLORS), IItemColorProvider {
    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { stack, _ -> if (ItemNBTHelper.getBoolean(stack.copy(), LibNBT.FLAT, false) && stack.itemDamage < 16)
            ItemDye.DYE_COLORS[15 - stack.itemDamage] else 0xFFFFFF }

    val block = ModBlocks.chalk

    init {
        addPropertyOverride(LibLocations.FLAT_CHALK) { stack, world, entityLivingBase -> if (ItemNBTHelper.getBoolean(stack.copy(), LibNBT.FLAT, false)) 1.0f else 0.0f }
        setMaxStackSize(1)
    }

    @SideOnly(Side.CLIENT)
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<String>, advanced: ITooltipFlag) {
        TooltipHelper.addToTooltip(tooltip, getUnlocalizedName(stack) + ".desc")
    }

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

    fun placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, newState: IBlockState): Boolean {
        if (!world.setBlockState(pos, newState, 3)) return false

        val state = world.getBlockState(pos)
        if (state.block === ModBlocks.chalk) {
            ItemBlock.setTileEntityNBT(world, player, pos, stack)
            ModBlocks.chalk.onBlockPlacedBy(world, pos, state, player, stack)
        }

        return true
    }


    companion object {
        private val COLORS: Array<String> = arrayOfStrings(EnumDyeColor.values().size)

        init {
            for (dye in EnumDyeColor.values())
                COLORS[dye.ordinal] = "chalk" + dye.toString().capitalizeFirst().replace("Silver", "LightGray")
        }


        fun byMetadata(meta: Int): EnumDyeColor? {
            if (meta == 16) return null
            return EnumDyeColor.byMetadata(meta)
        }
    }
}
