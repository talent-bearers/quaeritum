package eladkay.quaritum.common.block;

import eladkay.quaritum.common.block.base.BlockModFalling;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.block.material.Material;
import net.minecraftforge.oredict.OreDictionary;

public class GlowSandBlock extends BlockModFalling {

    public GlowSandBlock() {
        super(LibNames.GLOWSAND, Material.SAND);
        OreDictionary.registerOre("glowSand", this);
        setLightLevel(1f);
    }

}
