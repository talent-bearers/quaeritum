package eladkay.quaeritum.common.core

import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Items
import net.minecraft.stats.StatBase
import net.minecraft.stats.StatList
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.ReflectionHelper

/**
 * @author WireSegal
 * Created at 3:57 PM on 12/26/17.
 */
object AdvancementManager {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onEntityKilled(e: LivingDeathEvent) {
        val trueSource = e.source.trueSource
        if (trueSource is EntityPlayerMP) {
            val end = trueSource.serverWorld.advancementManager.getAdvancement(ResourceLocation("end/kill_dragon"))
            if (end != null && trueSource.advancements.getProgress(end).isDone) {
                val spirit = trueSource.serverWorld.advancementManager.getAdvancement(ResourceLocation("quaeritum:spirit"))
                if (spirit != null)
                    trueSource.advancements.grantCriterion(spirit, "slain_dragons")
            }
        }
    }

    fun grant(player: EntityPlayerMP, stat: StatBase?, threshold: Int, name: String, criterion: String) {
        if (stat == null) return
        val statValue = player.statFile.readStat(stat)
        if (statValue > threshold) {
            val flow = player.serverWorld.advancementManager.getAdvancement(ResourceLocation("quaeritum:$name"))
            if (flow != null)
                player.advancements.grantCriterion(flow, criterion)
        }
    }

    fun grant(player: EntityPlayerMP, stat: Array<StatBase>, threshold: Int, name: String, criterion: String) {
        val statValue = stat.sumBy { player.statFile.readStat(it) }
        if (statValue > threshold) {
            val flow = player.serverWorld.advancementManager.getAdvancement(ResourceLocation("quaeritum:$name"))
            if (flow != null)
                player.advancements.grantCriterion(flow, criterion)
        }
    }

    fun grantByTotalAchieved(player: EntityPlayerMP, stat: Array<StatBase?>, threshold: Int, name: String, criterion: String) {
        val statValue = stat.filterNotNull().sumBy { if (player.statFile.readStat(it) > 0) 1 else 0 }
        if (statValue > threshold) {
            val flow = player.serverWorld.advancementManager.getAdvancement(ResourceLocation("quaeritum:$name"))
            if (flow != null)
                player.advancements.grantCriterion(flow, criterion)
        }
    }

    val crafts: Array<StatBase?> = ReflectionHelper.getPrivateValue<Array<StatBase?>, StatList>(
            StatList::class.java, null, "ae", "field_188066_af", "CRAFTS_STATS")


    @SubscribeEvent
    fun onEntityLivingTick(e: LivingEvent.LivingUpdateEvent) {
        val player = e.entityLiving
        if (player is EntityPlayerMP) {
            grant(player, StatList.AVIATE_ONE_CM, 100000, "flow", "flown_far")
            grantByTotalAchieved(player, crafts, crafts.filterNotNull().size / 5, "form", "forged_much")
            grant(player, StatList.getCraftStats(Items.IRON_INGOT), 500, "metal", "torn_earth")
            grant(player, StatList.MOB_KILLS, 2000, "entropy", "culled_weak")
        }
    }
}
