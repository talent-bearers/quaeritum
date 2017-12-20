package eladkay.quaeritum.common.entity

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class EntityArrowFire : EntityArrow {

    constructor(worldIn: World) : super(worldIn)

    constructor(world: World, entityChaosborn: EntityChaosborn,
                p_82196_1_: EntityLivingBase, f: Float, g: Float) : super(world, entityChaosborn)

    override fun getArrowStack(): ItemStack {
        return ItemStack(Blocks.AIR)
    }

    override fun onEntityUpdate() {

        super.onEntityUpdate()
        if (this.isBurning) {
            this.world.setBlockState(BlockPos(this.posX, this.posY, this.posZ), Blocks.FIRE.defaultState)
        }
    }


}
