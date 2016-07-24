package eladkay.quaritum.common.rituals;

import eladkay.quaritum.api.rituals.RitualRegistry;
import eladkay.quaritum.common.rituals.works.CircleOfTheFinalMomentWork;
import eladkay.quaritum.common.rituals.works.ShardedSkiesWork;

public class ModWorks {
    public static void init() {
        RitualRegistry.registerWork(new CircleOfTheFinalMomentWork(), new CircleOfTheFinalMomentWork().getUnlocalizedName());
        RitualRegistry.registerWork(new ShardedSkiesWork(), new ShardedSkiesWork().getUnlocalizedName());
    }
}
