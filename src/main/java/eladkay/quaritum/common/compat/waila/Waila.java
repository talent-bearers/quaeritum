package eladkay.quaritum.common.compat.waila;

import eladkay.quaritum.common.block.chalk.BlockChalk;
import eladkay.quaritum.common.item.ModItems;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class Waila {
    public static void onWailaCall(IWailaRegistrar registrar) {
        System.out.println("Quaritum | Waila compat");
        try {
            registrar.registerStackProvider(new ChalkStackProvider(), BlockChalk.class);
        } catch (NullPointerException e) {
            System.out.println("An error occured during Waila compat init!");
            e.printStackTrace();
        }

    }

    public static class ChalkStackProvider implements IWailaDataProvider {
        @Override
        public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
            return new ItemStack(ModItems.chalk, 1, accessor.getMetadata());
        }

        @Override
        public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            return currenttip;
        }

        @Override
        public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            return currenttip;
        }

        @Override
        public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            return currenttip;
        }

        @Override
        public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
            return tag;
        }
    }
}
