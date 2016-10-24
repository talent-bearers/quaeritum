package eladkay.quaritum.common.core

import com.google.common.base.Throwables
import eladkay.quaritum.common.lib.LibObfuscation
import net.minecraft.entity.EntityLivingBase
import net.minecraftforge.fml.relauncher.ReflectionHelper
import org.apache.logging.log4j.Level
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles.publicLookup

/**
 * @author WireSegal
 * *         Created at 11:11 AM on 7/3/16.
 */
object QuaritumMethodHandles {

    private val swingTicksGetter: MethodHandle
    private val swingTicksSetter: MethodHandle

    init {
        try {
            val f = ReflectionHelper.findField(EntityLivingBase::class.java, *LibObfuscation.ENTITYLIVINGBASE_TICKSSINCELASTSWING)
            swingTicksGetter = publicLookup().unreflectGetter(f)
            swingTicksSetter = publicLookup().unreflectSetter(f)
        } catch (t: Throwable) {
            LogHelper.LOGGER.log(Level.ERROR, "Couldn't initialize methodhandles! Things will be broken!")
            t.printStackTrace()
            throw Throwables.propagate(t)
        }

    }

    fun getSwingTicks(entity: EntityLivingBase): Int {
        try {
            return swingTicksGetter.invokeExact(entity) as Int
        } catch (t: Throwable) {
            throw propagate(t)
        }

    }

    fun setSwingTicks(entity: EntityLivingBase, ticks: Int) {
        try {
            swingTicksSetter.invokeExact(entity, ticks)
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
