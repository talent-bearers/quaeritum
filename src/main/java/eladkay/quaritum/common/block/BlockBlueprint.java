package eladkay.quaritum.common.block;

import amerifrance.guideapi.api.IGuideLinked;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.page.PageText;
import eladkay.quaritum.api.lib.LibBook;
import eladkay.quaritum.api.lib.LibMisc;
import eladkay.quaritum.api.lib.LibNBT;
import eladkay.quaritum.api.util.ItemNBTHelper;
import eladkay.quaritum.client.core.TooltipHelper;
import eladkay.quaritum.common.Quaritum;
import eladkay.quaritum.common.block.base.BlockMod;
import eladkay.quaritum.common.block.base.ItemModBlock;
import eladkay.quaritum.common.block.tile.TileEntityBlueprint;
import eladkay.quaritum.common.book.ModBook;
import eladkay.quaritum.common.lib.LibLocations;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockBlueprint extends BlockMod implements IGuideLinked, ITileEntityProvider {

    public static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0, 0, 0, 1, 0.5f, 1);

    public BlockBlueprint(String name) {
        super(name, Material.PISTON);
        setHardness(1.2f);
    }

    @Nullable
    @Override
    protected ItemBlock createItemBlock() {
        ItemBlock item = new ItemModBlock(this);
        item.addPropertyOverride(LibLocations.FLAT_CHALK, new IItemPropertyGetter() {
            @Override
            public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entityLivingBase) {
                return ItemNBTHelper.getBoolean(stack.copy(), LibNBT.FLAT, false) ? 1.0f : 0.0f;
            }
        });
        return item;
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
        //Quaritum.proxy.spawnStafflikeParticles(worldIn, pos.getX(), pos.getY() + 1, pos.getZ());
        TileEntity tile = worldIn.getTileEntity(pos);
        return tile instanceof TileEntityBlueprint && ((TileEntityBlueprint) tile).onBlockActivated();
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        if(((World)world).isBlockPowered(pos)) {
            TileEntity tile = world.getTileEntity(pos);
            if(tile instanceof TileEntityBlueprint) ((TileEntityBlueprint) tile).onBlockActivated();
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        if(worldIn.isBlockPowered(pos)) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if(tile instanceof TileEntityBlueprint) ((TileEntityBlueprint) tile).onBlockActivated();
        }
    }

    @Override
    public ResourceLocation getLinkedEntry(World world, BlockPos pos, EntityPlayer player, ItemStack stack) {
        return new ResourceLocation(LibMisc.MOD_ID, LibBook.ENTRY_BLUEPRINT_NAME);
    }
    public static List<IPage> pages = new ArrayList<>();

    @Override
    public void constructBook() {
        pages.add(new PageText(TooltipHelper.local(LibBook.ENTRY_BLUEPRINT_PAGE1)));
        ModBook.register(ModBook.pagesAnimus, LibBook.ENTRY_BLUEPRINT_NAME, pages, new ItemStack(ModBlocks.blueprint));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBlueprint();
    }
}
