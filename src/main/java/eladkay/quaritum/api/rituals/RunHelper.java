package eladkay.quaritum.api.rituals;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RunHelper {
    @NotNull
    public static boolean runRitual(@NotNull IRitual ritual, @NotNull World world, @Nullable BlockPos pos, @Nullable EntityPlayer player) {
        if (ritual.getRitualDuration() == EnumRitualDuration.INSTANT) return ritual.runOnce(world, player, pos);
        else return ritual.runDurable(world, player, pos);
    }
}
