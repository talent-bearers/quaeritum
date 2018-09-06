package eladkay.quaeritum.common.item.historic

import com.teamwizardry.librarianlib.features.base.item.IGlowingItem
import com.teamwizardry.librarianlib.features.base.item.IGlowingItem.Helper.wrapperBake
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import eladkay.quaeritum.common.entity.DeagAnamType
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * @author WireSegal
 * Created at 5:10 PM on 7/16/18.
 */
class ItemDeagAnam : ItemMod("deaganam"), IGlowingItem {
    companion object {
        fun getType(stack: ItemStack): DeagAnamType {
            if (stack.item is ItemDeagAnam) {
                val key = ItemNBTHelper.getString(stack, "type", "")
                for (type in DeagAnamType.values())
                    if (type.id == key)
                        return type
            }

            return DeagAnamType.VIBRANT
        }
    }

    @SideOnly(Side.CLIENT)
    override fun transformToGlow(itemStack: ItemStack, model: IBakedModel) = wrapperBake(model, false, 1)

    @SideOnly(Side.CLIENT)
    override fun shouldDisableLightingForGlow(itemStack: ItemStack, model: IBakedModel) = true
}
