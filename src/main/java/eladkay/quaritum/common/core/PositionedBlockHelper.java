package eladkay.quaritum.common.core;

import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.block.chalk.EnumChalkColor;

public class PositionedBlockHelper {
    public void setStateFromColor(PositionedBlock block, EnumChalkColor chalkColor) {
        if (chalkColor.ordinal() == 16)
            block.setState(ModBlocks.chalk.getDefaultState());
        else
            block.setState(ModBlocks.chalk.getStateFromMeta(chalkColor.ordinal()));

    }
}
