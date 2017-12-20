package eladkay.quaeritum.common.entity

import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class EntityCircleOfTheFinalMoment : Entity {
    var ticksLeft: Int = 0

    constructor(worldIn: World) : super(worldIn) {
        ticksLeft = 1000
    }

    constructor(worldIn: World, pos: BlockPos) : super(worldIn)

    override fun onUpdate() {
        super.onUpdate()
        println("ticks: " + ticksLeft)
        ticksLeft--
        if (ticksLeft <= 0) {
            setDead()
            world.removeEntity(this)
        }
    }

    override fun entityInit() {
        ticksLeft = 1000
    }

    override fun readEntityFromNBT(compound: NBTTagCompound) {
        ticksLeft = compound.getInteger("tix")
    }

    override fun writeEntityToNBT(compound: NBTTagCompound) {
        compound.setInteger("tix", ticksLeft)
    }

    companion object {

        private val RANGE = 16
    }
}
