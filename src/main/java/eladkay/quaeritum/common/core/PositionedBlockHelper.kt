package eladkay.quaeritum.common.core

import com.google.common.collect.Lists
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.common.block.ModBlocks
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.IBlockState
import net.minecraft.item.EnumDyeColor
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

    fun getChalkPriority(list: List<PositionedBlock>, entity: TileEntity): Int {
        val world = entity.world
        var chalks = 0
        for (block in list) {
            val state = world.getBlockState(entity.pos.add(block.getPos()))
            val comparables = block.comparables

            if (block.state.block === state.block) {
                if (block.state.block === ModBlocks.tempest)
                    chalks++
                else if (comparables != null) {
                    for (property in comparables) {
                        if (!castCheckValue(property, block.state, state)) {
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

    @Suppress("UNCHECKED_CAST")
    private fun castCheckValue(property: IProperty<*>, stateOne: IBlockState, stateTwo: IBlockState): Boolean {
        return checkValue(property as IProperty<Comparable<Comparable<*>>>, stateOne, stateTwo)
    }

    private fun <T : Comparable<T>> checkValue(property: IProperty<T>, stateOne: IBlockState, stateTwo: IBlockState): Boolean {
        return stateOne.getValue(property) == stateTwo.getValue(property)
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

}
