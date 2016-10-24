package eladkay.quaritum.common.item.misc

import com.teamwizardry.librarianlib.common.base.item.ItemMod
import eladkay.quaritum.common.lib.LibNames

class ItemRiftmakerPart : ItemMod(LibNames.RIFTMAKER_PART, *ItemRiftmakerPart.TYPES) {

    init {
        setMaxStackSize(1)
    }

    companion object {
        private val TYPES = arrayOf("riftFang", "riftFur", "riftClaw")
    }

}
