package eladkay.quaritum.common.rituals;

import eladkay.quaritum.api.rituals.RitualRegistry;

public class ModDiagrams {
    public static void init() {
        RitualRegistry.registerDiagram(new AltarOfTheFallingStarDiagram(), "altarOfTheFallingStar");
        RitualRegistry.registerDiagram(new ShardedSkiesDiagram(), "shardedSkies");
        RitualRegistry.registerDiagram(new ShardedSkiesTier2Diagram(), "shardedSkies2");
        RitualRegistry.registerDiagram(new InfusionDiagram(), "infusion");
        RitualRegistry.registerDiagram(new RitualSummoning(), "summonChaosborn");
        RitualRegistry.registerDiagram(new HarmonizedHallDiagram(), "merger");
    }

}
