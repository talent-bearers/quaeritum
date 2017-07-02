package eladkay.quaeritum.common.spell

import com.google.common.base.Predicates
import com.teamwizardry.librarianlib.features.utilities.RaycastUtils
import eladkay.quaeritum.api.spell.EnumSpellElement.*
import eladkay.quaeritum.api.spell.SpellParser.registerSpell
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
import net.minecraftforge.fml.relauncher.ReflectionHelper
import java.util.*


/**
 * @author WireSegal
 * Created at 12:19 PM on 7/2/17.
 */
object BasicSpells {
    private fun applyPotionBuff(potion: Potion, player: EntityLivingBase, trailing: Int, total: Int, amplifier: Boolean) {
        val potionEffect = player.removeActivePotionEffect(potion) ?: PotionEffect(potion)
        player.addPotionEffect(PotionEffect(potion, Math.max(5 + trailing * 2 - total, 1) * if (amplifier) 10 else 20 + potionEffect.duration,
                if (amplifier) trailing / 2 else 0))

    }


    // From Botania
    fun brainwashEntity(entity: EntityLiving, mobs: List<IMob>): Boolean {
        val target = entity.attackTarget
        var did = false

        if (target == null || target !is IMob) {
            var newTarget: IMob
            do
                newTarget = mobs[entity.world.rand.nextInt(mobs.size)]
            while (newTarget === entity)

            if (newTarget is EntityLiving) {
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

    init {
        registerSpell(arrayOf(EARTH)) { player, trailing, total ->
            applyPotionBuff(MobEffects.RESISTANCE, player, trailing, total, true)
        }

        registerSpell(arrayOf(WATER)) { player, trailing, total ->
            if (player.foodStats.foodLevel > 0) {
                player.heal(trailing * 3f + 2f)
                player.addPotionEffect(PotionEffect(MobEffects.HUNGER, total * 20, total * 2))
            }
            if (total > 3) player.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 500, total - 2))
        }

        registerSpell(arrayOf(FIRE)) { _, _, _ ->
            // todo: fire bolt
        }

        registerSpell(arrayOf(AIR)) { player, trailing, total ->
            val look = player.lookVec
            val speedVec = look.scale(0.75 + trailing.toDouble() / total).addVector(player.motionX, player.motionY, player.motionZ)

            player.motionX = speedVec.x
            player.motionY = speedVec.y
            player.motionZ = speedVec.z

            player.fallDistance = 0f
        }

        registerSpell(arrayOf(METAL)) { player, trailing, total ->
            applyPotionBuff(PotionIronskin, player, trailing, total, false)
        }

        registerSpell(arrayOf(ENTROPY)) { player, trailing, total ->
            RaycastUtils.getEntityLookedAt(player)
                    ?.attackEntityFrom(DamageSource.causeIndirectMagicDamage(player, player), (2 + trailing.toFloat()) / total)
        }

        registerSpell(arrayOf(FORM)) { player, trailing, total ->
            applyPotionBuff(MobEffects.INVISIBILITY, player, trailing, total, false)
        }

        registerSpell(arrayOf(FLOW)) { player, trailing, total ->
            applyPotionBuff(MobEffects.JUMP_BOOST, player, trailing, total, true)
            applyPotionBuff(MobEffects.SPEED, player, trailing, total, true)
        }

        registerSpell(arrayOf(CONNECTION)) { player, trailing, total ->
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

        registerSpell(arrayOf(SPIRIT)) { player, trailing, total ->
            val range = 3 + (trailing.toDouble() + 1) / total
            @Suppress("UNCHECKED_CAST")
            val entities = player.world.getEntitiesWithinAABB(Entity::class.java, player.entityBoundingBox.grow(range)) {
                it is IMob
            } as List<IMob>

            entities.filterIsInstance<EntityLiving>()
                    .any { brainwashEntity(it, entities) }
        }

        registerSpell(arrayOf(AETHER)) { _, _, _ ->
            // todo: raw power bolt, ignoring spell count
        }

        registerSpell(arrayOf(SOUL)) { _, _, _ ->
            // todo: cognitive echoes that fight for you
        }

    }


}
