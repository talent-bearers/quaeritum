package eladkay.quaeritum.common.item.misc

import eladkay.quaeritum.common.item.base.ItemQuaeritum
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumActionResult.PASS
import net.minecraft.util.EnumActionResult.SUCCESS
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/*
    Most of the code in this class has been taken from Similsax Transtructors
    Its license permits it.

 */
class ItemTransducer : ItemQuaeritum(LibNames.TRANSDUCER) {

    init {
        setMaxStackSize(1)
    }

    @SideOnly(Side.CLIENT)
    override fun isFull3D(): Boolean {
        return true
    }

    override fun onItemUse(stack: ItemStack?, player: EntityPlayer?, world: World?, pos: BlockPos?, hand: EnumHand?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (world!!.isRemote) return SUCCESS
        //check if you can place a block
        //if (getMaxDamage() == 0) return PASS;
        if (!player!!.capabilities.allowEdit) return PASS
        val state = world.getBlockState(pos!!)
        val block = state.block
        if (block.isReplaceable(world, pos)) return PASS
        if (block.hasTileEntity(state)) return PASS
        val blockStack = ItemStack(block, 1, block.damageDropped(state))
        if (!player.capabilities.isCreativeMode && !player.inventory.hasItemStack(blockStack)) return PASS
        return tower(stack, player, block, state, world, pos, getSide(facing!!.index, hitX.toDouble(), hitY.toDouble(), hitZ.toDouble()), blockStack)
    }

    private fun tower(stack: ItemStack?, player: EntityPlayer, block: Block, state: IBlockState, world: World, pos: BlockPos, side: Int, blockStack: ItemStack): EnumActionResult {
        return tower(stack, player, block, state, world, pos, EnumFacing.getFront(side).opposite, blockStack, 24)
    }

    private fun tower(stack: ItemStack?, player: EntityPlayer, block: Block, state: IBlockState, world: World, pos: BlockPos, side: EnumFacing, blockStack: ItemStack, range: Int): EnumActionResult {
        if (range == 0) return PASS
        val otherState = world.getBlockState(pos)
        val otherBlock = otherState.block
        if (block === otherBlock && state.properties == otherState.properties)
            return tower(stack, player, block, state, world, pos.offset(side), side, blockStack, range - 1)
        else if (otherBlock.isReplaceable(world, pos)) {
            if (!world.canBlockBePlaced(block, pos, false, side.opposite, null, blockStack)) return PASS
            stack?.damageItem(1, player)
            if (stack?.stackSize == 0)
                world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT,
                        SoundCategory.PLAYERS, 1f, 1f)
            if (!player.capabilities.isCreativeMode) {
                for (i in player.inventory.mainInventory.indices) {
                    val localStack = player.inventory.getStackInSlot(i) ?: continue
                    if (localStack.isItemEqual(blockStack)) {
                        player.inventory.decrStackSize(i, 1)
                        player.openContainer.detectAndSendChanges()
                        break
                    }
                }
            }
            world.setBlockState(pos, state)
            world.playSound(null, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, block.soundType.placeSound,
                    SoundCategory.BLOCKS, (block.soundType.volume + 1.0f) / 2.0f, block.soundType.pitch * 0.8f)
            return SUCCESS
        } else
            return PASS
    }

    companion object {
        private val sidesXY = intArrayOf(4, 5, 0, 1)
        private val sidesYZ = intArrayOf(0, 1, 2, 3)
        private val sidesZX = intArrayOf(2, 3, 4, 5)

        private fun getSide(side: Int, xIn: Double, yIn: Double, zIn: Double): Int {
            //if the middle was clicked, place on the opposite side
            val lo = .25f
            val hi = .75f
            var centeredSides = 0
            if (side != 0 && side != 1)
                centeredSides += if (yIn > lo && yIn < hi) 1 else 0
            if (side != 2 && side != 3)
                centeredSides += if (zIn > lo && zIn < hi) 1 else 0
            if (side != 4 && side != 5)
                centeredSides += if (xIn > lo && xIn < hi) 1 else 0
            if (centeredSides == 2)
                return side

            //otherwise, place on the nearest side
            val left: Double
            val right: Double
            val sides: IntArray
            when (side) {
                0, 1 -> {
                    left = zIn
                    right = xIn
                    sides = sidesZX
                }
                2, 3 -> {
                    left = xIn
                    right = yIn
                    sides = sidesXY
                }
                4, 5 -> {
                    left = yIn
                    right = zIn
                    sides = sidesYZ
                }
                else -> return -1
            }
            val b0 = left > right
            val b1 = left > 1 - right
            if (b0 && b1)
                return sides[0]
            else if (!b0 && !b1)
                return sides[1]
            else if (b1)
                return sides[2]
            else
                return sides[3]
        }
    }


}
