package eladkay.quaeritum.common.core

import com.teamwizardry.librarianlib.common.util.MethodHandleHelper
import eladkay.quaeritum.common.lib.LibObfuscation
import net.minecraft.entity.EntityLivingBase

/**
 * @author WireSegal
 * *         Created at 11:11 AM on 7/3/16.
 */
object QuaeritumMethodHandles {

    private val swingTicksGetter = MethodHandleHelper.wrapperForGetter(EntityLivingBase::class.java, *LibObfuscation.ENTITYLIVINGBASE_TICKSSINCELASTSWING)
    private val swingTicksSetter = MethodHandleHelper.wrapperForSetter(EntityLivingBase::class.java, *LibObfuscation.ENTITYLIVINGBASE_TICKSSINCELASTSWING)

    fun getSwingTicks(entity: EntityLivingBase) = swingTicksGetter(entity) as Int
    fun setSwingTicks(entity: EntityLivingBase, ticks: Int) = swingTicksSetter(entity, ticks)
}
