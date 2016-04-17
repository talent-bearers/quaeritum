package eladkay.quaritum.common.block.chalk;

import eladkay.quaritum.common.block.base.BlockMod;
import eladkay.quaritum.common.item.chalk.ItemChalk;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.block.material.Material;

public class BlockChalk extends BlockMod {

    public BlockChalk() {
        super("block" + ItemChalk.capitalizeFirst(LibNames.CHALK), Material.cake, ItemChalk.COLORS);
    }

}
