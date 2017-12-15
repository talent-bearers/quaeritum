package eladkay.quaeritum.common.core

import com.google.common.collect.Lists
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import eladkay.quaeritum.api.lib.LibNBT
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.common.block.ModBlocks
import eladkay.quaeritum.common.item.ModItems
import net.minecraft.block.properties.IProperty
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import java.util.*

object PositionedBlockHelper {

    fun positionedBlockWith(pos: BlockPos, chalkColor: EnumDyeColor): PositionedBlock {
        return PositionedBlock.constructPosChalk(chalkColor, pos)
    }

    fun sort(list: ArrayList<PositionedBlock>): ArrayList<ArrayList<PositionedBlock>> {
        val ret = Lists.newArrayList<ArrayList<PositionedBlock>>()
        for (i in 0..24) ret.add(Lists.newArrayList<PositionedBlock>())
        for (block in list)
            ret[block.getPos().y].add(PositionedBlock(block.state, BlockPos(block.getPos().x, 0, block.getPos().z), block.comparables))
        return ret
    }

    fun getChalkPriority(list: List<PositionedBlock>, entity: TileEntity, optionalName: String): Int {
        val world = entity.world
        var chalks = 0
        for (block in list) {
            val state = world.getBlockState(entity.pos.add(block.getPos()))
            val comparables = block.comparables

            if (block.state.block === state.block) {
                LogHelper.logDebug("Block check OK for " + optionalName)
                if (block.state.block === ModBlocks.tempest && state.block === ModBlocks.tempest)
                    chalks++
                else if (comparables != null) {
                    for (property in comparables) {
                        if (block.state.getValue(property as IProperty<Comparable<Comparable<*>>>) != state.getValue(property)) {
                            return -1
                        }
                    }
                    chalks++
                } else if (block.state !== state) {
                    return -1
                } else
                    chalks++
            } else {
                return -1
            }
        }

        return chalks
    }

    fun getDimensions(list: List<PositionedBlock>): Vec3i {
        var minX = 0
        var maxX = 0
        var minY = 0
        var maxY = 0
        var minZ = 0
        var maxZ = 0
        for (block in list) {
            minX = Math.min(block.getPos().x, minX)
            maxX = Math.max(block.getPos().x, maxX)

            minY = Math.min(block.getPos().y, minY)
            maxY = Math.max(block.getPos().y, maxY)

            minZ = Math.min(block.getPos().z, minZ)
            maxZ = Math.max(block.getPos().z, maxZ)
        }
        return Vec3i(maxX - minX, maxY - minY, maxZ - minZ)
    }

    @Deprecated("")
    fun getItemStackArrayArrayArrayFromPositionedBlockList(blocks: List<PositionedBlock>): Array<Array<Array<ItemStack?>>> {
        val ret = Array(50) { Array(50) { arrayOfNulls<ItemStack>(50) } }
        for (block in blocks) {
            val stack = ItemStack(ModItems.chalk, 1, block.state.block.getMetaFromState(block.state))
            ret[block.getPos().x + 3][block.getPos().y + 3][block.getPos().z + 3] = stack
        }
        return ret
    }

    fun getStackFromChalk(block: PositionedBlock): ItemStack {
        if (block.state.block === ModBlocks.blueprint) return ItemStack(ModItems.picture, 1, 0)
        return ItemStack(ModItems.chalk, 1, block.state.block.getMetaFromState(block.state))
    }

    fun getStackFromChalk(block: PositionedBlock, flat: Boolean): ItemStack {
        var ret = ItemStack(block.state.block, 1, block.state.block.getMetaFromState(block.state))
        if (block.state.block === ModBlocks.chalk)
            ret = ItemStack(ModItems.chalk, 1, block.state.block.getMetaFromState(block.state))
        else if (block.state.block === ModBlocks.tempest) ret = ItemStack(ModItems.tempest, 1)
        if (flat)
            ItemNBTHelper.setBoolean(ret, LibNBT.FLAT, true)

        return ret
    }

}
