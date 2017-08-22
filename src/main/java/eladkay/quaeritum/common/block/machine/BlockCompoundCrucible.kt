package eladkay.quaeritum.common.block.machine

import com.teamwizardry.librarianlib.features.base.block.BlockModContainer
import eladkay.quaeritum.common.block.tile.TileCompCruc
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

class BlockCompoundCrucible : BlockModContainer("compound_crucible", Material.IRON) {
    override fun createTileEntity(world: World, state: IBlockState): TileEntity? {
        return TileCompCruc()
    }
}
