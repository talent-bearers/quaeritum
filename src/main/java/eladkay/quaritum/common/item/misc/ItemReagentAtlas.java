package eladkay.quaritum.common.item.misc;

import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.item.chalk.ItemChalk;
import eladkay.quaritum.common.lib.LibNames;

public class ItemReagentAtlas extends ItemMod {

    public ItemReagentAtlas() {
        super(LibNames.REAGENT + ItemChalk.capitalizeFirst(LibNames.ATLAS));
    }
}
