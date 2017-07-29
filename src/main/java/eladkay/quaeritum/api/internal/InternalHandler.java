package eladkay.quaeritum.api.internal;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author WireSegal
 *         Created at 11:51 AM on 7/29/17.
 */
public final class InternalHandler {
    @NotNull
    private static IInternalHandler handler = new DummyInternalHandler();

    @NotNull
    public static IInternalHandler getInternalHandler() {
        return handler;
    }

    public static void setInternalHandler(@NotNull IInternalHandler newHandler) {
        ModContainer container = Loader.instance().activeModContainer();
        if (container == null || !Objects.equals(container.getModId(), "quaeritum")) return;
        handler = newHandler;
    }
}
