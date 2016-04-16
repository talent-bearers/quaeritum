package eladkay.quartium;

import eladkay.quartium.rituals.blocks.BlockBlueprint;
import eladkay.quartium.rituals.runners.diagram.TileEntityBlueprint;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

//@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, dependencies = References.DEPENDENCIES, acceptedMinecraftVersions = References.ACCEPTED_VERSION, guiFactory = References.GUI_FACTORY)
@Mod(modid = References.MODID)
public class Quartium {
    BlockBlueprint blueprint = new BlockBlueprint();
    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {
        boolean client = event.getSide() == Side.CLIENT;
        System.out.println("TEST");
        ModObjects.registerTE(new TileEntityBlueprint());
        //ModObjects.registerBlock(blueprint, client);
        ModObjects.registerBlock(blueprint, client);
    }
}
