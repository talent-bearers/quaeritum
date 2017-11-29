package eladkay.quaeritum.common.potions

import com.teamwizardry.librarianlib.features.base.PotionMod
import net.minecraft.block.material.Material
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.potion.PotionEffect
import net.minecraft.util.EnumParticleTypes
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * @author WireSegal
 * Created at 2:47 PM on 4/15/16.
 */
object PotionVoidbind : PotionMod("unbound", false, 0x590D57) {
    init {
        MinecraftForge.EVENT_BUS.register(this)
        registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "459E48AE-2C73-4457-A6F6-46D5fC13A733", 0.4, 2)
        registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "129CCB40-9C9A-4D42-BF82-F0E5113BDFAE", 3.0, 0)
        registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR, "4516BF84-5C64-4782-9BAD-9E7AF590A53A", 5.0, 0)
        registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR_TOUGHNESS, "974A0C49-3009-4F86-8169-7DA1AD111FDF", 2.0, 0)
        registerPotionAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, "3D8147EA-6531-4AE2-9BF7-B4C941B34197", 0.25, 0)
    }

    override fun isReady(duration: Int, amplifier: Int) = true

    override fun performEffect(player: EntityLivingBase, amplifier: Int) {
        player.activePotionEffects
                .filter { it.potion != this }
                .forEach {
                    if (it.potion.isBadEffect)
                        player.removePotionEffect(it.potion)
                    else
                        it.combine(PotionEffect(it.potion,
                            it.duration + 1,
                            it.amplifier,
                            it.isAmbient,
                            it.doesShowParticles())
                            .apply { curativeItems = it.curativeItems }) }
        player.fallDistance = Math.min(player.fallDistance, 4f)
        if ((player.onGround || player !is EntityPlayer || player.capabilities.isFlying) && player.moveForward > 0F && !player.isInsideOfMaterial(Material.WATER))
            player.moveRelative(0.0f, 0.0f, 0.0525f, 1f)
        if (player is EntityPlayer)
            player.foodStats.addStats(1, 0f)

        EntityEquipmentSlot.values()
                .map { player.getItemStackFromSlot(it) }
                .filter { it.isItemDamaged && player.world.rand.nextDouble() < 0.25 }
                .forEach { it.damageItem(-1, player) }

        for (i in 0 until 2)
            player.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, true,
                    player.posX - 0.5 + player.world.rand.nextDouble(),
                    player.posY + player.world.rand.nextDouble() * player.height,
                    player.posZ - 0.5 + player.world.rand.nextDouble(),
                    0.0, 0.0, 0.0)
    }

    @SubscribeEvent
    fun onJump(e: LivingEvent.LivingJumpEvent) {
        if (hasEffect(e.entityLiving))
            e.entityLiving.motionY *= 1.5
    }
}
