package eladkay.quaeritum.common.block.machine

import com.teamwizardry.librarianlib.common.base.block.BlockModContainer
import com.teamwizardry.librarianlib.common.base.block.TileMod
import com.teamwizardry.librarianlib.common.util.autoregister.TileRegister
import com.teamwizardry.librarianlib.common.util.saving.Save
import com.teamwizardry.librarianlib.common.util.saving.SaveMethodGetter
import com.teamwizardry.librarianlib.common.util.saving.SaveMethodSetter
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemStackHandler
import net.minecraftforge.items.wrapper.RangedWrapper
import java.util.*







/**
 * @author WireSegal
 * Created at 10:40 AM on 2/3/17.
 */
class BlockCrafter : BlockModContainer(LibNames.CRAFTER, Material.WOOD) {

    companion object {
        val POWERED: PropertyBool = PropertyBool.create("powered")
    }

    override fun createBlockState() = BlockStateContainer(this, POWERED)
    override fun getMetaFromState(state: IBlockState) = if (state.getValue(POWERED)) 1 else 0
    override fun getStateFromMeta(meta: Int): IBlockState = defaultState.withProperty(POWERED, meta != 0)

    override fun createTileEntity(world: World, state: IBlockState) = TileCrafter()

    fun updatePower(world: World, pos: BlockPos, state: IBlockState) {
        if (world.isRemote) return
        val isOn = state.getValue(POWERED)
        if (isOn xor world.isBlockPowered(pos)) {
            world.setBlockState(pos, defaultState.withProperty(POWERED, !isOn))
            if (!isOn) (world.getTileEntity(pos) as? TileCrafter)?.onPowered()
        }
    }

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block) = updatePower(worldIn, pos, state)
    override fun onBlockAdded(worldIn: World, pos: BlockPos, state: IBlockState) = updatePower(worldIn, pos, state)
    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) = updatePower(worldIn, pos, state)

    @TileRegister("crafter")
    class TileCrafter : TileMod() {
        private val internalHandler = object : ItemStackHandler(10) {
            override fun getStackLimit(slot: Int, stack: ItemStack?) = 1
            override fun onContentsChanged(slot: Int) {
                markDirty()
            }
        }
        var handler: ItemStackHandler
            @SaveMethodGetter("handler") get() = internalHandler
            @SaveMethodSetter("handler") set(value) {
                handler.deserializeNBT(value.serializeNBT())
            }

        @Save
        val enabled = BooleanArray(9) { true }

        val inputs = object : RangedWrapper(handler, 0, 9) {
            override fun insertItem(slot: Int, stack: ItemStack?, simulate: Boolean): ItemStack? {
                if ((slot + 0 < 9) || !enabled[slot]) return stack
                return super.insertItem(slot, stack, simulate)
            }
        }
        val output = RangedWrapper(handler, 9, 10)

        fun onPowered() {
            //todo implementation
        }

        private fun craft(fullCheck: Boolean): Boolean {
            if (fullCheck && !isFull())
                return false

            val craft = InventoryCrafting(object : Container() {
                override fun canInteractWith(player: EntityPlayer): Boolean {
                    return false
                }
            }, 3, 3)
            for (i in 0..8) {
                val stack = handler.getStackInSlot(i)

                if (stack == null || !enabled[i])
                    continue

                craft.setInventorySlotContents(i, stack.copy())
            }

            val recipes = CraftingManager.getInstance().recipeList
            for (recipe in recipes)
                if (recipe.matches(craft, worldObj)) {
                    handler.setStackInSlot(9, recipe.getCraftingResult(craft))

                    for (i in 0..8) {
                        val stack = handler.getStackInSlot(i) ?: continue

                        val container = stack.item.getContainerItem(stack)
                        handler.setStackInSlot(i, container)
                    }
                    return true
                }

            return false
        }

        fun isFull(): Boolean {
            return (0 until 9).none { enabled[it] && handler.getStackInSlot(it) == null }
        }

        private fun ejectAll() {
            (0 until 9).forEach {
                val stack = handler.getStackInSlot(it)
                if (stack != null) eject(stack)
                handler.setStackInSlot(it, null)
            }
        }

        fun canEject(): Boolean {
            val stateBelow = worldObj.getBlockState(pos.down())
            val blockBelow = stateBelow.block
            return blockBelow.isAir(stateBelow, worldObj, pos.down()) || stateBelow.getCollisionBoundingBox(worldObj, pos.down()) == null
        }

        fun eject(stack: ItemStack) {
            val item = EntityItem(worldObj, pos.x + 0.5, pos.y - 0.5, pos.z + 0.5, stack)
            item.motionX = 0.0
            item.motionY = 0.0
            item.motionZ = 0.0
            worldObj.spawnEntityInWorld(item)
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : Any> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return when (facing) {
                EnumFacing.NORTH,
                EnumFacing.SOUTH,
                EnumFacing.WEST,
                EnumFacing.EAST,
                EnumFacing.UP -> inputs
                EnumFacing.DOWN -> output
                else -> handler
            } as T
            return super.getCapability(capability, facing)
        }

        override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
            return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing)
        }
    }
}
