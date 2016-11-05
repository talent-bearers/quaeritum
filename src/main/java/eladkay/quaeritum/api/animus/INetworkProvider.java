package eladkay.quaeritum.api.animus;

import com.teamwizardry.librarianlib.common.util.ItemNBTHelper;
import eladkay.quaeritum.api.lib.LibNBT;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface INetworkProvider {
    @Nullable
    default UUID getPlayer(@Nonnull ItemStack stack) {
        String id = ItemNBTHelper.getString(stack, LibNBT.TAG_UUID, null);
        return id == null ? null : UUID.fromString(id);
    }

    @Nonnull
    default ItemStack setPlayer(@Nonnull ItemStack stack, @Nullable UUID player) {
        if (player == null) ItemNBTHelper.removeEntry(stack, LibNBT.TAG_UUID);
        else ItemNBTHelper.setString(stack, LibNBT.TAG_UUID, player.toString());
        return stack;
    }

    boolean isProvider(@Nonnull ItemStack stack);

    boolean isReceiver(@Nonnull ItemStack stack);
}
