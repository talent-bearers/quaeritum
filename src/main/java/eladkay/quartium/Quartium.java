package eladkay.quartium;

import eladkay.quartium.rituals.blocks.BlockBlueprint;
import eladkay.quartium.rituals.runners.diagram.TileEntityBlueprint;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, dependencies = References.DEPENDENCIES, acceptedMinecraftVersions = References.ACCEPTED_VERSION, guiFactory = References.GUI_FACTORY)
public class Quartium {
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModObjects.registerTE(new TileEntityBlueprint());
        ModObjects.registerBlock(new BlockBlueprint());
    }
}
