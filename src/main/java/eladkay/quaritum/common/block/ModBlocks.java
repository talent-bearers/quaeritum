package eladkay.quaritum.common.block;

import eladkay.quaritum.api.lib.LibMisc;
import eladkay.quaritum.common.block.base.BlockMod;
import eladkay.quaritum.common.block.chalk.BlockChalk;
import eladkay.quaritum.common.block.chalk.BlockChalkTempest;
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower;
import eladkay.quaritum.common.block.tile.TileEntityBlueprint;
import eladkay.quaritum.common.block.tile.TileEntityFoundationStone;
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
    public static CrystalSoul crystal;
    public static BlockFoundationStone foundation;
    public static BlockChalkTempest tempest;

    public static void init() {
        blueprint = new BlockBlueprint(LibNames.BLUEPRINT);
        chalk = new BlockChalk();
        flower = new BlockAnimusFlower();
        crystal = new CrystalSoul();
        foundation = new BlockFoundationStone();
        tempest = new BlockChalkTempest();

        GameRegistry.registerTileEntity(TileEntityBlueprint.class, new ResourceLocation(LibMisc.MOD_ID, LibNames.BLUEPRINT).toString());
        GameRegistry.registerTileEntity(TileEntityFoundationStone.class, new ResourceLocation(LibMisc.MOD_ID, LibNames.FOUNDATION).toString());
    }
}
