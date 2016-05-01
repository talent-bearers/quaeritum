package eladkay.quaritum.common.core;

import eladkay.quaritum.api.rituals.RitualRegistry;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.crafting.ModRecipes;
import eladkay.quaritum.common.entity.ModEntities;
import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.rituals.AltarOfTheFallingStarDiagram;
import eladkay.quaritum.common.rituals.ShardedSkiesDiagram;
import eladkay.quaritum.common.rituals.ShardedSkiesTier2Diagram;
import eladkay.quaritum.common.rituals.SimpleTestRitualDiagram;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author WireSegal
 *         Created at 4:29 PM on 4/16/16.
 */
public class CommonProxy {
    public void pre(FMLPreInitializationEvent e) {
        ModBlocks.init();
        ModItems.init();
        ModEntities.init();
        ModRecipes.init();
        RitualRegistry.registerRitual(new AltarOfTheFallingStarDiagram(), "altarOfTheFallingStar");
        RitualRegistry.registerRitual(new SimpleTestRitualDiagram(), "test");
        RitualRegistry.registerRitual(new ShardedSkiesDiagram(), "shardedSkies");
        RitualRegistry.registerRitual(new ShardedSkiesTier2Diagram(), "shardedSkies2");
    }

    public void init(FMLInitializationEvent e) {

    }

    public void post(FMLPostInitializationEvent e) {

    }
}
