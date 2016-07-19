package eladkay.quaritum.common.rituals;

import com.google.common.collect.ImmutableList;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.api.rituals.RitualRegistry;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower;
import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.rituals.diagrams.*;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ModDiagrams {
    public static final CraftingDiagramBase transducer = new CraftingDiagramBase("transducer", new ItemStack[]{new ItemStack(Items.STICK), new ItemStack(ModItems.dormant), new ItemStack(ModBlocks.flower, 1, BlockAnimusFlower.Variants.COMMON_ARCANE.ordinal())}, new ItemStack(ModItems.transducer), ImmutableList.of(PositionedBlock.BLUEPRINT), 20, false, 1, true);
    public static void init() {
        RitualRegistry.registerDiagram(new ShardedSkiesDiagram(), "shardedSkies");
        RitualRegistry.registerDiagram(new ShardedSkiesTier2Diagram(), "shardedSkies2");
        RitualRegistry.registerDiagram(new RitualSummoning(), "summonChaosborn");
        RitualRegistry.registerDiagram(new AltarOfTheFallingStarDiagram(), "altarOfTheFallingStar");
        RitualRegistry.registerDiagram(new HarmonizedHallDiagram(), "merger");
        RitualRegistry.registerDiagram(new InfusionDiagram(), "infusion");
        RitualRegistry.registerDiagram(transducer, "transducer");
    }

}
