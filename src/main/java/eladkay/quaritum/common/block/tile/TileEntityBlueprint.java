package eladkay.quaritum.common.block.tile;

import com.google.common.collect.Lists;
import eladkay.quaritum.api.rituals.IDiagram;
import eladkay.quaritum.api.rituals.RitualRegistry;
import eladkay.quaritum.common.core.PositionedBlockHelper;
import eladkay.quaritum.common.lib.LibMisc;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TileEntityBlueprint extends TileMod implements IInventory {
    public boolean debug = true;
    public ArrayList<ItemStack> items = Lists.newArrayList();
    public boolean debug2 = true;

    public static boolean matches(List<ItemStack> items, List<ItemStack> required) {
        List<Object> inputsMissing = new ArrayList<>(required);
        for (ItemStack i : items) {
            for (int j = 0; j < inputsMissing.size(); j++) {
                Object inp = inputsMissing.get(j);
                if (inp instanceof ItemStack && ((ItemStack) inp).getItemDamage() == 32767)
                    ((ItemStack) inp).setItemDamage(i.getItemDamage());
                if (itemEquals(i, inp)) {
                    inputsMissing.remove(j);
                    break;
                }
            }
        }
        return inputsMissing.isEmpty();
    }

    private static boolean simpleAreStacksEqual(ItemStack stack, ItemStack stack2) {
        return stack.getItem() == stack2.getItem() && stack.getItemDamage() == stack2.getItemDamage();
    }

    private static boolean itemEquals(ItemStack stack, Object stack2) {
        if (stack2 instanceof String) {

            for (ItemStack orestack : OreDictionary.getOres((String) stack2)) {
                ItemStack cstack = orestack.copy();

                if (cstack.getItemDamage() == 32767) cstack.setItemDamage(stack.getItemDamage());
                if (stack.isItemEqual(cstack)) return true;
            }

        } else return stack2 instanceof ItemStack && simpleAreStacksEqual(stack, (ItemStack) stack2);
        return false;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        boolean dirty = false;
        if (!worldObj.isRemote) {
            List<EntityItem> eitems = worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(this.getPos(), getPos().add(1, 1, 1)));
            for (EntityItem item : eitems)
                if (!item.isDead) {
                    ItemStack stack = item.getEntityItem();
                    items.add(stack);
                    item.setDead();
                    dirty = true;
                }
        }
        if (dirty) markDirty();
    }

    private IDiagram getValidRitual(EntityPlayer player) {
        for (IDiagram ritual : RitualRegistry.getDiagramList()) {
            boolean foundAll = matches(items, ritual.getRequiredItems());
            boolean requirementsMet = ritual.canRitualRun(this.getWorld(), player, pos, this);
            boolean chalks = PositionedBlockHelper.isChalkSetupValid(ritual.buildChalks(Lists.newArrayList()), this, ritual.getUnlocalizedName());
            if (foundAll && requirementsMet && chalks) {
                return ritual;
            } else if (!requirementsMet) {
                System.out.println("REQUIREMENTS FOR RITUAL " + ritual.getUnlocalizedName() + " WERE NOT MET.");
            } else if (!foundAll) {
                System.out.println("ITEM REQUIREMENTS FOR RITUAL " + ritual.getUnlocalizedName() + " WERE NOT MET. REQUIREMENTS ARE " + ritual.getRequiredItems() + " & THERE IS " + items);
            } else {
                System.out.println("CHALK REQUIREMENTS FOR RITUAL " + ritual.getUnlocalizedName() + " WERE NOT MET. REQUIREMENTS ARE " + ritual.getRequiredItems() + " & THERE IS " + items);
            }
        }
        return null;
    }

    private boolean runRitual(IDiagram ritual, EntityPlayer player) {
        if (ritual != null) {
            boolean flag;
            if (flag = ritual.run(worldObj, player, pos, this, items))
                clear();
            return flag;
        } else
            return false;


    }

    private boolean checkItems(List container, List required) {
        if (container.size() != required.size()) return false;
        for (int i = 0; i < container.size(); i++) {
            if (container.get(i) != required.get(i)) return false;
        }
        return true;
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
    public boolean isUseableByPlayer(@Nonnull EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {
    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
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

    @Nonnull
    @Override
    public String getName() {
        return "tile.quaritum.blueprint";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString("Blueprint");
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        return runRitual(getValidRitual(playerIn), playerIn);
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
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

   /* @Override
    protected SimpleItemStackHandler createItemHandler() {
        return new SimpleItemStackHandler(this, false) {
            @Override
            protected int getStackLimit(int slot, ItemStack stack) {
                return 1;
            }
        };
    }*/

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
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
