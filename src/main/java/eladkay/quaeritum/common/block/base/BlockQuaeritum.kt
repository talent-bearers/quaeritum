package eladkay.quaeritum.common.block.base

import com.teamwizardry.librarianlib.common.base.ModCreativeTab
import com.teamwizardry.librarianlib.common.base.block.BlockMod
import eladkay.quaeritum.common.core.Tab
import net.minecraft.block.material.Material

/**
 * @author WireSegal
 * Created at 6:42 PM on 11/5/16.
 */
open class BlockQuaeritum(name: String, materialIn: Material, vararg variants: String) : BlockMod(name, materialIn, *variants) {
    override val creativeTab: ModCreativeTab?
        get() = Tab
}
