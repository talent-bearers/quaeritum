package eladkay.quaeritum.common.block.flowers

import com.teamwizardry.librarianlib.features.base.block.BlockModBush
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import java.util.*

class BlockFiresoulFlower : BlockModBush(LibNames.FLOWER + "_firesoul", Material.PLANTS) {
    init {
        setLightLevel(1f)
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos) = super.getBoundingBox(state, source, pos).offset(state.getOffset(source, pos))

    override fun getOffsetType() = Block.EnumOffsetType.XZ

    override fun randomDisplayTick(stateIn: IBlockState, worldIn: World, pos: BlockPos, rand: Random) {
        val aabb = getBoundingBox(stateIn, worldIn, pos)
        val d0 = aabb.center.x + pos.x
        val d1 = aabb.maxY + pos.y
        val d2 = aabb.center.z + pos.z

        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0, 0.0, 0.0)
        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0, 0.0, 0.0)
    }

}
