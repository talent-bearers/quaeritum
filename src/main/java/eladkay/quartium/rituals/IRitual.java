package eladkay.quartium.rituals;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public interface IRitual {

    String getUnlocalizedName();
    EnumRitualType getRitualType();
    EnumRitualDuration getRitualDuration();
    boolean runOnce(World world, EntityPlayer player, BlockPos pos);
    boolean runDurable(World world, EntityPlayer player, BlockPos pos);
    boolean canRitualRun(World world, EntityPlayer player, BlockPos pos, TileEntity tile);
    ArrayList<ItemStack> getRequiredItems();
    String getCanonicalName();
}
