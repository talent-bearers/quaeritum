package eladkay.quaritum.common.block.base

import com.teamwizardry.librarianlib.common.base.block.IModBlock
import com.teamwizardry.librarianlib.common.base.block.ItemModBlock
import com.teamwizardry.librarianlib.common.util.VariantHelper
import net.minecraft.block.Block
import net.minecraft.block.BlockBush
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.item.ItemBlock
import net.minecraftforge.fml.common.Loader

/**
 * @author WireSegal
 * *         Created at 4:18 PM on 4/30/16.
 */
abstract class BlockModFlower(name: String, materialIn: Material, color: MapColor, vararg variants: String) : BlockBush(materialIn, color), IModBlock {

    constructor(name: String, materialIn: Material, vararg variants: String) : this(name, materialIn, materialIn.materialMapColor, *variants)

    final override val variants: Array<out String>

    override val bareName: String = name
    val modId: String

    val itemForm: ItemBlock? by lazy { createItemForm() }

    init {
        modId = Loader.instance().activeModContainer().modId
        this.variants = VariantHelper.beginSetupBlock(name, variants)
        @Suppress("LeakingThis")
        VariantHelper.finishSetupBlock(this, name, itemForm, creativeTab)
    }

    override fun setUnlocalizedName(name: String): Block {
        super.setUnlocalizedName(name)
        VariantHelper.setUnlocalizedNameForBlock(this, modId, name, itemForm)
        return this
    }

    open fun createItemForm(): ItemBlock? {
        return ItemModBlock(this)
    }
}
