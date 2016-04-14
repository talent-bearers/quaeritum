package eladkay.quartium.rituals;

import net.minecraft.entity.player.EntityPlayer;

public interface IRitualRunner {
    IRitual getValidRitual(EntityPlayer player);
    boolean runRitual(IRitual ritual, EntityPlayer player);
}
