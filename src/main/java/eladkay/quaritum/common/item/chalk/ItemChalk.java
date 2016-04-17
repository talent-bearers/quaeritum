package eladkay.quaritum.common.item.chalk;

import eladkay.quaritum.client.core.ModelHandler;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemChalk extends ItemMod implements ModelHandler.IColorProvider {
    public static final String[] COLORS = {};

    static {
        for (EnumDyeColor dye : EnumDyeColor.values())
            COLORS[dye.ordinal()] = "chalk" + capitalizeFirst(dye.toString());
        COLORS[COLORS.length] = "chalkTempest";

    }

    public ItemChalk() {
        super(LibNames.CHALK, COLORS);
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
        BlockPos newPos = blockPos.offset(side);

        if (world.isAirBlock(newPos)) {
            if (!world.isRemote) {
                world.setBlockState(newPos, );
            }

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }

    @SideOnly(Side.CLIENT)
    public IItemColor getColor() {
        return new IItemColor() {
            @Override
            public int getColorFromItemstack(ItemStack stack, int tintIndex) {
                return tintIndex == 1 && stack.getItemDamage() < 16 ? ItemDye.dyeColors[15 - stack.getItemDamage()] : 0xFFFFFF;
            }
        };
    }
}
