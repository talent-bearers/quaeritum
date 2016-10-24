package eladkay.quaritum.common.block

import com.teamwizardry.librarianlib.common.base.block.BlockMod
import eladkay.quaritum.common.lib.LibNames
import net.minecraft.block.material.Material

class CrystalSoul : BlockMod(LibNames.CRYSTAL, Material.ROCK) {

    init {
        setHardness(2f)
    }
}
