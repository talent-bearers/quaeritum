package eladkay.quaritum.common.block;

import eladkay.quaritum.common.block.base.BlockMod;
import eladkay.quaritum.common.block.tile.TileEntityBlueprint;
import eladkay.quaritum.common.lib.LibMisc;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author WireSegal
 *         Created at 5:00 PM on 4/16/16.
 */
public class ModBlocks {
    public static BlockMod blueprint;

    public static void init() {
        blueprint = new BlockBlueprint(LibNames.BLUEPRINT);

        GameRegistry.registerTileEntity(TileEntityBlueprint.class, new ResourceLocation(LibMisc.MOD_ID, LibNames.BLUEPRINT).toString());
    }
}
