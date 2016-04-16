package eladkay.quaritum.common.block.tile;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.IRitual;
import eladkay.quaritum.api.rituals.IRitualRunner;
import eladkay.quaritum.api.rituals.RitualRegistry;
import eladkay.quaritum.api.rituals.RunHelper;
import eladkay.quaritum.common.lib.LibMisc;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TileEntityBlueprint extends TileEntity implements IInventory, IRitualRunner, ITickable {

    public ArrayList<ItemStack> items = Lists.newArrayList();

    @Override
    public void update() {
       boolean dirty = false;
        if (!worldObj.isRemote) {
            List<EntityItem> eitems = worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(this.getPos(), getPos().add(1, 1, 1)));
            for (EntityItem item : eitems)
                if (!item.isDead && item.getEntityItem() != null) {
                    ItemStack stack = item.getEntityItem();
                    items.add(stack);
                    item.setDead();
                    dirty = true;
                }
        }
        if(dirty) markDirty();
    }

    @Override
    public IRitual getValidRitual(EntityPlayer player) {
        for (IRitual ritual : RitualRegistry.getRitualList()) {
            boolean foundAll = true;
            for (ItemStack stack : ritual.getRequiredItems()) {
                if (!items.contains(stack)) foundAll = false;
            }
            if (items.size() == 0) foundAll = false;
            if (foundAll && ritual.canRitualRun(this.getWorld(), player, pos, this)) {
                return ritual;
            }
        }
        return null;
    }

    @Override
    public boolean runRitual(IRitual ritual, EntityPlayer player) {
        if (ritual == null) return false;
        else {
            clear();
            return RunHelper.runRitual(ritual, this.getWorld(), pos, player);
        }
    }

    @Override
    public int getSizeInventory() {
        return LibMisc.INVENTORY_SIZE_BLUEPRINT;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return items.set(index, new ItemStack(items.get(index).getItem(), items.get(index).stackSize - 1, items.get(index).getMetadata()));
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return items.set(index, null);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        items.set(index, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return LibMisc.INVENTORY_SIZE_BLUEPRINT;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        items = Lists.newArrayList();
    }

    @Override
    public String getName() {
        return "tile.quaritum.blueprint";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString("Blueprint");
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        return runRitual(getValidRitual(playerIn), playerIn);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.items = Lists.newArrayList();

        NBTTagList nbttaglist = compound.getTagList("Items", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < this.items.size()) {
                this.items.set(j, ItemStack.loadItemStackFromNBT(nbttagcompound));
            }
        }

    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.items.size(); ++i) {
            if (this.items.get(i) != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) i);
                this.items.get(i).writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        compound.setTag("Items", nbttaglist);

    }
}
