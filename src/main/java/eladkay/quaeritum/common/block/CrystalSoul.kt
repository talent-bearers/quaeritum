package eladkay.quaeritum.common.block

import com.teamwizardry.librarianlib.common.base.block.BlockMod
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.material.Material

class CrystalSoul : BlockMod(LibNames.CRYSTAL, Material.ROCK) {

    init {
        setHardness(2f)
    }
}
