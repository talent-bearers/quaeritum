package eladkay.quaritum.common.core;

import eladkay.quaritum.api.util.Vector3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author WireSegal
 *         Created at 11:45 AM on 7/3/16.
 */
public class RayHelper {
    @Nullable
    public static RayTraceResult raycast(Entity e, double len) {
        return raycast(e, len, false);
    }

    @Nullable
    public static RayTraceResult raycast(Entity e, double len, boolean stopOnLiquid) {
        Vector3 vec = Vector3.fromEntity(e).add(0, e instanceof EntityPlayer ? e.getEyeHeight() : 0.0, 0.0);
        Vec3d look = e.getLookVec();
        if (look == null) return null;
        return raycast(e.worldObj, vec, new Vector3(look), len, stopOnLiquid);
    }

    @Nullable
    public static RayTraceResult raycast(World world, Vector3 origin, Vector3 ray, double len, boolean stopOnLiquid) {
        Vector3 end = origin.add(ray.normalize().multiply(len));
        return world.rayTraceBlocks(origin.toVec3D(), end.toVec3D(), stopOnLiquid);
    }

    @Nullable
    public static Entity getEntityLookedAt(Entity e, double distance) {
        Entity foundEntity = null;

        final double finalDistance = distance;

        RayTraceResult pos = raycast(e, distance);
        Vec3d positionVector = e.getPositionVector();
        if (e instanceof EntityPlayer)
            positionVector = positionVector.addVector(0, e.getEyeHeight(), 0);

        if (pos != null)
            distance = pos.hitVec.distanceTo(positionVector);

        Vec3d lookVector = e.getLookVec();
        Vec3d reachVector = positionVector.addVector(lookVector.xCoord * finalDistance, lookVector.yCoord * finalDistance, lookVector.zCoord * finalDistance);

        Entity lookedEntity = null;
        List<Entity> entitiesInBoundingBox = e.worldObj.getEntitiesWithinAABBExcludingEntity(e, e.getEntityBoundingBox().addCoord(lookVector.xCoord * finalDistance, lookVector.yCoord * finalDistance, lookVector.zCoord * finalDistance).expand(1F, 1F, 1F));
        double minDistance = distance;

        for (Entity entity : entitiesInBoundingBox) {
            if (entity.canBeCollidedWith()) {
                float collisionBorderSize = entity.getCollisionBorderSize();
                AxisAlignedBB hitbox = entity.getEntityBoundingBox().expand(collisionBorderSize, collisionBorderSize, collisionBorderSize);
                RayTraceResult interceptPosition = hitbox.calculateIntercept(positionVector, reachVector);

                if (hitbox.isVecInside(positionVector)) {
                    if (0.0D < minDistance || minDistance == 0.0D) {
                        lookedEntity = entity;
                        minDistance = 0.0D;
                    }
                } else if (interceptPosition != null) {
                    double distanceToEntity = positionVector.distanceTo(interceptPosition.hitVec);

                    if (distanceToEntity < minDistance || minDistance == 0.0D) {
                        lookedEntity = entity;
                        minDistance = distanceToEntity;
                    }
                }
            }

            if (lookedEntity != null && (minDistance < distance || pos == null))
                foundEntity = lookedEntity;
        }

        return foundEntity;
    }
}
