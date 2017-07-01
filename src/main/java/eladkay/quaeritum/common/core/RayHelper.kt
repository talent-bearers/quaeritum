package eladkay.quaeritum.common.core

import com.teamwizardry.librarianlib.common.util.times
import com.teamwizardry.librarianlib.common.util.vec
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

/**
 * @author WireSegal
 * *         Created at 11:45 AM on 7/3/16.
 */
object RayHelper {

    @JvmOverloads fun raycast(e: Entity, len: Double, stopOnLiquid: Boolean = false): RayTraceResult? {
        val vec = e.positionVector.addVector(0.0, if (e is EntityPlayer) e.getEyeHeight().toDouble() else 0.0, 0.0)
        val look = e.lookVec ?: return null
        return raycast(e.world, vec, look, len, stopOnLiquid)
    }

    fun raycast(world: World, origin: Vec3d, ray: Vec3d, len: Double, stopOnLiquid: Boolean): RayTraceResult? {
        val end = origin.add(ray.normalize() * len)
        return world.rayTraceBlocks(origin, end, stopOnLiquid)
    }

    fun getEntityLookedAt(e: Entity, distance: Double): Entity? {
        var dist = distance
        var foundEntity: Entity? = null

        val finalDistance = dist

        val pos = raycast(e, dist)
        var positionVector = e.positionVector
        if (e is EntityPlayer)
            positionVector = positionVector.addVector(0.0, e.getEyeHeight().toDouble(), 0.0)

        if (pos != null)
            dist = pos.hitVec.distanceTo(positionVector)

        val lookVector = e.lookVec
        val reachVector = positionVector.addVector(lookVector.x * finalDistance, lookVector.y * finalDistance, lookVector.z * finalDistance)

        var lookedEntity: Entity? = null
        val vec = vec(lookVector.x * finalDistance, lookVector.y * finalDistance, lookVector.z * finalDistance)
        val entitiesInBoundingBox = e.world.getEntitiesWithinAABBExcludingEntity(e, e.entityBoundingBox.union(AxisAlignedBB(vec, vec)).grow(1.0, 1.0, 1.0))
        var minDistance = dist

        for (entity in entitiesInBoundingBox) {
            if (entity.canBeCollidedWith()) {
                val collisionBorderSize = entity.collisionBorderSize
                val hitbox = entity.entityBoundingBox.expand(collisionBorderSize.toDouble(), collisionBorderSize.toDouble(), collisionBorderSize.toDouble())
                val interceptPosition = hitbox.calculateIntercept(positionVector, reachVector)

                if (hitbox.contains(positionVector)) {
                    if (0.0 < minDistance || minDistance == 0.0) {
                        lookedEntity = entity
                        minDistance = 0.0
                    }
                } else if (interceptPosition != null) {
                    val distanceToEntity = positionVector.distanceTo(interceptPosition.hitVec)

                    if (distanceToEntity < minDistance || minDistance == 0.0) {
                        lookedEntity = entity
                        minDistance = distanceToEntity
                    }
                }
            }

            if (lookedEntity != null && (minDistance < dist || pos == null))
                foundEntity = lookedEntity
        }

        return foundEntity
    }
}
