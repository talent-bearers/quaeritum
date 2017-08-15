package eladkay.quaeritum.common.potions

import com.teamwizardry.librarianlib.features.base.PotionMod
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.SharedMonsterAttributes

/**
 * @author WireSegal
 * Created at 2:47 PM on 4/15/16.
 */
object PotionWrath : PotionMod(LibNames.WRATH, false, 0xD32323) {

    init {
        registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "359E48AE-2C73-4457-A6F6-46D5fC13A733", 0.4, 2)
        registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "029CCB40-9C9A-4D42-BF82-F0E5113BDFAE", 3.0, 0)
    }
}
