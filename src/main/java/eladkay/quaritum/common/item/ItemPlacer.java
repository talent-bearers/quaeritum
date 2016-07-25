package eladkay.quaritum.common.item;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.PositionedBlock;
import eladkay.quaritum.api.rituals.PositionedBlockChalk;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemPlacer extends ItemMod {
    public ItemPlacer() {
        super(LibNames.DEV_PLACER);
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.getBlockState(pos).getBlock() != ModBlocks.blueprint && worldIn.getBlockState(pos).getBlock() != ModBlocks.foundation) return EnumActionResult.FAIL;
        List<PositionedBlock> chalks = Lists.newArrayList();
        buildChalksForPlacer(chalks);
        for(PositionedBlock block : chalks)
            worldIn.setBlockState(pos.add(block.getPos()), block.getState());
        return EnumActionResult.SUCCESS;
    }

    public static void buildChalksForPlacer(List<PositionedBlock> chalks) {
        chalks.add(new PositionedBlockChalk(EnumDyeColor.PINK, new BlockPos(0, 0, -2)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(-1, 0, -1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(0, 0, -1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(1, 0, -1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.PINK, new BlockPos(-2, 0, 0)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(-1, 0, 0)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(1, 0, 0)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.PINK, new BlockPos(2, 0, 0)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(-1, 0, 1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(0, 0, 1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.WHITE, new BlockPos(1, 0, 1)));
        chalks.add(new PositionedBlockChalk(EnumDyeColor.PINK, new BlockPos(0, 0, 2)));
    }
}
