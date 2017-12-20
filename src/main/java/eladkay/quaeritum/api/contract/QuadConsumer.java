package eladkay.quaeritum.api.contract;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author WireSegal
 * Created at 9:11 PM on 2/10/17.
 */
@FunctionalInterface
public interface QuadConsumer<T, U, V, W> {
    void consume(@Nullable T t, @NotNull U u, @NotNull V v, @NotNull W w);
}
