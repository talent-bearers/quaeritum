package eladkay.quaritum.common.block.base

import com.teamwizardry.librarianlib.common.base.block.BlockMod
import eladkay.quaritum.common.lib.capitalizeFirst
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.EnumDyeColor

open class BlockModColored(name: String, materialIn: Material) : BlockMod(name, materialIn, *BlockModColored.generateVariants(name)) {

    init {
        this.defaultState = this.blockState.baseState.withProperty(COLOR, EnumDyeColor.WHITE)
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
    }

    override fun damageDropped(state: IBlockState?): Int {
        return state!!.getValue(COLOR).metadata
    }

    override fun getMapColor(state: IBlockState?): MapColor {
        return state!!.getValue(COLOR).mapColor
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return this.defaultState.withProperty(COLOR, EnumDyeColor.byMetadata(meta))
    }

    override fun getMetaFromState(state: IBlockState?): Int {
        return state!!.getValue(COLOR).metadata
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, COLOR)
    }

    companion object {
        val COLOR = PropertyEnum.create<EnumDyeColor>("color", EnumDyeColor::class.java)

        fun generateVariants(name: String): Array<String> {
            val ret = arrayOf<String>()
            for (dye in EnumDyeColor.values())
                ret[dye.ordinal] = name + dye.toString().capitalizeFirst()
            return ret
        }
    }

}
