package eladkay.quaritum.common.entity;

import eladkay.quaritum.common.Quartium;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {
    private static int id;

    public static void init() {
        registerModEntityWithEgg(EntityChaosborn.class, LibNames.CHAOSBORN, 15451, 45615);
    }

    public static void registerModEntityWithEgg(Class parEntityClass, String parEntityName,
                                                int parEggColor, int parEggSpotsColor) {
        EntityRegistry.registerModEntity(parEntityClass, parEntityName, ++id,
                Quartium.getInstance(), 80, 3, false);
        EntityRegistry.registerEgg(parEntityClass, parEggColor, parEggSpotsColor);
    }
}
