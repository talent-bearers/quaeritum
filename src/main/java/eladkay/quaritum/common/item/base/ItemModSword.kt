package eladkay.quaritum.common.item.base

import eladkay.quaritum.api.lib.LibMisc
import eladkay.quaritum.client.core.TooltipHelper
import eladkay.quaritum.common.Quaritum
import eladkay.quaritum.common.core.CreativeTab
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemSword
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry

open class ItemModSword(private val bareName: String, material: Item.ToolMaterial, vararg variants: String) : ItemSword(material) {

    override val variants: Array<String>

    init {
        var variants = variants
        unlocalizedName = bareName
        CreativeTab.set(this)
        if (variants.size > 1)
            setHasSubtypes(true)

        if (variants.size == 0)
            variants = arrayOf(bareName)
        this.variants = variants
        /*try {
            ModelHandler.variantCache.add(this);
        } catch(NoClassDefFoundError server) {}*/
        Quaritum.proxy!!.addToVariantCache(this)
    }

    override val customMeshDefinition: ItemMeshDefinition?
        get() = null

    override fun setUnlocalizedName(unlocalizedName: String): Item {
        GameRegistry.register(this, ResourceLocation(LibMisc.MOD_ID, unlocalizedName))
        return super.setUnlocalizedName(unlocalizedName)
    }

    override fun getUnlocalizedName(stack: ItemStack?): String {
        val dmg = stack!!.itemDamage
        val variants = this.variants
        val name: String
        if (dmg >= variants.size) {
            name = this.bareName
        } else {
            name = variants[dmg]
        }

        return "item." + LibMisc.MOD_ID + ":" + name
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
        val variants = this.variants

        for (i in variants.indices) {
            subItems.add(ItemStack(itemIn, 1, i))
        }
    }

    companion object {

        fun tooltipIfShift(tooltip: MutableList<String>, r: Runnable) {
            TooltipHelper.tooltipIfShift(tooltip, r)
        }

        fun addToTooltip(tooltip: MutableList<String>, s: String, vararg format: Any) {
            TooltipHelper.addToTooltip(tooltip, s, *format)
        }

        fun local(s: String): String {
            return TooltipHelper.local(s)
        }
    }
}
