package eladkay.quaeritum.common.item.soulstones

import com.teamwizardry.librarianlib.common.base.item.ItemMod
import eladkay.quaeritum.common.lib.LibNames
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
