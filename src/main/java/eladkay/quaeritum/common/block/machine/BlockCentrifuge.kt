package eladkay.quaeritum.common.block.machine

import com.teamwizardry.librarianlib.features.autoregister.TileRegister
import com.teamwizardry.librarianlib.features.base.block.BlockModContainer
import com.teamwizardry.librarianlib.features.base.block.TileMod
import com.teamwizardry.librarianlib.features.saving.Save
import com.teamwizardry.librarianlib.features.saving.SaveMethodGetter
import com.teamwizardry.librarianlib.features.saving.SaveMethodSetter
import eladkay.quaeritum.api.machines.CentrifugeRecipes
import eladkay.quaeritum.common.core.QuaeritumSoundEvents
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.BlockHorizontal
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.Blocks
import net.minecraft.inventory.InventoryHelper
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler
import net.minecraftforge.items.wrapper.RangedWrapper

/**
 * @author WireSegal
 * Created at 9:24 AM on 2/1/17.
 */
class BlockCentrifuge : BlockModContainer(LibNames.CENTRIFUGE, Material.CLOTH) {

    companion object {
        val FACING: PropertyDirection = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL)
    }

    override fun createBlockState() = BlockStateContainer(this, FACING)

    override fun withRotation(state: IBlockState, rot: Rotation): IBlockState
            = state.withProperty(FACING, rot.rotate(state.getValue(FACING)))
    override fun withMirror(state: IBlockState, mirrorIn: Mirror): IBlockState
            = state.withRotation(mirrorIn.toRotation(state.getValue(FACING)))

    override fun getStateFromMeta(meta: Int): IBlockState
            = defaultState.withProperty(FACING, EnumFacing.getHorizontal(meta))
    override fun getMetaFromState(state: IBlockState)
            = state.getValue(FACING).horizontalIndex

    override fun getStateForPlacement(world: World?, pos: BlockPos?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase, hand: EnumHand?): IBlockState
         = defaultState.withProperty(BlockHorizontal.FACING, placer.horizontalFacing)

    override fun hasComparatorInputOverride(state: IBlockState) = true
    override fun getComparatorInputOverride(blockState: IBlockState, worldIn: World, pos: BlockPos): Int {
        return (worldIn.getTileEntity(pos) as? TileCentrifuge)?.getComparatorOutput() ?: 0
    }

    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
        val handler = (worldIn.getTileEntity(pos) as? TileCentrifuge)?.handler
        if (handler != null)
            dropInventoryItems(worldIn, pos, handler)
        super.breakBlock(worldIn, pos, state)
    }

    fun dropInventoryItems(worldIn: World, pos: BlockPos, inventory: IItemHandler) {
        dropInventoryItems(worldIn, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), inventory)
    }

    private fun dropInventoryItems(worldIn: World, x: Double, y: Double, z: Double, inventory: IItemHandler) {
        (0 until inventory.slots)
                .mapNotNull { inventory.getStackInSlot(it) }
                .forEach { InventoryHelper.spawnItemStack(worldIn, x, y, z, it) }
    }

    override fun createTileEntity(world: World, state: IBlockState) = TileCentrifuge()

    private class CentrifugeHandler(val tile: TileMod) : ItemStackHandler(3) {
        override fun onContentsChanged(slot: Int) {
            tile.markDirty()
        }

        public override fun getStackLimit(slot: Int, stack: ItemStack): Int {
            return super.getStackLimit(slot, stack)
        }
    }

    @TileRegister("centrifuge")
    class TileCentrifuge : TileMod(), ITickable {

        private val internalHandler = CentrifugeHandler(this)
        var handler: ItemStackHandler
            @SaveMethodGetter("handler") get() = internalHandler
            @SaveMethodSetter("handler") set(value) {
                handler.deserializeNBT(value.serializeNBT())
            }

        val inputs = RangedWrapper(handler, 0, 2)
        val output = RangedWrapper(handler, 2, 3)

        fun getComparatorOutput(): Int {
            val unfloored = ((0 until internalHandler.slots)
                    .flatMap {
                        val stack = internalHandler.getStackInSlot(it)
                        if (stack.isEmpty) listOf()
                        else listOf(IndexedValue(it, stack))
                    }.sumByDouble {
                        it.value.count / Math.min(internalHandler.getStackLimit(it.index, it.value), it.value.maxStackSize).toDouble()
                    } / internalHandler.slots.toFloat()) * 14.0
            val floored = Math.floor(unfloored).toInt()

            return floored + if (unfloored > 0.0) 1 else 0
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : Any> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return when (facing) {
                EnumFacing.UP -> inputs
                EnumFacing.DOWN,
                EnumFacing.NORTH,
                EnumFacing.SOUTH,
                EnumFacing.WEST,
                EnumFacing.EAST -> output
                else -> handler
            } as T
            return super.getCapability(capability, facing)
        }

        override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
            return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing)
        }

        @Save var totalSteam = 0

        override fun update() {
            if (world.isRemote) return

            val heated = world.getBlockState(pos.down()).block == Blocks.FIRE
            val recipe = CentrifugeRecipes.getRecipe(inputs, heated)
            if (recipe != null) {
                if (totalSteam >= recipe.steamRequired(inputs, heated)) {
                    if (output.insertItem(0, recipe.getOutput(inputs, heated), false).isEmpty) {
                        recipe.consumeInputs(inputs, heated)
                        totalSteam = 0
                    }
                } else {
                    totalSteam += 20 // temporary
                    if (totalSteam % 50 == 0)
                        world.playSound(null, pos, QuaeritumSoundEvents.centrifuge, SoundCategory.BLOCKS, 1f, 5f)
                }

                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, 0.0, 0.0, 0.0)
                markDirty()
            } else if (totalSteam != 0) {
                totalSteam = 0
                markDirty()
            }
        }
    }
}
