package eladkay.quaeritum.common.block.base

import com.teamwizardry.librarianlib.features.base.block.BlockMod
import eladkay.quaeritum.common.lib.arrayOfStrings
import eladkay.quaeritum.common.lib.capitalizeFirst
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.EnumDyeColor
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess

open class BlockModColored(name: String, materialIn: Material) : BlockMod(name, materialIn, *BlockModColored.generateVariants(name)) {

    init {
        this.defaultState = this.blockState.baseState.withProperty(COLOR, EnumDyeColor.WHITE)
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
    }

    override fun damageDropped(state: IBlockState): Int {
        return state.getValue(COLOR).metadata
    }

    override fun getMapColor(state: IBlockState, worldIn: IBlockAccess?, pos: BlockPos?): MapColor {
        return MapColor.getBlockColor(state.getValue(COLOR))
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
            val ret = arrayOfStrings(EnumDyeColor.values().size)
            for (dye in EnumDyeColor.values())
                ret[dye.ordinal] = name + dye.toString().capitalizeFirst()
            return ret
        }
    }

}
