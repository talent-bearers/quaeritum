package eladkay.quaritum.common.block.base;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static eladkay.quaritum.common.item.chalk.ItemChalk.capitalizeFirst;

public class BlockModColored extends BlockMod {
    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

    public BlockModColored(String name, Material materialIn) {
        super(name, materialIn, generateVariants(name));
        this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public static String[] generateVariants(String name) {
        String[] ret = new String[16];
        for (EnumDyeColor dye : EnumDyeColor.values())
            ret[dye.ordinal()] = name + capitalizeFirst(dye.toString());
        return ret;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(COLOR).getMetadata();
    }

    @Override
    public MapColor getMapColor(IBlockState state) {
        return state.getValue(COLOR).getMapColor();
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(COLOR).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, COLOR);
    }

}
