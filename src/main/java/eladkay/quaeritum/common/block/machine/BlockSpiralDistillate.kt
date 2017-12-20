package eladkay.quaeritum.common.block.machine

import com.teamwizardry.librarianlib.features.base.block.tile.BlockModContainer
import eladkay.quaeritum.common.block.tile.TileSpiralDistillate
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

class BlockSpiralDistillate : BlockModContainer("spiral_distillate", Material.IRON) {
    override fun createTileEntity(world: World, state: IBlockState): TileEntity? {
        return TileSpiralDistillate()
    }
}
