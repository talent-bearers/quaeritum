package eladkay.quaritum.common.compat.waila;

import eladkay.quaritum.common.block.BlockBlueprint;
import eladkay.quaritum.common.block.chalk.BlockChalk;
import eladkay.quaritum.common.block.tile.TileEntityBlueprint;
import eladkay.quaritum.common.item.ModItems;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class Waila {
    public static void onWailaCall(IWailaRegistrar registrar) {
        System.out.println("Quaritum | Waila compat");
        try {
            registrar.registerStackProvider(new ChalkStackProvider(), BlockChalk.class);
            registrar.registerBodyProvider(new BlueprintBodyProvider(), BlockBlueprint.class);
            registrar.registerNBTProvider(new BlueprintBodyProvider(), BlockBlueprint.class);
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

    public static class BlueprintBodyProvider implements IWailaDataProvider {

        @Override
        public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
            return null;
        }

        @Override
        public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            //currenttip.addAll(((TileEntityBlueprint) accessor.getTileEntity()).items.stream().map(stack -> "Item: " + stack.getItem()).collect(Collectors.toList()));
            ItemStack[] stack = new ItemStack[]{};
            NBTTagList taglist = (NBTTagList) accessor.getNBTData().getTag("Inventory");

            for (int i = 0; i < taglist.tagCount(); i++) {
                NBTTagCompound tag = (NBTTagCompound) taglist.get(i);

                byte slot = tag.getByte("Slot");

                if (slot >= 0 && slot < stack.length) {
                    stack[slot] = ItemStack.loadItemStackFromNBT(tag);
                }
            }
            for (ItemStack stacks : stack) currenttip.add("Item: " + stacks.getItem());
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
            NBTTagList itemlist = new NBTTagList();

            for (int i = 0; i < ((TileEntityBlueprint) te).items.size(); i++) {
                ItemStack itemstack = ((TileEntityBlueprint) te).items.get(i);

                if (itemstack != null) {
                    NBTTagCompound tag2 = new NBTTagCompound();
                    tag2.setByte("Slot", (byte) i);
                    itemstack.writeToNBT(tag2);
                    itemlist.appendTag(tag2);
                }
            }
            /*for(ItemStack item : ((TileEntityBlueprint) te).items) {
                item.writeToNBT(tag);
            }*/
            tag.setTag("Inventory", itemlist);
            return tag;
        }

    }
}
