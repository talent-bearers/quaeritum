package eladkay.quaritum.common.item.misc;

import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFertilizer extends ItemMod {
    public ItemFertilizer() {
        super(LibNames.FERTILIZER);
    }

    private static boolean applyBonemeal(ItemStack stack, World worldIn, BlockPos target) {
        return /*worldIn instanceof WorldServer && */applyBonemeal0(stack, worldIn, target);
    }

    private static boolean applyBonemeal0(ItemStack stack, World worldIn, BlockPos target) {
        IBlockState iblockstate = worldIn.getBlockState(target);
        if (iblockstate.equals(ModBlocks.flower.getStateFromMeta(BlockAnimusFlower.Variants.COMMON_ARCANE.ordinal()))) {
            if (!worldIn.isRemote) {
                if (Math.random() < 0.05)
                    worldIn.setBlockState(target, ModBlocks.flower.getStateFromMeta(BlockAnimusFlower.Variants.ARCANE.ordinal()));
                worldIn.playBroadcastSound(2005, target, 0);
                spawnBonemealParticles(worldIn, target, 0);
            }
            stack.stackSize--;
        }
        return true;
    }


    public static void spawnBonemealParticles(World worldIn, BlockPos pos, int amount) {
        if (amount == 0) {
            amount = 15;
        }

        IBlockState iblockstate = worldIn.getBlockState(pos);
        int i1;
        double d0;
        double d1;
        double d2;
        if (iblockstate.getMaterial() != Material.AIR) {
            for (i1 = 0; i1 < amount; ++i1) {
                d0 = itemRand.nextGaussian() * 0.02D;
                d1 = itemRand.nextGaussian() * 0.02D;
                d2 = itemRand.nextGaussian() * 0.02D;
                worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, (double) ((float) pos.getX() + itemRand.nextFloat()), (double) pos.getY() + (double) itemRand.nextFloat() * iblockstate.getBoundingBox(worldIn, pos).maxY, (double) ((float) pos.getZ() + itemRand.nextFloat()), d0, d1, d2);
            }
        } else {
            for (i1 = 0; i1 < amount; ++i1) {
                d0 = itemRand.nextGaussian() * 0.02D;
                d1 = itemRand.nextGaussian() * 0.02D;
                d2 = itemRand.nextGaussian() * 0.02D;
                worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, (double) ((float) pos.getX() + itemRand.nextFloat()), (double) pos.getY() + (double) itemRand.nextFloat() * 1.0D, (double) ((float) pos.getZ() + itemRand.nextFloat()), d0, d1, d2);
            }
        }

    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return applyBonemeal(stack, worldIn, pos) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
    }
}
