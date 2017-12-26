package eladkay.quaeritum.common.core

import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

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
}
