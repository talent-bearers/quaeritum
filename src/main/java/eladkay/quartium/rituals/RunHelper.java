package eladkay.quartium.rituals;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RunHelper {
    public static boolean runRitual(IRitual ritual, World world, BlockPos pos, EntityPlayer player) {
        if (ritual.getRitualDuration() == EnumRitualDuration.INSTANT) return ritual.runOnce(world, player, pos);
        else return ritual.runDurable(world, player, pos);
    }
}
