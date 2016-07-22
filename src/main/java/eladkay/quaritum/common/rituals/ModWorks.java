package eladkay.quaritum.common.rituals;

import eladkay.quaritum.api.rituals.RitualRegistry;
import eladkay.quaritum.common.rituals.works.CircleOfTheFinalMomentWork;

public class ModWorks {
    public static void init() {
        RitualRegistry.registerWork(new CircleOfTheFinalMomentWork(), new CircleOfTheFinalMomentWork().getUnlocalizedName());
    }
}
