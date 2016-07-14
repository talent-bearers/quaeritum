package eladkay.quaritum.common.rituals;

import eladkay.quaritum.api.rituals.RitualRegistry;
import eladkay.quaritum.common.rituals.works.SimpleTestWork;

public class ModWorks {
    public static void init() {
        RitualRegistry.registerWork(new SimpleTestWork(), new SimpleTestWork().getUnlocalizedName());
    }
}
