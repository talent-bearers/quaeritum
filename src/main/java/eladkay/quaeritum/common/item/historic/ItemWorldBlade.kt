package eladkay.quaeritum.common.item.historic

import com.google.common.collect.Multimap
import com.teamwizardry.librarianlib.features.base.item.ItemModSword
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.kotlin.times
import com.teamwizardry.librarianlib.features.utilities.RaycastUtils
import eladkay.quaeritum.common.core.QuaeritumMethodHandles
import eladkay.quaeritum.common.lib.LibMaterials
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.EnumRarity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class ItemWorldBlade : ItemModSword(LibNames.WORLD_BLADE, LibMaterials.TEMPESTEEL) {

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity?, itemSlot: Int, isSelected: Boolean) {
        val ticks = ItemNBTHelper.getInt(stack, TAG_TELEPORTED, 0)
        if (ticks > 0 && entityIn is EntityLivingBase)
            QuaeritumMethodHandles.setSwingTicks((entityIn as EntityLivingBase?)!!, ticks)
        ItemNBTHelper.removeEntry(stack, TAG_TELEPORTED)
        if (stack.tagCompound?.size == 0)
            stack.tagCompound = null
    }

    override fun onEntitySwing(entityLiving: EntityLivingBase, stack: ItemStack): Boolean {
        if (entityLiving is EntityPlayer && entityLiving.cooldownTracker.hasCooldown(this))
            return false

        var pos: Vec3d
        var hitEntity = false

        val hit = RaycastUtils.getEntityLookedAt(entityLiving, 8.0)
        if (hit != null) {
            pos = hit.positionVector
            hitEntity = true
        } else {
            val result = RaycastUtils.raycast(entityLiving, 8.0)
            if (result == null) {
                pos = entityLiving.positionVector.add(entityLiving.lookVec * 8.0)
            } else {
                when (result.typeOfHit) {
                    RayTraceResult.Type.BLOCK -> pos = Vec3d(result.blockPos).add(Vec3d(result.sideHit.directionVec)).addVector(0.5, 0.5, 0.5)
                    RayTraceResult.Type.ENTITY -> {
                        pos = result.entityHit.positionVector
                        hitEntity = true
                    }
                    else -> pos = entityLiving.positionVector.add(entityLiving.lookVec * 8.0)
                }
            }
        }

        pos = Vec3d(pos.x, Math.max(pos.y, entityLiving.posY), pos.z)
        val blockPos = BlockPos(pos)

        if (entityLiving.world.getBlockState(blockPos.up()).getCollisionBoundingBox(entityLiving.world, blockPos.up()) == null) {
            if (entityLiving.world.isRemote)
                for (i in 0..99)
                    entityLiving.world.spawnParticle(EnumParticleTypes.PORTAL,
                            entityLiving.posX + (entityLiving.world.rand.nextDouble() - 0.5) * entityLiving.width.toDouble(),
                            entityLiving.posY + entityLiving.world.rand.nextDouble() * entityLiving.height.toDouble() - 0.25,
                            entityLiving.posZ + (entityLiving.world.rand.nextDouble() - 0.5) * entityLiving.width.toDouble(),
                            (entityLiving.world.rand.nextDouble() - 0.5) * 2.0, -entityLiving.world.rand.nextDouble(),
                            (entityLiving.world.rand.nextDouble() - 0.5) * 2.0)

            entityLiving.setPosition(pos.x, pos.y, pos.z)
            entityLiving.fallDistance = 0f

            if (entityLiving.world.isRemote)
                for (i in 0..99)
                    entityLiving.world.spawnParticle(EnumParticleTypes.PORTAL,
                            entityLiving.posX + (entityLiving.world.rand.nextDouble() - 0.5) * entityLiving.width.toDouble(),
                            entityLiving.posY + entityLiving.world.rand.nextDouble() * entityLiving.height.toDouble() - 0.25,
                            entityLiving.posZ + (entityLiving.world.rand.nextDouble() - 0.5) * entityLiving.width.toDouble(),
                            (entityLiving.world.rand.nextDouble() - 0.5) * 2.0,
                            -entityLiving.world.rand.nextDouble(),
                            (entityLiving.world.rand.nextDouble() - 0.5) * 2.0)
            entityLiving.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1f, 1f)


            if (!hitEntity)
                ItemNBTHelper.setInt(stack, TAG_TELEPORTED, QuaeritumMethodHandles.getSwingTicks(entityLiving))

            if (entityLiving is EntityPlayer)
                entityLiving.cooldownTracker.setCooldown(this, entityLiving.cooldownPeriod.toInt())
        }

        return false
    }

    override fun getAttributeModifiers(slot: EntityEquipmentSlot, stack: ItemStack?): Multimap<String, AttributeModifier> {
        val multimap = super.getAttributeModifiers(slot, stack)

        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.name)
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.name, AttributeModifier(Item.ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316, 0))
        }

        return multimap
    }

    override fun getRarity(stack: ItemStack): EnumRarity {
        return EnumRarity.EPIC
    }

    companion object {
        val TAG_TELEPORTED = "teleportTicks"
    }
}
