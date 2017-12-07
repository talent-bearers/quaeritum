package eladkay.quaeritum.common.block.flowers

import com.teamwizardry.librarianlib.features.base.block.BlockModBush
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.material.Material

class BlockFiresoulFlower : BlockModBush(LibNames.FLOWER + "_firesoul", Material.PLANTS) {
    init {
        setLightLevel(1f)
    }
}
