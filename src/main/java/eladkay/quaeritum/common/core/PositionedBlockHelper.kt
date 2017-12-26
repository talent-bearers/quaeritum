package eladkay.quaeritum.common.core

import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.common.block.ModBlocks
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.IBlockState
import net.minecraft.item.EnumDyeColor
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i

object PositionedBlockHelper {

    fun positionedBlockWith(pos: BlockPos, chalkColor: EnumDyeColor): PositionedBlock {
        return PositionedBlock.constructPosChalk(chalkColor, pos)
    }

    fun transform(list: MutableList<PositionedBlock>, facing: EnumFacing, mirrorAlongX: Int) {
        if (facing == EnumFacing.NORTH && mirrorAlongX == 1) return
        for (i in list.indices)
            list[i] = list[i].transform(facing, mirrorAlongX)
    }

    fun getChalkPriority(list: List<PositionedBlock>, entity: TileEntity): Int {
        val world = entity.world
        var chalks = 0
        for (block in list) {
            val state = world.getBlockState(entity.pos.add(block.pos))
            val comparables = block.comparables

            if (block.state.block == state.block) {
                when {
                    block.state.block == ModBlocks.tempest -> chalks++
                    comparables != null -> {
                        if (comparables.any { !castCheckValue(it, block.state, state) })
                            return -1
                        chalks++
                    }
                    block.state !== state -> return -1
                    else -> chalks++
                }
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
            minX = Math.min(block.pos.x, minX)
            maxX = Math.max(block.pos.x, maxX)

            minY = Math.min(block.pos.y, minY)
            maxY = Math.max(block.pos.y, maxY)

            minZ = Math.min(block.pos.z, minZ)
            maxZ = Math.max(block.pos.z, maxZ)
        }
        return Vec3i(maxX - minX, maxY - minY, maxZ - minZ)
    }

}
