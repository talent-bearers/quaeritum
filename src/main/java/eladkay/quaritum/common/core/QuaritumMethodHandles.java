package eladkay.quaritum.common.core;

import com.google.common.base.Throwables;
import eladkay.quaritum.common.lib.LibObfuscation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;

import static java.lang.invoke.MethodHandles.publicLookup;

/**
 * @author WireSegal
 *         Created at 11:11 AM on 7/3/16.
 */
public class QuaritumMethodHandles {

    @Nonnull
    private static final MethodHandle swingTicksGetter, swingTicksSetter;

    static {
        try {
            Field f = ReflectionHelper.findField(EntityLivingBase.class, LibObfuscation.ENTITYLIVINGBASE_TICKSSINCELASTSWING);
            swingTicksGetter = publicLookup().unreflectGetter(f);
            swingTicksSetter = publicLookup().unreflectSetter(f);
        } catch (Throwable t) {
            LogHelper.LOGGER.log(Level.ERROR, "Couldn't initialize methodhandles! Things will be broken!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }

    public static int getSwingTicks(@Nonnull EntityLivingBase entity) {
        try {
            return (int) swingTicksGetter.invokeExact(entity);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    public static void setSwingTicks(@Nonnull EntityLivingBase entity, int ticks) {
        try {
            swingTicksSetter.invokeExact(entity, ticks);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    private static RuntimeException propagate(Throwable t) {
        LogHelper.LOGGER.log(Level.ERROR, "Methodhandle failed!");
        t.printStackTrace();
        return Throwables.propagate(t);
    }

}
