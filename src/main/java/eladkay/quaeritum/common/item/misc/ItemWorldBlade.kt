package eladkay.quaeritum.common.item.misc

import com.google.common.collect.Multimap
import com.teamwizardry.librarianlib.common.base.ModCreativeTab
import com.teamwizardry.librarianlib.common.base.item.ItemModSword
import com.teamwizardry.librarianlib.common.util.ItemNBTHelper
import com.teamwizardry.librarianlib.common.util.times
import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.common.Quaeritum
import eladkay.quaeritum.common.core.QuaeritumMethodHandles
import eladkay.quaeritum.common.core.RayHelper
import eladkay.quaeritum.common.core.Tab
import eladkay.quaeritum.common.lib.LibMaterials
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.state.IBlockState
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

class ItemWorldBlade : ItemModSword(LibNames.WORLD_BLADE, LibMaterials.MYSTIC) {

    internal var shouldUseElucentParticles = false

    override fun onUpdate(stack: ItemStack, worldIn: World, entityIn: Entity?, itemSlot: Int, isSelected: Boolean) {
        if (worldIn.isRemote && entityIn is EntityPlayer && stack.itemDamage > 0 && AnimusHelper.Network.requestAnimus(entityIn as EntityPlayer?, 2, 0, true))
            stack.itemDamage = stack.itemDamage - 1

        val ticks = ItemNBTHelper.getInt(stack, TAG_TELEPORTED, 0)
        if (ticks > 0 && entityIn is EntityLivingBase)
            QuaeritumMethodHandles.setSwingTicks((entityIn as EntityLivingBase?)!!, ticks)
        ItemNBTHelper.removeEntry(stack, TAG_TELEPORTED)
    }

    override fun onBlockDestroyed(stack: ItemStack, worldIn: World, state: IBlockState, pos: BlockPos, entityLiving: EntityLivingBase): Boolean {
        if (state.getBlockHardness(worldIn, pos) > 0)
            AnimusHelper.damageItem(stack, 1, entityLiving, 1, 0)
        return true
    }

    override fun hitEntity(stack: ItemStack, target: EntityLivingBase?, attacker: EntityLivingBase): Boolean {
        AnimusHelper.damageItem(stack, 1, attacker, 1, 0)
        return true
    }

    override fun onEntitySwing(entityLiving: EntityLivingBase, stack: ItemStack): Boolean {
        if (entityLiving is EntityPlayer && entityLiving.cooldownTracker.hasCooldown(this))
            return false

        var pos = Vec3d.ZERO
        var hitEntity = false

        val hit = RayHelper.getEntityLookedAt(entityLiving, 8.0)
        if (hit != null) {
            pos = hit.positionVector
            hitEntity = true
        } else {
            val result = RayHelper.raycast(entityLiving, 8.0)
            if (result == null) {
                pos = entityLiving.positionVector.add(entityLiving.lookVec * 8.0)
            } else {
                when (result.typeOfHit) {
                    RayTraceResult.Type.BLOCK -> pos = Vec3d(result.blockPos).add(Vec3d(result.sideHit.directionVec)).addVector(0.5, 0.5, 0.5)
                    RayTraceResult.Type.ENTITY -> {
                        pos = result.entityHit.positionVector
                        hitEntity = true
                    }
                    else -> {}
                }
            }
        }

        pos = Vec3d(pos.xCoord, Math.max(pos.yCoord, entityLiving.posY), pos.zCoord)
        val blockPos = BlockPos(pos)

        if (entityLiving.worldObj.getBlockState(blockPos.up()).getCollisionBoundingBox(entityLiving.worldObj, blockPos.up()) == null) {
            if (entityLiving.worldObj.isRemote)
                for (i in 0..99)
                    entityLiving.worldObj.spawnParticle(EnumParticleTypes.PORTAL,
                            entityLiving.posX + (entityLiving.worldObj.rand.nextDouble() - 0.5) * entityLiving.width.toDouble(),
                            entityLiving.posY + entityLiving.worldObj.rand.nextDouble() * entityLiving.height.toDouble() - 0.25,
                            entityLiving.posZ + (entityLiving.worldObj.rand.nextDouble() - 0.5) * entityLiving.width.toDouble(),
                            (entityLiving.worldObj.rand.nextDouble() - 0.5) * 2.0, -entityLiving.worldObj.rand.nextDouble(),
                            (entityLiving.worldObj.rand.nextDouble() - 0.5) * 2.0)

            entityLiving.setPosition(pos.xCoord, pos.yCoord, pos.zCoord)
            entityLiving.fallDistance = 0f

            if (entityLiving.worldObj.isRemote)
                for (i in 0..99) {
                    if (!shouldUseElucentParticles)
                        entityLiving.worldObj.spawnParticle(EnumParticleTypes.PORTAL,
                                entityLiving.posX + (entityLiving.worldObj.rand.nextDouble() - 0.5) * entityLiving.width.toDouble(),
                                entityLiving.posY + entityLiving.worldObj.rand.nextDouble() * entityLiving.height.toDouble() - 0.25,
                                entityLiving.posZ + (entityLiving.worldObj.rand.nextDouble() - 0.5) * entityLiving.width.toDouble(),
                                (entityLiving.worldObj.rand.nextDouble() - 0.5) * 2.0,
                                -entityLiving.worldObj.rand.nextDouble(),
                                (entityLiving.worldObj.rand.nextDouble() - 0.5) * 2.0)
                    else
                        Quaeritum.proxy!!.spawnStafflikeParticles(
                                entityLiving.worldObj, //world
                                entityLiving.posX + (entityLiving.worldObj.rand.nextDouble() - 0.5) * entityLiving.width.toDouble(), //x
                                entityLiving.posY + entityLiving.worldObj.rand.nextDouble() * entityLiving.height.toDouble() - 0.25, //y
                                entityLiving.posZ + (entityLiving.worldObj.rand.nextDouble() - 0.5) * entityLiving.width.toDouble() //z
                        )
                }

            entityLiving.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1f, 1f)


            if (!hitEntity)
                ItemNBTHelper.setInt(stack, TAG_TELEPORTED, QuaeritumMethodHandles.getSwingTicks(entityLiving))

            AnimusHelper.damageItem(stack, 1, entityLiving, 1, 0)

            if (entityLiving is EntityPlayer && entityLiving.worldObj.isRemote)
                entityLiving.cooldownTracker.setCooldown(this, entityLiving.cooldownPeriod.toInt())
        }

        return false
    }

    override fun getAttributeModifiers(slot: EntityEquipmentSlot, stack: ItemStack?): Multimap<String, AttributeModifier> {
        val multimap = super.getAttributeModifiers(slot, stack)

        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.attributeUnlocalizedName)
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.attributeUnlocalizedName, AttributeModifier(Item.ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316, 0))
        }

        return multimap
    }

    override fun getRarity(stack: ItemStack): EnumRarity {
        return EnumRarity.EPIC
    }

    companion object {

        val TAG_TELEPORTED = "teleportTicks"
    }

    override val creativeTab: ModCreativeTab?
        get() = Tab
}
