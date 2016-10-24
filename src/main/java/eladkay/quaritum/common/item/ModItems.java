package eladkay.quaritum.common.item;

import eladkay.quaritum.common.Quaritum;
import eladkay.quaritum.common.item.chalk.ItemChalk;
import eladkay.quaritum.common.item.chalk.ItemChalkTempest;
import eladkay.quaritum.common.item.misc.*;
import eladkay.quaritum.common.item.soulstones.*;

public class ModItems {

    public static ItemDormantSoulstone dormant;
    public static ItemAwakenedSoulstone awakened;
    public static ItemPassionateSoulstone passionate;
    public static ItemWorldSoulstone passive;
    public static ItemOppressiveSoulstone oppressive;
    public static ItemAttunedSoulstone attuned;
    public static ItemDebug debug;
    public static ItemChalk chalk;
    public static ItemFertilizer fertilizer;
    public static ItemReagentAtlas altas;
    public static ItemRiftmakerPart riftmakerPart;
    public static ItemWorldBlade worldBlade;
    public static ItemPicture picture;
    public static ItemBook book;
    public static ItemTransducer transducer;
    public static ItemChalkTempest tempest;
    public static ItemHollower hollower;
    public static ItemPlacer placer;
    public static void init() {
        dormant = new ItemDormantSoulstone();
        awakened = new ItemAwakenedSoulstone();
        passionate = new ItemPassionateSoulstone();
        passive = new ItemWorldSoulstone();
        oppressive = new ItemOppressiveSoulstone();
        attuned = new ItemAttunedSoulstone();
        chalk = new ItemChalk();
        fertilizer = new ItemFertilizer();
        altas = new ItemReagentAtlas();
        riftmakerPart = new ItemRiftmakerPart();
        worldBlade = new ItemWorldBlade();
        picture = new ItemPicture();
        transducer = new ItemTransducer();
        tempest = new ItemChalkTempest();
        book = new ItemBook();
        if (Quaritum.isDevEnv) {
            debug = new ItemDebug();
            hollower = new ItemHollower();
            placer = new ItemPlacer();
        }


    }
}
