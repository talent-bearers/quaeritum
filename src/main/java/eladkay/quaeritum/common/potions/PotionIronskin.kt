package eladkay.quaeritum.common.potions;

import com.teamwizardry.librarianlib.features.base.PotionMod
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * @author WireSegal
 * Created at 2:47 PM on 4/15/16.
 */
object PotionIronskin : PotionMod(LibNames.IRONSKIN, false, 0x909090) {

    init {
        MinecraftForge.EVENT_BUS.register(this)
        registerPotionAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, "F412C29C-0DB3-11E6-B4DD-7CEA70D5A8C7", 1.0, 0)
    }

    @SubscribeEvent
    fun onHurt(e: LivingAttackEvent) {
        if (e.source.isProjectile && hasEffect(e.entityLiving))
            e.isCanceled = true
    }
}
