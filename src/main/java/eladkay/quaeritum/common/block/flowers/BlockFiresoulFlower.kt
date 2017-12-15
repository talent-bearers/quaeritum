package eladkay.quaeritum.common.block.flowers

import com.teamwizardry.librarianlib.features.base.block.BlockModBush
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess

class BlockFiresoulFlower : BlockModBush(LibNames.FLOWER + "_firesoul", Material.PLANTS) {
    init {
        setLightLevel(1f)
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos)
            = super.getBoundingBox(state, source, pos).offset(state.getOffset(source, pos))

    override fun getOffsetType() = Block.EnumOffsetType.XZ

}
