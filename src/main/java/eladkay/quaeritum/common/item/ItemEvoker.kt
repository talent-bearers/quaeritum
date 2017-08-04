package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import eladkay.quaeritum.api.spell.EnumSpellElement
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.item.ItemStack

/**
 * @author WireSegal
 * Created at 10:01 PM on 8/3/17.
 */
class ItemEvoker : ItemMod(LibNames.SOUL_EVOKER) {
    companion object {
        fun getEvocationFromStack(stack: ItemStack): Array<EnumSpellElement> {
            if (stack.item !is ItemEvoker) return arrayOf()
            return arrayOf() // todo
        }
    }

    // todo
}
