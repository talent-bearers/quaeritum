package eladkay.quaritum.api.rituals;

import net.minecraft.entity.player.EntityPlayer;

public interface IRitualRunner {
    IRitual getValidRitual(EntityPlayer player);

    boolean runRitual(IRitual ritual, EntityPlayer player);

  /*  boolean checkPosChalk(PositionedChalk chalk);

    boolean checkAllPosChalk(ArrayList<PositionedChalk> chalks);

    boolean checkAllPossiblePosChalks(ArrayList<ArrayList<PositionedChalk>> chalks);*/
}
