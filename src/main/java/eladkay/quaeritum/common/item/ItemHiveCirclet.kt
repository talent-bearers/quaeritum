package eladkay.quaeritum.common.item

import baubles.api.BaubleType
import baubles.api.BaublesApi
import com.teamwizardry.librarianlib.common.util.ItemNBTHelper
import eladkay.quaeritum.common.item.base.ItemModBauble
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * @author WireSegal
 * Created at 9:04 PM on 11/19/16.
 */
class ItemHiveCirclet : ItemModBauble(LibNames.HIVE_CIRCLET) {
    companion object {
        val TAG_UUID = "uuid"

        @SubscribeEvent
        fun onDamageEvent(e: LivingAttackEvent) {
            val player = e.entityLiving
            if (player is EntityPlayer) {
                val baublesPlayer = BaublesApi.getBaublesHandler(player)
                val headPlayer = baublesPlayer.getStackInSlot(BaubleType.HEAD.validSlots[0])
                if (headPlayer != null && headPlayer.item is ItemHiveCirclet) {
                    val uuid = ItemNBTHelper.getUUID(headPlayer, TAG_UUID, true) ?: return
                    val source = e.source.sourceOfDamage
                    if (source is EntityPlayer) {
                        val baublesSource = BaublesApi.getBaublesHandler(source)
                        val headSource = baublesSource.getStackInSlot(BaubleType.HEAD.validSlots[0])
                        if (headSource != null && headSource.item is ItemHiveCirclet) {
                            val otheruuid = ItemNBTHelper.getUUID(headSource, TAG_UUID, true) ?: return
                            if (uuid == otheruuid)
                                e.isCanceled = true
                        }
                    }
                }
            }
        }

        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        /*
            todo:
            add binding recipe
            add bestowing
            add chat channeling
         */
    }

    override fun getBaubleType(p0: ItemStack?) = BaubleType.HEAD
}
