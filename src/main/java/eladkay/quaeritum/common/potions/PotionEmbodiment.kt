package eladkay.quaeritum.common.potions

import com.teamwizardry.librarianlib.features.base.PotionMod
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.SharedMonsterAttributes

/**
 * @author WireSegal
 * Created at 2:47 PM on 4/15/16.
 */
object PotionEmbodiment : PotionMod(LibNames.EMBODIMENT, false, 0x23B6D3) {

    init {
        registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR, "3516BF84-5C64-4782-9BAD-9E7AF590A53A", 5.0, 0)
        registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR_TOUGHNESS, "774A0C49-3009-4F86-8169-7DA1AD111FDF", 2.0, 0)
        registerPotionAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, "2D8147EA-6531-4AE2-9BF7-B4C941B34197", 0.25, 0)
    }

    override fun isReady(duration: Int, amplifier: Int): Boolean {
        val k = 50 shr amplifier
        return k < 0 || duration % k == 0
    }

    override fun performEffect(player: EntityLivingBase, amplifier: Int) {
        if (player.health < player.maxHealth) player.heal(1.0f)
    }
}
