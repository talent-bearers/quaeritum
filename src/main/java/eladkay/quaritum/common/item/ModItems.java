package eladkay.quaritum.common.item;

import eladkay.quaritum.common.animus.DormantSoulstone;
import eladkay.quaritum.common.animus.SoulstoneAwakened;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNames;

public class ModItems {
    public static DormantSoulstone dormant;
    public static DormantSoulstone awakened;
    public static ItemMod debug;

    public static void init() {
        debug = new DebugItem(LibNames.DEBUG_ITEM);
        dormant = new DormantSoulstone();
        awakened = new SoulstoneAwakened();
    }
}
