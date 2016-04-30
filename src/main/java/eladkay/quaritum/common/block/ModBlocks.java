package eladkay.quaritum.common.block;

import eladkay.quaritum.api.animus.IFlower;
import eladkay.quaritum.common.block.base.BlockMod;
import eladkay.quaritum.common.block.chalk.BlockChalk;
import eladkay.quaritum.common.block.tile.TileEntityBlueprint;
import eladkay.quaritum.common.item.flowers.Arcane;
import eladkay.quaritum.common.item.flowers.Common;
import eladkay.quaritum.common.item.flowers.CommonArcane;
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
    public static IFlower common;
    public static IFlower commonArcane;
    public static IFlower arcane;

    public static void init() {
        blueprint = new BlockBlueprint(LibNames.BLUEPRINT);
        chalk = new BlockChalk();
        common = new Common();
        commonArcane = new CommonArcane();
        arcane = new Arcane();
        GameRegistry.registerTileEntity(TileEntityBlueprint.class, new ResourceLocation(LibMisc.MOD_ID, LibNames.BLUEPRINT).toString());
    }
}
