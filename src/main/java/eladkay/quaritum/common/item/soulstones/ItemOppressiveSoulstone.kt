package eladkay.quaritum.common.item.soulstones

import eladkay.quaritum.common.lib.LibNames
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class ItemOppressiveSoulstone : ItemMod(LibNames.OPPRESSIVE_SOULSTONE) {
    init {
        setMaxStackSize(1)
    }

    override fun getEntityLifespan(itemStack: ItemStack?, world: World?): Int {
        return Integer.MAX_VALUE
    }
}
