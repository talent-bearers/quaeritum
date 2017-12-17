package eladkay.quaeritum.api.contract;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author WireSegal
 *         Created at 9:11 PM on 2/10/17.
 */
public interface IContractOath {
    @NotNull
    List<String> getUnlocText(@NotNull ItemStack stack);

    @NotNull
    String getUnlocName(@NotNull ItemStack stack);

    default boolean unlocked(@NotNull EntityPlayer player) {
        return true;
    }

    void fireContract(@Nullable EntityPlayer player, @NotNull ItemStack stack, @NotNull World world, @NotNull BlockPos pos);
}
