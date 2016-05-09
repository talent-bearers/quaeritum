package eladkay.quaritum.common.core;

import eladkay.quaritum.api.rituals.RitualRegistry;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.crafting.ModRecipes;
import eladkay.quaritum.common.entity.ModEntities;
import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.rituals.*;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
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
        RitualRegistry.registerDiagram(new AltarOfTheFallingStarDiagram(), "altarOfTheFallingStar");
        RitualRegistry.registerDiagram(new SimpleTestRitualDiagram(), "test");
        RitualRegistry.registerDiagram(new ShardedSkiesDiagram(), "shardedSkies");
        RitualRegistry.registerDiagram(new ShardedSkiesTier2Diagram(), "shardedSkies2");
        RitualRegistry.registerDiagram(new RitualTrashDiagram(), "trash");
        RitualRegistry.registerDiagram(new InfusionDiagram(), "infusion");
        RitualRegistry.registerDiagram(new RitualSummoning(), "summonChaosborn");
        FMLInterModComms.sendMessage("Waila", "register", "eladkay.quaritum.common.compat.waila.Waila.onWailaCall");
    }

    public void init(FMLInitializationEvent e) {

    }

    public void post(FMLPostInitializationEvent e) {

    }
}
