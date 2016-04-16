package eladkay.quartium.rituals.blocks;

import eladkay.quartium.IModObject;
import eladkay.quartium.rituals.runners.diagram.TileEntityBlueprint;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockBlueprint extends Block implements IModObject {
    public static final String NAME = "blueprint";
    Random random = new Random();
    public BlockBlueprint() {
        super(Material.iron);
        this.setHardness(0.6f);
        //this.setBlockBounds(0, 0, 0, 1, 0.5f, 1);
        this.enableStats = true;
        this.stepSound = SoundType.STONE;
        this.blockParticleGravity = 1.0F;
        this.slipperiness = 0.6F;
        // this.setDefaultState(this.blockState.getBaseState());
        this.fullBlock = false;
        this.lightOpacity = 0;

    }

    //  @Override
    //  public boolean hasTileEntity(IBlockState state) {
    //      return true;
    //  }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
       return new AxisAlignedBB(0, 0, 0, 1, 0.5f, 1);
    }

    //  @Override
    //  public TileEntity createNewTileEntity(World worldIn, int meta) {
    //     return new TileEntityBlueprint();
    // }

    @Override
    public void breakBlock(World par1World, BlockPos pos, IBlockState state) {
        TileEntity inv = par1World.getTileEntity(pos);

        if (inv != null) {
            for (int j1 = 0; j1 < ((TileEntityBlueprint) inv).items.size(); ++j1) {
                ItemStack itemstack = ((IInventory) inv).getStackInSlot(j1);

                if (itemstack != null) {
                    float f = random.nextFloat() * 0.8F + 0.1F;
                    float f1 = random.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; par1World.spawnEntityInWorld(entityitem)) {
                        int k1 = random.nextInt(21) + 10;

                        if (k1 > itemstack.stackSize)
                            k1 = itemstack.stackSize;

                        itemstack.stackSize -= k1;
                        entityitem = new EntityItem(par1World, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (float) random.nextGaussian() * f3;
                        entityitem.motionY = (float) random.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = (float) random.nextGaussian() * f3;

                        if (itemstack.hasTagCompound())
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                    }
                }
            }

            par1World.notifyBlockOfStateChange(pos, state.getBlock());
        }

        super.breakBlock(par1World, pos, state);
    }
    @Override
    public String getUnlocalizedName() {
        return "diagram";
    }
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {

        TileEntity tile = worldIn.getTileEntity(pos);
        return tile instanceof TileEntityBlueprint && ((TileEntityBlueprint) tile).onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
    }
    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos,
                                     EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
                                     EntityLivingBase placer) {

        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
    }
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int[] getMetas() {
        return new int[]{0};
    }
}
