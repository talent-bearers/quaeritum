package eladkay.quaritum.common.item;

import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.block.base.ItemModBlock;
import eladkay.quaritum.common.lib.LibNames;

public class ItemBlockBlueprint extends ItemModBlock {
    public ItemBlockBlueprint() {
        super(ModBlocks.blueprint);
        this.setUnlocalizedName(LibNames.BLUEPRINT);
    }
}
