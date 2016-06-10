package eladkay.quaritum.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityArrowFire extends EntityArrow {

    public EntityArrowFire(World worldIn) {
        super(worldIn);

    }

    public EntityArrowFire(World worldObj, EntityChaosborn entityChaosborn,
                           EntityLivingBase p_82196_1_, float f, float g) {
        super(worldObj, entityChaosborn);
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Blocks.AIR);
    }

    @Override
    public void onEntityUpdate() {

        super.onEntityUpdate();
        if (this.isBurning()) {
            this.worldObj.setBlockState(new BlockPos(this.posX, this.posY, this.posZ), Blocks.FIRE.getDefaultState());
        }
    }


}
