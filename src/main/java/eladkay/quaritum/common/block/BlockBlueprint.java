package eladkay.quaritum.common.block;

import eladkay.quaritum.common.block.base.BlockModContainer;
import eladkay.quaritum.common.block.tile.TileEntityBlueprint;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockBlueprint extends BlockModContainer {

    public static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0, 0, 0, 1, 0.5f, 1);

    public BlockBlueprint(String name) {
        super(name, Material.PISTON);
        setHardness(1.2f);
    }

    @Nonnull
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileEntityBlueprint();
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullyOpaque(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, @Nonnull BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        //world, x, y, z, r, g, b, size, xspeed, yspeed, zspeed, maxage
        //Quartium.proxy.wispFX(worldIn, pos.getX(), pos.getY() + 1, pos.getZ(), 51, 153, 255, 0.1F);
        TileEntity tile = worldIn.getTileEntity(pos);
        return tile instanceof TileEntityBlueprint && ((TileEntityBlueprint) tile).onBlockActivated();
    }
}
