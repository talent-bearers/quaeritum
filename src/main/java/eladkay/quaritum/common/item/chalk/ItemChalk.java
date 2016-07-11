package eladkay.quaritum.common.item.chalk;

import eladkay.quaritum.api.lib.LibNBT;
import eladkay.quaritum.api.util.ItemNBTHelper;
import eladkay.quaritum.client.core.ModelHandler;
import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibLocations;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemChalk extends ItemMod implements ModelHandler.IColorProvider, ModelHandler.ICustomLogHolder {
    private static final String[] COLORS = new String[17];

    static {
        for (EnumDyeColor dye : EnumDyeColor.values())
            COLORS[dye.ordinal()] = "chalk" + (capitalizeFirst(dye.toString())).replace("Silver", "LightGray");
        COLORS[16] = "chalkTempest";
    }

    public ItemChalk() {
        super(LibNames.CHALK, COLORS);
        addPropertyOverride(LibLocations.FLAT_CHALK, (stack, world, entityLivingBase) -> ItemNBTHelper.getBoolean(stack.copy(), LibNBT.FLAT, false) ? 1.0f : 0.0f);
        setMaxStackSize(1);
    }

    public static String capitalizeFirst(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static EnumDyeColor byMetadata(int meta) {
        if (meta == 16) return null;
        return EnumDyeColor.byMetadata(meta);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState iblockstate = world.getBlockState(blockPos);
        Block block = iblockstate.getBlock();

        if (!block.isReplaceable(world, blockPos)) {
            blockPos = blockPos.offset(side);
        }

        if (stack.stackSize != 0 && player.canPlayerEdit(blockPos, side, stack) && world.canBlockBePlaced(ModBlocks.chalk, blockPos, false, side, null, stack)) {
            int i = stack.getMetadata();
            IBlockState iblockstate1 = ModBlocks.chalk.onBlockPlaced(world, blockPos, side, hitX, hitY, hitZ, i, player);

            if (placeBlockAt(stack, player, world, blockPos, side, hitX, hitY, hitZ, iblockstate1)) {
                SoundType soundtype = ModBlocks.chalk.getSoundType();
                world.playSound(player, blockPos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            }

            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }

    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (!world.setBlockState(pos, newState, 3)) return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == ModBlocks.chalk) {
            ItemBlock.setTileEntityNBT(world, player, pos, stack);
            ModBlocks.chalk.onBlockPlacedBy(world, pos, state, player, stack);
        }

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IItemColor getColor() {
        return new IItemColor() {
            @Override
            public int getColorFromItemstack(ItemStack stack, int tintIndex) {
                return ItemNBTHelper.getBoolean(stack, LibNBT.FLAT, false) && stack.getItemDamage() < 16 ? ItemDye.DYE_COLORS[15- stack.getItemDamage()] : 0xFFFFFF;
            }
        };
    }

    @Override
    public String customLog() {
        return ModelHandler.modlength + " |  Variants by dye color";
    }

    @Override
    public String customLogVariant(int variantID, String variant) {
        return "";
    }

    @Override
    public int sortingPrecedence() {
        return 1;
    }

    @Override
    public boolean shouldLogForVariant(int variantID, String variant) {
        return false;
    }
}
