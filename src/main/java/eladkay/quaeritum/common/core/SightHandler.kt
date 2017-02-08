package eladkay.quaeritum.common.core

import eladkay.quaeritum.api.SightEvent
import eladkay.quaeritum.common.potions.PotionSoulgaze
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * @author WireSegal
 * Created at 9:13 PM on 2/6/17.
 */
object SightHandler {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    private val sightHolder = ThreadLocal.withInitial { true }
    private var sightOverride: Boolean
        get() = sightHolder.get()
        set(value) = sightHolder.set(value)

    fun hasTheSight(player: EntityPlayer): Boolean {
        if (sightOverride) return true
        val event = SightEvent(player)
        MinecraftForge.EVENT_BUS.post(event)
        return event.hasThirdEye
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun renderTick(e: TickEvent.RenderTickEvent) {
        sightOverride = false
        val thePlayer = Minecraft.getMinecraft().thePlayer ?: return
        if (e.phase == TickEvent.Phase.START)
            sightOverride = hasTheSight(thePlayer)
    }

    @SubscribeEvent
    fun defaultSightApplication(e: SightEvent) {
        if (e.hasThirdEye) return

        if (e.player.getActivePotionEffect(MobEffects.BLINDNESS) != null) e.hasThirdEye = true
        else if (PotionSoulgaze.hasEffect(e.player)) e.hasThirdEye = true
        // todo when crown is implemented
    }
}
