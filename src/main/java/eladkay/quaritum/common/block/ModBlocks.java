package eladkay.quaritum.common.block;

import eladkay.quaritum.common.block.base.BlockMod;
import eladkay.quaritum.common.block.base.BlockModFlower;
import eladkay.quaritum.common.block.chalk.BlockChalk;
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower;
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
    public static BlockChalk chalk;
    public static BlockAnimusFlower flower;

    public static void init() {
        blueprint = new BlockBlueprint(LibNames.BLUEPRINT);
        chalk = new BlockChalk();
        flower = new BlockAnimusFlower();
        GameRegistry.registerTileEntity(TileEntityBlueprint.class, new ResourceLocation(LibMisc.MOD_ID, LibNames.BLUEPRINT).toString());
    }
}
