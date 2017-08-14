package eladkay.quaeritum.common.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.BlockEvent

/**
 * @author WireSegal
 * Created at 8:38 PM on 8/12/17.
 */
class EntityDrill : EntityBaseProjectile {
    constructor(worldIn: World) : super(worldIn)
    constructor(worldIn: World, x: Double, y: Double, z: Double) : super(worldIn, x, y, z)
    constructor(worldIn: World, shooter: EntityLivingBase) : super(worldIn, shooter)

    var blocksLeft = 0

    override fun getDefaultDamageSource() = null
    override fun getShotDamageSource(shooter: Entity) = null

    override fun onImpactEntity(entity: Entity, successful: Boolean) {
        // NO-OP
    }

    override fun onUpdate() {
        super.onUpdate()
        if (!world.isRemote) {
            val posMin = BlockPos(entityBoundingBox.minX, entityBoundingBox.minY, entityBoundingBox.minZ)
            val posMax = BlockPos(entityBoundingBox.maxX, entityBoundingBox.maxY, entityBoundingBox.maxZ)
            for (block in BlockPos.getAllInBoxMutable(posMin, posMax))
                onImpactBlock(Vec3d(block), EnumFacing.UP, block)
        }
    }

    override fun onImpactBlock(hitVec: Vec3d, side: EnumFacing, position: BlockPos) {
        val shooter = shootingEntity
        val state = world.getBlockState(position)
        if (shooter !is EntityPlayer || blocksLeft == 0 ||
                !world.isBlockLoaded(position) || !world.isBlockModifiable(shooter, position) ||
                state.block.getPlayerRelativeBlockHardness(state, shooter, world, position) <= 0 ||
                MinecraftForge.EVENT_BUS.post(BlockEvent.BreakEvent(world, position, state, shooter)))
            super.onImpactBlock(hitVec, side, position)
        else if (!state.block.isReplaceable(world, position)){
            world.spawnEntity(EntityDroppingBlock(world, position.x + 0.5, position.y.toDouble(), position.z + 0.5, state))
            world.setBlockToAir(position)
            blocksLeft--
        }
    }

    override fun writeEntityToNBT(compound: NBTTagCompound) {
        super.writeEntityToNBT(compound)
        compound.setInteger("remaining", blocksLeft)
    }

    override fun readEntityFromNBT(compound: NBTTagCompound) {
        super.readEntityFromNBT(compound)
        blocksLeft = compound.getInteger("remaining")
    }

}
