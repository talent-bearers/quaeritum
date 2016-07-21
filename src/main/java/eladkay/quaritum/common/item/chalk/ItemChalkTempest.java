package eladkay.quaritum.common.item.chalk;

import eladkay.quaritum.api.lib.LibNBT;
import eladkay.quaritum.api.util.ItemNBTHelper;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibLocations;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemChalkTempest extends ItemMod {
    public ItemChalkTempest() {
        super(LibNames.CHALK_TEMPEST);
        addPropertyOverride(LibLocations.FLAT_CHALK, new IItemPropertyGetter() {
            @Override
            public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entityLivingBase) {
                return ItemNBTHelper.getBoolean(stack.copy(), LibNBT.FLAT, false) ? 1.0f : 0.0f;
            }
        });
        setMaxStackSize(1);
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState iblockstate = world.getBlockState(blockPos);
        Block block = iblockstate.getBlock();

        if (!block.isReplaceable(world, blockPos)) {
            blockPos = blockPos.offset(side);
        }

        if (stack.stackSize != 0 && player.canPlayerEdit(blockPos, side, stack) && world.canBlockBePlaced(ModBlocks.tempest, blockPos, false, side, null, stack)) {
            int i = stack.getMetadata();
            IBlockState iblockstate1 = ModBlocks.tempest.onBlockPlaced(world, blockPos, side, hitX, hitY, hitZ, i, player);

            if (placeBlockAt(stack, player, world, blockPos, side, hitX, hitY, hitZ, iblockstate1)) {
                SoundType soundtype = ModBlocks.tempest.getSoundType();
                world.playSound(player, blockPos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            }

            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }

    private boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (!world.setBlockState(pos, newState, 3)) return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == ModBlocks.tempest) {
            ItemBlock.setTileEntityNBT(world, player, pos, stack);
            ModBlocks.tempest.onBlockPlacedBy(world, pos, state, player, stack);
        }

        return true;
    }
}
