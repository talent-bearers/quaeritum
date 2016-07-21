package eladkay.quaritum.common.entity;

import eladkay.quaritum.common.Quaritum;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {
    private static int id;

    public static void init() {
        registerModEntityWithEgg(EntityChaosborn.class, LibNames.CHAOSBORN, 15451, 45615);
        registerModEntity(EntityCircleOfTheFinalMoment.class, LibNames.CIRCLE);
    }

    public static void registerModEntityWithEgg(Class<? extends Entity> parEntityClass, String parEntityName, int parEggColor, int parEggSpotsColor) {
        EntityRegistry.registerModEntity(parEntityClass, parEntityName, ++id,
                Quaritum.instance, 80, 3, false);
        EntityRegistry.registerEgg(parEntityClass, parEggColor, parEggSpotsColor);
    }

    public static void registerModEntity(Class<? extends Entity> parEntityClass, String parEntityName) {
        EntityRegistry.registerModEntity(parEntityClass, parEntityName, ++id,
                Quaritum.instance, 80, 3, false);
    }
}
