package eladkay.quaritum.common.item.misc;

import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNames;

public class ItemRiftmakerPart extends ItemMod {
    private static final String[] TYPES = new String[]{"riftFang", "riftFur", "riftClaw"};

    public ItemRiftmakerPart() {
        super(LibNames.RIFTMAKER_PART, TYPES);
        setMaxStackSize(1);
    }

}
