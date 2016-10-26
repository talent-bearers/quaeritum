package eladkay.quaritum.common.block.tile

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandlerModifiable
import net.minecraftforge.items.ItemStackHandler

abstract class TileSimpleInventory : TileMod() {

    protected var itemHandler = createItemHandler()

    override fun readCustomNBT(par1NBTTagCompound: NBTTagCompound) {
        itemHandler = createItemHandler()
        itemHandler.deserializeNBT(par1NBTTagCompound)
    }

    override fun writeCustomNBT(par1NBTTagCompound: NBTTagCompound) {
        par1NBTTagCompound.merge(itemHandler.serializeNBT())
    }

    abstract val sizeInventory: Int

    protected fun createItemHandler(): SimpleItemStackHandler {
        return SimpleItemStackHandler(this, true)
    }

    fun getItemHandler(): IItemHandlerModifiable {
        return itemHandler
    }

    override fun hasCapability(cap: Capability<*>, side: EnumFacing?): Boolean {
        return cap === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side)
    }

    override fun <T> getCapability(cap: Capability<T>, side: EnumFacing?): T {
        if (cap === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast<T>(itemHandler)
        return super.getCapability(cap, side)
    }

    /* Extension of ItemStackHandler that uses our own slot array, allows for control of writing,
       allows control over stack limits, and allows for itemstack-slot validation */
    protected class SimpleItemStackHandler(private val tile: TileSimpleInventory, private val allowWrite: Boolean) : ItemStackHandler(tile.sizeInventory) {

        override fun insertItem(slot: Int, stack: ItemStack?, simulate: Boolean): ItemStack? {
            if (allowWrite) {
                return super.insertItem(slot, stack, simulate)
            } else
                return stack
        }

        override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack? {
            if (allowWrite) {
                return super.extractItem(slot, amount, simulate)
            } else
                return null
        }

        public override fun onContentsChanged(slot: Int) {
            tile.markDirty()
        }
    }
}
