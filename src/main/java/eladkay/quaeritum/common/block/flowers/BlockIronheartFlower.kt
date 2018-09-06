package eladkay.quaeritum.common.block.flowers

import com.teamwizardry.librarianlib.features.base.block.BlockModBush
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

class BlockIronheartFlower : BlockModBush(LibNames.FLOWER + "_ironheart", Material.PLANTS) {
    override fun onEntityCollision(worldIn: World?, pos: BlockPos?, state: IBlockState?, entityIn: Entity) {
        if (entityIn is EntityLivingBase)
            entityIn.attackEntityFrom(DamageSource.CACTUS, 3.0f)
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos) = super.getBoundingBox(state, source, pos).offset(state.getOffset(source, pos))

    override fun getOffsetType() = Block.EnumOffsetType.XZ
}
