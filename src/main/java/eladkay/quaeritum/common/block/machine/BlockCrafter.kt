package eladkay.quaeritum.common.block.machine

import com.teamwizardry.librarianlib.features.autoregister.TileRegister
import com.teamwizardry.librarianlib.features.base.block.tile.BlockModContainer
import com.teamwizardry.librarianlib.features.base.block.tile.TileMod
import com.teamwizardry.librarianlib.features.base.block.tile.module.ModuleInventory
import com.teamwizardry.librarianlib.features.saving.Module
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler

/**
 * @author WireSegal
 * Created at 10:40 AM on 2/3/17.
 */
class BlockCrafter : BlockModContainer(LibNames.CRAFTER, Material.WOOD) {

    companion object {
        val FACING: PropertyDirection = PropertyDirection.create("facing") { it in EnumFacing.HORIZONTALS }
    }

    override fun getStateForPlacement(world: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase, hand: EnumHand): IBlockState {
        return defaultState.withProperty(FACING, placer.horizontalFacing.opposite)
    }

    override fun createBlockState() = BlockStateContainer(this, FACING)
    override fun getMetaFromState(state: IBlockState) = state.getValue(FACING).index
    override fun getStateFromMeta(meta: Int): IBlockState = defaultState.withProperty(FACING, EnumFacing.getFront(meta))

    override fun createTileEntity(world: World, state: IBlockState) = TileCrafter()

    @TileRegister("crafter")
    class TileCrafter : TileMod() {
        @Module
        val inventory = ModuleInventory(object : ItemStackHandler(1) {
            override fun getSlotLimit(slot: Int): Int {
                return 1
            }
        }).setSides()

        @Suppress("UNCHECKED_CAST")
        override fun <T : Any> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
            val normal = world.getBlockState(pos).getValue(FACING)

            return if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY &&
                    (facing != null && facing.axis == normal.axis))
                (if (normal == facing) outputInventory else inventory.handler) as T
            else super.getCapability(capability, facing)
        }

        override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
            val normal = world.getBlockState(pos).getValue(FACING)

            return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY &&
                    (facing != null && facing.axis == normal.axis))
                    || super.hasCapability(capability, facing)
        }

        private val outputInventory = OutputInventory()

        private inner class OutputInventory : IItemHandler {
            fun extractRecipe(amount: Int, take: Boolean): ItemStack {
                val inventoryCrafting = InventoryCrafting(object : Container() {
                    override fun canInteractWith(playerIn: EntityPlayer): Boolean {
                        return true
                    }
                }, 3, 3)

                val normal = world.getBlockState(pos).getValue(FACING)
                val onPlane = normal.rotateYCCW()

                for (y in -1..1) for (perpendicular in -1..1) {
                    val row = y + 1
                    val column = perpendicular + 1

                    val idx = row + column * 3

                    val tile = world.getTileEntity(pos.offset(EnumFacing.UP, y).offset(onPlane, perpendicular))
                    if (tile is TileCrafter) {
                        val stack = tile.inventory.handler.getStackInSlot(0)
                        inventoryCrafting.setInventorySlotContents(idx, stack.copy())
                    }
                }

                val recipe = CraftingManager.findMatchingRecipe(inventoryCrafting, world)
                if (recipe != null) {
                    val output = recipe.getCraftingResult(inventoryCrafting)
                    if (output.count <= amount) {
                        if (take) {
                            val remainder = recipe.getRemainingItems(inventoryCrafting)

                            for (y in -1..1) for (perpendicular in -1..1) {
                                val row = y + 1
                                val column = perpendicular + 1

                                val idx = row + column * 3

                                val tile = world.getTileEntity(pos.offset(EnumFacing.UP, y).offset(onPlane, perpendicular))
                                (tile as? TileCrafter)?.inventory?.handler?.setStackInSlot(0, remainder[idx])
                            }
                        }
                        return output
                    }
                }

                return ItemStack.EMPTY
            }

            override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
                return stack
            }

            override fun getStackInSlot(slot: Int): ItemStack {
                return ItemStack.EMPTY
            }

            override fun getSlotLimit(slot: Int): Int {
                return 64
            }

            override fun getSlots(): Int {
                return 1
            }

            override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
                return extractRecipe(amount, simulate)
            }
        }
    }
}
