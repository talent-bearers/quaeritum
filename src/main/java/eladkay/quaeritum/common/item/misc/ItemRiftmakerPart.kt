package eladkay.quaeritum.common.item.misc

import com.teamwizardry.librarianlib.features.base.item.ItemMod

import eladkay.quaeritum.common.lib.LibNames

class ItemRiftmakerPart : ItemMod(LibNames.RIFTMAKER_PART, *ItemRiftmakerPart.TYPES) {

    init {
        setMaxStackSize(1)
    }

    companion object {
        private val TYPES = arrayOf("riftFang", "riftFur", "riftClaw")
    }

}
