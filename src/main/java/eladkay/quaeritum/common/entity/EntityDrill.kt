package eladkay.quaeritum.common.entity

import net.minecraft.block.BlockFalling
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumActionResult
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

    companion object {
        fun dropBlock(shooter: Entity?, world: World, position: BlockPos, shouldDrop: Boolean = true): EnumActionResult {
            val state = world.getBlockState(position)
            return if (shooter !is EntityPlayer ||
                    !world.isBlockLoaded(position) || !world.isBlockModifiable(shooter, position) ||
                    state.block.getPlayerRelativeBlockHardness(state, shooter, world, position) <= 0 ||
                    MinecraftForge.EVENT_BUS.post(BlockEvent.BreakEvent(world, position, state, shooter)))
                EnumActionResult.FAIL
            else if (!state.block.isReplaceable(world, position)) {
                world.setBlockToAir(position)
                world.spawnEntity(EntityDroppingBlock(world,
                        position.x + 0.5,
                        position.y.toDouble() +
                                if (!BlockFalling.canFallThrough(world.getBlockState(position.down())))
                                    0.01f
                                else
                                    0f,
                        position.z + 0.5,
                        state)
                        .withDrop(shouldDrop))
                EnumActionResult.SUCCESS
            } else
                EnumActionResult.PASS
        }
    }

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
                if (blocksLeft == 0) {
                    setDead()
                    break
                } else when (dropBlock(shootingEntity, world, position)) {
                    EnumActionResult.SUCCESS -> blocksLeft--
                    EnumActionResult.FAIL -> setDead()
                    else -> {
                    } // NO-OP
                }
        }
    }

    override fun onImpactBlock(hitVec: Vec3d, side: EnumFacing, position: BlockPos) {
        if (blocksLeft == 0) setDead()
        else
            when (dropBlock(shootingEntity, world, position)) {
                EnumActionResult.SUCCESS -> blocksLeft--
                EnumActionResult.FAIL -> setDead()
                else -> {
                } // NO-OP
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
