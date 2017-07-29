package eladkay.quaeritum.api.internal;

import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * @author WireSegal
 *         Created at 11:50 AM on 7/29/17.
 */
public interface IInternalHandler {
    void syncAnimusData(@NotNull EntityPlayer player);
}
