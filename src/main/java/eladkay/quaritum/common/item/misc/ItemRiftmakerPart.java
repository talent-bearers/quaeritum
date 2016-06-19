package eladkay.quaritum.common.item.misc;

import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNames;

public class ItemRiftmakerPart extends ItemMod {
    private static final String[] TYPES = new String[3];

    static {
        TYPES[0] = "riftFang";
        TYPES[1] = "riftFur";
        TYPES[2] = "riftClaw";
    }

    public ItemRiftmakerPart() {
        super(LibNames.RIFTMAKER_PART, TYPES);
        setMaxStackSize(1);
    }

}
