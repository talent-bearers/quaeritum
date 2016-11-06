package eladkay.quaeritum.common.item.base

import com.teamwizardry.librarianlib.common.base.ModCreativeTab
import com.teamwizardry.librarianlib.common.base.item.ItemMod
import eladkay.quaeritum.common.core.Tab

/**
 * @author WireSegal
 * Created at 6:42 PM on 11/5/16.
 */
open class ItemQuaeritum(name: String, vararg variants: String) : ItemMod(name, *variants) {
    override val creativeTab: ModCreativeTab?
        get() = Tab
}
