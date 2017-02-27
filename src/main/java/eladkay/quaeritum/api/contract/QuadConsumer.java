package eladkay.quaeritum.api.contract;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author WireSegal
 *         Created at 9:11 PM on 2/10/17.
 */
@FunctionalInterface
public interface QuadConsumer<T, U, V, W> {
    void consume(@NotNull T t, @NotNull U u, @NotNull V v, @NotNull W w);
}
