package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.core.common.OreDictionaryRegistrar
import com.teamwizardry.librarianlib.features.base.item.ItemModFood
import net.minecraft.init.MobEffects
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect

/**
 * @author WireSegal
 * Created at 8:22 PM on 12/16/17.
 */
class ItemOpium : ItemModFood("flower_dust", 3, 0.6f) {
    init {
        setPotionEffect(PotionEffect(MobEffects.NAUSEA, 400), 0.9f)
        setAlwaysEdible()
        OreDictionaryRegistrar.registerOre("dustOpium", this)
    }

    override fun getItemBurnTime(itemStack: ItemStack): Int {
        return 400
    }
}
