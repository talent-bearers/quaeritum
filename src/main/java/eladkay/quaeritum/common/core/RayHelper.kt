package eladkay.quaeritum.common.core

import eladkay.quaeritum.api.util.Vector3
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World

/**
 * @author WireSegal
 * *         Created at 11:45 AM on 7/3/16.
 */
object RayHelper {

    @JvmOverloads fun raycast(e: Entity, len: Double, stopOnLiquid: Boolean = false): RayTraceResult? {
        val vec = Vector3.fromEntity(e).add(0.0, if (e is EntityPlayer) e.getEyeHeight().toDouble() else 0.0, 0.0)
        val look = e.lookVec ?: return null
        return raycast(e.worldObj, vec, Vector3(look), len, stopOnLiquid)
    }

    fun raycast(world: World, origin: Vector3, ray: Vector3, len: Double, stopOnLiquid: Boolean): RayTraceResult? {
        val end = origin.add(ray.normalize().multiply(len))
        return world.rayTraceBlocks(origin.toVec3D(), end.toVec3D(), stopOnLiquid)
    }

    fun getEntityLookedAt(e: Entity, distance: Double): Entity? {
        var distance = distance
        var foundEntity: Entity? = null

        val finalDistance = distance

        val pos = raycast(e, distance)
        var positionVector = e.positionVector
        if (e is EntityPlayer)
            positionVector = positionVector.addVector(0.0, e.getEyeHeight().toDouble(), 0.0)

        if (pos != null)
            distance = pos.hitVec.distanceTo(positionVector)

        val lookVector = e.lookVec
        val reachVector = positionVector.addVector(lookVector.xCoord * finalDistance, lookVector.yCoord * finalDistance, lookVector.zCoord * finalDistance)

        var lookedEntity: Entity? = null
        val entitiesInBoundingBox = e.worldObj.getEntitiesWithinAABBExcludingEntity(e, e.entityBoundingBox.addCoord(lookVector.xCoord * finalDistance, lookVector.yCoord * finalDistance, lookVector.zCoord * finalDistance).expand(1.0, 1.0, 1.0))
        var minDistance = distance

        for (entity in entitiesInBoundingBox) {
            if (entity.canBeCollidedWith()) {
                val collisionBorderSize = entity.collisionBorderSize
                val hitbox = entity.entityBoundingBox.expand(collisionBorderSize.toDouble(), collisionBorderSize.toDouble(), collisionBorderSize.toDouble())
                val interceptPosition = hitbox.calculateIntercept(positionVector, reachVector)

                if (hitbox.isVecInside(positionVector)) {
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

            if (lookedEntity != null && (minDistance < distance || pos == null))
                foundEntity = lookedEntity
        }

        return foundEntity
    }
}
