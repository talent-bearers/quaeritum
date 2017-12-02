package eladkay.quaeritum.common.spell

import com.google.common.base.Predicates
import com.teamwizardry.librarianlib.features.utilities.RaycastUtils
import eladkay.quaeritum.api.spell.EnumSpellElement.*
import eladkay.quaeritum.api.spell.EnumSpellType.*
import eladkay.quaeritum.api.spell.SpellParser.registerSpell
import eladkay.quaeritum.common.entity.EntityFirebolt
import eladkay.quaeritum.common.lib.LibObfuscation
import eladkay.quaeritum.common.potions.PotionIronskin
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.EntityAIAttackMelee
import net.minecraft.entity.ai.EntityAINearestAttackableTarget
import net.minecraft.entity.monster.IMob
import net.minecraft.init.MobEffects
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.util.DamageSource
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.relauncher.ReflectionHelper
import java.util.*


/**
 * @author WireSegal
 * Created at 12:19 PM on 7/2/17.
 */
object BasicSpells {
    fun applyPotionBuff(potion: Potion, player: EntityLivingBase, trailing: Int, total: Int, amplifier: Boolean) {
        val potionEffect = player.removeActivePotionEffect(potion) ?: PotionEffect(potion)
        player.addPotionEffect(PotionEffect(potion, Math.max(5 + (trailing * 2) / total, 1) * (if (amplifier) 10 else 20) + potionEffect.duration,
                if (amplifier) trailing / 2 else 0, true, true))

    }

    fun Vec3d.max(max: Number): Vec3d {
        if (this.lengthSquared() < max.toDouble() * max.toDouble()) return this
        return this.normalize().scale(max.toDouble())
    }


    // From Botania
    fun brainwashEntity(entity: EntityLiving, mobs: List<IMob>): Boolean {
        val target = entity.attackTarget
        var did = false

        if (target == null || target !is IMob) {
            var newTarget: IMob
            do
                newTarget = mobs[entity.world.rand.nextInt(mobs.size)]
            while (newTarget === entity && mobs.size != 1)

            if (newTarget != entity && newTarget is EntityLiving) {
                val entries = ArrayList(entity.tasks.taskEntries)
                entries.addAll(ArrayList(entity.targetTasks.taskEntries))

                for (entry in entries)
                    if (entry.action is EntityAINearestAttackableTarget<*>) {
                        messWithGetTargetAI(entry.action as EntityAINearestAttackableTarget<*>, newTarget)
                        did = true
                    } else if (entry.action is EntityAIAttackMelee) {
                        did = true
                    }

                if (did)
                    entity.attackTarget = newTarget
            }
        }

        return did
    }

    private fun messWithGetTargetAI(aiEntry: EntityAINearestAttackableTarget<*>, target: EntityLivingBase) {
        ReflectionHelper.setPrivateValue(EntityAINearestAttackableTarget::class.java, aiEntry, Entity::class.java, *LibObfuscation.TARGET_CLASS)
        ReflectionHelper.setPrivateValue(EntityAINearestAttackableTarget::class.java, aiEntry, Predicates.equalTo(target), *LibObfuscation.TARGET_ENTITY_SELECTOR)
    }

    // Basic spells are those with only a single symbol.

    init {
        ComplexSpells

        registerSpell(arrayOf(EARTH), ALTERATION, "resist") { player, trailing, total ->
            applyPotionBuff(MobEffects.RESISTANCE, player, trailing, total, true)
        }

        registerSpell(arrayOf(WATER), RESTORATION, "heal") { player, trailing, total ->
            if (player.foodStats.foodLevel > 0) {
                player.heal(trailing * 3f + 2f)
                player.addPotionEffect(PotionEffect(MobEffects.HUNGER, total * 20, total * 2))
            }
            if (total > 3) player.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 500, total - 2))
        }

        registerSpell(arrayOf(FIRE), EVOCATION, "firebolt") { player, trailing, total ->
            val firebolt = EntityFirebolt(player.world, player)
            val damage = 3f + 3f * trailing / total
            val inaccuracy = Math.max(total - trailing.toFloat(), 0f)
            val velocity = Math.min(2f, 3f * total / (trailing + 1))
            firebolt.damage = damage
            firebolt.gravity = velocity * 0.0125f
            firebolt.setAim(player, player.rotationPitch, player.rotationYaw, velocity, inaccuracy)
            player.world.spawnEntity(firebolt)
        }

        registerSpell(arrayOf(AIR), EVOCATION, "leap") { player, trailing, total ->
            val look = player.lookVec
            val speedVec = look
                    .scale(0.75 + (trailing.toDouble() / 2) / total)
                    .addVector(player.motionX / total, player.motionY / total + look.y * (trailing.toDouble() / 2) / total, player.motionZ / total)

            player.motionX = speedVec.x
            player.motionY = speedVec.y
            player.motionZ = speedVec.z
            player.velocityChanged = true

            player.fallDistance = 0f
        }

        registerSpell(arrayOf(METAL), ALTERATION, "ironskin") { player, trailing, total ->
            applyPotionBuff(PotionIronskin, player, trailing, total, false)
        }

        registerSpell(arrayOf(ENTROPY), EVOCATION, "strike") { player, trailing, total ->
            RaycastUtils.getEntityLookedAt(player)?.run {
                hurtResistantTime = 0
                attackEntityFrom(DamageSource.causeIndirectMagicDamage(player, player), (2 + trailing.toFloat() * 2) / total)
            }
        }

        registerSpell(arrayOf(FORM), ALTERATION, "dissipate") { player, trailing, total ->
            applyPotionBuff(MobEffects.INVISIBILITY, player, trailing, total, false)
        }

        registerSpell(arrayOf(FLOW), ALTERATION, "flow") { player, trailing, total ->
            applyPotionBuff(MobEffects.JUMP_BOOST, player, trailing, total, true)
            applyPotionBuff(MobEffects.SPEED, player, trailing, total, true)
        }

        registerSpell(arrayOf(CONNECTION), ALTERATION, "equalize") { player, trailing, total ->
            val entity = RaycastUtils.getEntityLookedAt(player)
            if (entity != null && entity is EntityLivingBase) {
                val health = entity.health
                val playerHealth = player.health
                val totalHealth = health + playerHealth + (2 + trailing.toFloat()) / total
                val newPlayerHealth = Math.min(totalHealth / 2f, player.maxHealth)
                player.health = newPlayerHealth
                entity.health = Math.min(entity.maxHealth, totalHealth - newPlayerHealth)
            }
        }

        registerSpell(arrayOf(SPIRIT), ALTERATION, "dominate") { player, trailing, total ->
            val range = 3 + (trailing.toDouble() + 1) / total
            val entities = player.world.getEntitiesWithinAABB(Entity::class.java, player.entityBoundingBox.grow(range))
                    .filterIsInstance<IMob>()

            entities.filterIsInstance<EntityLiving>()
                    .any { brainwashEntity(it, entities) }
        }

        registerSpell(arrayOf(AETHER), INCARNATION, "aetherbolt") { player, trailing, total ->
            // todo: raw power bolt, ignoring spell count
        }

        registerSpell(arrayOf(SOUL), INCARNATION, "cognitive_shadow") { player, trailing, total ->
            // todo: cognitive echoes that fight for you
        }

    }


}
