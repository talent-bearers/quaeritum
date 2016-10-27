package eladkay.quaeritum.common.core

import com.google.common.base.Throwables
import com.teamwizardry.librarianlib.common.util.MethodHandleHelper
import eladkay.quaeritum.common.lib.LibObfuscation
import net.minecraft.entity.EntityLivingBase
import net.minecraftforge.fml.relauncher.ReflectionHelper
import org.apache.logging.log4j.Level

/**
 * @author WireSegal
 * *         Created at 11:11 AM on 7/3/16.
 */
object QuaeritumMethodHandles {

    private val swingTicksGetter: (EntityLivingBase)->Any?
    private val swingTicksSetter: (EntityLivingBase, Any?)->Unit

    init {
        try {
            val f = ReflectionHelper.findField(EntityLivingBase::class.java, *LibObfuscation.ENTITYLIVINGBASE_TICKSSINCELASTSWING)
            swingTicksGetter = MethodHandleHelper.wrapperForGetter(EntityLivingBase::class.java, *LibObfuscation.ENTITYLIVINGBASE_TICKSSINCELASTSWING)
            swingTicksSetter = MethodHandleHelper.wrapperForSetter(EntityLivingBase::class.java, *LibObfuscation.ENTITYLIVINGBASE_TICKSSINCELASTSWING)
        } catch (t: Throwable) {
            LogHelper.LOGGER.log(Level.ERROR, "Couldn't initialize methodhandles! Things will be broken!")
            t.printStackTrace()
            throw Throwables.propagate(t)
        }

    }

    fun getSwingTicks(entity: EntityLivingBase): Int {
        try {
            return swingTicksGetter(entity) as Int
        } catch (t: Throwable) {
            throw propagate(t)
        }

    }

    fun setSwingTicks(entity: EntityLivingBase, ticks: Int) {
        try {
            swingTicksSetter(entity, ticks)
        } catch (t: Throwable) {
            throw propagate(t)
        }

    }

    private fun propagate(t: Throwable): RuntimeException {
        LogHelper.LOGGER.log(Level.ERROR, "Methodhandle failed!")
        t.printStackTrace()
        return Throwables.propagate(t)
    }

}
