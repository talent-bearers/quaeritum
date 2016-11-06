package eladkay.quaeritum.common.block

import eladkay.quaeritum.common.block.base.BlockQuaeritum
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.material.Material

class CrystalSoul : BlockQuaeritum(LibNames.CRYSTAL, Material.ROCK) {

    init {
        setHardness(2f)
    }
}
