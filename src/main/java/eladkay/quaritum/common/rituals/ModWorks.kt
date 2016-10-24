package eladkay.quaritum.common.rituals

import eladkay.quaritum.api.rituals.RitualRegistry
import eladkay.quaritum.common.rituals.works.CircleOfTheFinalMomentWork
import eladkay.quaritum.common.rituals.works.ShardedSkiesWork

object ModWorks {
    fun init() {
        RitualRegistry.registerWork(CircleOfTheFinalMomentWork(), CircleOfTheFinalMomentWork().unlocalizedName)
        RitualRegistry.registerWork(ShardedSkiesWork(), ShardedSkiesWork().unlocalizedName)
    }
}
