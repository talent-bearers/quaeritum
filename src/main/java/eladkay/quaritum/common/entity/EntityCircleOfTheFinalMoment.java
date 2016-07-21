package eladkay.quaritum.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityCircleOfTheFinalMoment extends Entity {

    private static final int RANGE = 16;
    public int ticksLeft;

    public EntityCircleOfTheFinalMoment(World worldIn) {
        super(worldIn);
        ticksLeft = 1000;
    }

    public EntityCircleOfTheFinalMoment(World worldIn, BlockPos pos) {
        super(worldIn);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        System.out.println("ticks: " + ticksLeft);
        ticksLeft--;
        if (ticksLeft <= 0) {
            kill();
            worldObj.removeEntity(this);
        }
    }

    @Override
    protected void entityInit() {
        ticksLeft = 1000;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        ticksLeft = compound.getInteger("tix");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("tix", ticksLeft);
    }
}
