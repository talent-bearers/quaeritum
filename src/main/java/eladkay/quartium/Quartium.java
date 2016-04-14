package eladkay.quartium;

import eladkay.quartium.rituals.blocks.BlockBlueprint;
import eladkay.quartium.rituals.runners.diagram.TileEntityBlueprint;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, dependencies = References.DEPENDENCIES, acceptedMinecraftVersions = References.ACCEPTED_VERSION, guiFactory = References.GUI_FACTORY)
public class Quartium {
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("TEST");
        ModObjects.registerTE(new TileEntityBlueprint());
        ModObjects.registerBlock(new BlockBlueprint());
        GameRegistry.register(new ItemBlock(new BlockBlueprint()), new ResourceLocation(References.MODID, BlockBlueprint.NAME));
    }
}
