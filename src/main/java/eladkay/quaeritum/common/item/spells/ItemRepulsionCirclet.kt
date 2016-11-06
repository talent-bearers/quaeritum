package eladkay.quaeritum.common.item.spells

import baubles.api.BaubleType
import com.teamwizardry.librarianlib.client.util.TooltipHelper
import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.api.spell.IBaubleSpell
import eladkay.quaeritum.common.item.base.ItemSpellBauble
import eladkay.quaeritum.common.lib.LibNames
import eladkay.quaeritum.common.potions.PotionRooted
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect

/**
 * @author WireSegal
 * Created at 6:08 PM on 11/5/16.
 */
class ItemRepulsionCirclet : ItemSpellBauble(LibNames.REPULSOR) {
    object Repulsion : IBaubleSpell {
        override fun getIconStack(bauble: ItemStack, slot: Int) = bauble

        override fun getSpellName(bauble: ItemStack, slot: Int) = TooltipHelper.local("spell.${LibMisc.MOD_ID}.repulse.name")

        override fun onCast(player: EntityPlayer, bauble: ItemStack, slot: Int): Boolean {
            val entities = player.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, player.entityBoundingBox.expand(10.0, 10.0, 10.0))
            var flag = false
            for (entity in entities) if (entity != player) {
                val distSquared = (entity.posX - player.posX) * (entity.posX - player.posX) +
                        (entity.posY - player.posY) * (entity.posY - player.posY) +
                        (entity.posZ - player.posZ) * (entity.posZ - player.posZ)
                if (distSquared < 100 && !player.worldObj.isRemote) {
                    flag = true
                    entity.addPotionEffect(PotionEffect(PotionRooted, 60))
                    val vec = entity.positionVector.subtract(player.positionVector).normalize()
                    entity.setPosition(entity.posX, entity.posY + 1, entity.posZ)
                    entity.onGround = false
                    entity.motionY += 1 + vec.yCoord
                    entity.motionX += vec.xCoord * 1.5
                    entity.motionZ += vec.zCoord * 1.5
                }
            }
            return flag
        }

        override fun getCooldown(player: EntityPlayer, bauble: ItemStack, slot: Int) = 400
    }

    override fun getBaubleType(p0: ItemStack?) = BaubleType.HEAD

    override fun getSpell(bauble: ItemStack, slot: Int) = Repulsion
}
