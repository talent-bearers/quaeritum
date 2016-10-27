package eladkay.quaeritum.common.rituals

import eladkay.quaeritum.api.rituals.RitualRegistry
import eladkay.quaeritum.common.rituals.works.CircleOfTheFinalMomentWork
import eladkay.quaeritum.common.rituals.works.ShardedSkiesWork

object ModWorks {
    fun init() {
        RitualRegistry.registerWork(CircleOfTheFinalMomentWork(), CircleOfTheFinalMomentWork().unlocalizedName)
        RitualRegistry.registerWork(ShardedSkiesWork(), ShardedSkiesWork().unlocalizedName)
    }
}
