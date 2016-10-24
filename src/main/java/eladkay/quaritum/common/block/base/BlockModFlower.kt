package eladkay.quaritum.common.block.base

import com.teamwizardry.librarianlib.common.base.block.ItemModBlock
import eladkay.quaritum.common.Quaritum
import eladkay.quaritum.common.core.CreativeTab
import net.minecraft.block.Block
import net.minecraft.block.BlockBush
import net.minecraft.block.material.Material
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 * @author WireSegal
 * *         Created at 4:18 PM on 4/30/16.
 */
abstract class BlockModFlower(override val bareName: String, materialIn: Material, vararg variants: String) : BlockBush(materialIn) {

    override var variants: Array<String> = null
        private set

    init {
        this.variants = variants
        if (variants.size == 0) {
            this.variants = arrayOf(bareName)
        }
        this.setUnlocalizedName(bareName)

        if (shouldRegisterInCreative())
            CreativeTab.set(this)
    }

    fun shouldRegisterInCreative(): Boolean {
        return true
    }

    fun shouldHaveItemForm(): Boolean {
        return true
    }

    override fun setUnlocalizedName(name: String): Block {
        super.setUnlocalizedName(name)
        setRegistryName(name)
        GameRegistry.register(this)
        if (shouldHaveItemForm())
            GameRegistry.register(ItemModBlock(this)/*, new ResourceLocation(LibMisc.MOD_ID, name)*/)
        else
            Quaritum.proxy!!.addToVariantCache(this)
        //ModelHandler.variantCache.add(this);

        return this
    }


    override fun getBlockRarity(stack: ItemStack): EnumRarity {
        return EnumRarity.COMMON
    }
}
