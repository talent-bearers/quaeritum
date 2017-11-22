package eladkay.quaeritum.common.core

import com.teamwizardry.librarianlib.core.common.RegistrationHandler
import eladkay.quaeritum.api.lib.LibMisc
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent

/**
 * @author WireSegal
 * Created at 4:53 PM on 11/5/16.
 */
object QuaeritumSoundEvents {
    val baubleEquip: SoundEvent
    val centrifuge: SoundEvent

    init {
        var loc = ResourceLocation(LibMisc.MOD_ID, "baubleEquip")
        baubleEquip = SoundEvent(loc)
        RegistrationHandler.register(baubleEquip, loc)

        loc = ResourceLocation(LibMisc.MOD_ID, "centrifuge")
        centrifuge = SoundEvent(loc)
        RegistrationHandler.register(centrifuge, loc)
    }
}
