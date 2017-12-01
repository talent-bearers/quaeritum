package eladkay.quaeritum.common.block.machine

import com.teamwizardry.librarianlib.features.autoregister.TileRegister
import com.teamwizardry.librarianlib.features.base.block.tile.BlockModContainer
import com.teamwizardry.librarianlib.features.base.block.tile.TileModTickable
import com.teamwizardry.librarianlib.features.base.block.tile.module.ModuleFluid
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.helpers.nonnullListOf
import com.teamwizardry.librarianlib.features.kotlin.getTileEntitySafely
import com.teamwizardry.librarianlib.features.saving.Module
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper
import eladkay.quaeritum.api.lib.LibMisc
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.NonNullList
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * @author WireSegal
 * Created at 7:37 PM on 11/27/17.
 */
class BlockFluidHolder : BlockModContainer("fluid_holder", Material.GLASS) {
    init {
        setHardness(1f)
    }

    companion object {
        val AABB = AxisAlignedBB(2 / 16.0, 0.0, 2 / 16.0, 14 / 16.0, 1.0, 14 / 16.0)

        val UP: PropertyBool = PropertyBool.create("up")
        val DOWN: PropertyBool = PropertyBool.create("down")
    }

    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState {
        var retState = state
        if (worldIn.getBlockState(pos.down()).block != this)
            retState = retState.withProperty(DOWN, false)
        if (worldIn.getBlockState(pos.up()).block != this)
            retState = retState.withProperty(UP, false)
        return retState
    }

    override fun isFullCube(state: IBlockState) = false

    override fun isOpaqueCube(blockState: IBlockState) = false

    override fun getBlockFaceShape(world: IBlockAccess, state: IBlockState, pos: BlockPos, facing: EnumFacing): BlockFaceShape {
        if (facing.axis == EnumFacing.Axis.Y) return BlockFaceShape.MIDDLE_POLE_THICK
        return BlockFaceShape.UNDEFINED
    }

    override fun getMetaFromState(state: IBlockState?) = 0

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, UP, DOWN)
    }

    override fun getBlockLayer() = BlockRenderLayer.CUTOUT

    override fun getBoundingBox(state: IBlockState?, source: IBlockAccess?, pos: BlockPos?) = AABB

    override fun createTileEntity(world: World, state: IBlockState) = TileFluidColumn()

    @SideOnly(Side.CLIENT)
    override fun addInformation(stack: ItemStack, player: World?, tooltip: MutableList<String>, advanced: ITooltipFlag) {
        if (stack.hasTagCompound() && ItemNBTHelper.verifyExistence(stack, "fluid")) {
            val fluid = FluidStack.loadFluidStackFromNBT(ItemNBTHelper.getCompound(stack, "fluid")!!)
            if (fluid != null)
                TooltipHelper.addToTooltip(tooltip, "${LibMisc.MOD_ID}.hud.fluid", fluid.amount, fluid.localizedName)
        }
    }

    override fun onBlockPlacedBy(worldIn: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack) {
        if (stack.hasTagCompound() && ItemNBTHelper.verifyExistence(stack, "fluid")) {
            val fluid = FluidStack.loadFluidStackFromNBT(ItemNBTHelper.getCompound(stack, "fluid")!!)
            val te = worldIn.getTileEntity(pos)
            if (te is TileFluidColumn)
                te.fluid.handler.fluid = fluid
        }
    }

    override fun getDrops(drops: NonNullList<ItemStack>, world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int) {
        val drop = ItemStack(this)
        val tile = world.getTileEntitySafely(pos) as? TileFluidColumn ?: return super.getDrops(drops, world, pos, state, fortune)
        val fluid = tile.fluid.handler.fluid ?: return super.getDrops(drops, world, pos, state, fortune)
        ItemNBTHelper.setCompound(drop, "fluid", fluid.writeToNBT(NBTTagCompound()))
        drops.add(drop)
    }

    override fun getPickBlock(state: IBlockState, target: RayTraceResult, world: World, pos: BlockPos, player: EntityPlayer): ItemStack {
        return nonnullListOf<ItemStack>().apply { getDrops(this, world, pos, state, 0) }[0]
    }

    override fun removedByPlayer(state: IBlockState, world: World, pos: BlockPos, player: EntityPlayer, willHarvest: Boolean): Boolean {
        if (willHarvest) {
            onBlockHarvested(world, pos, state, player)
            return true
        } else {
            return super.removedByPlayer(state, world, pos, player, willHarvest)
        }
    }

    override fun harvestBlock(worldIn: World, player: EntityPlayer?, pos: BlockPos, state: IBlockState?, te: TileEntity?, stack: ItemStack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack)
        worldIn.setBlockToAir(pos)
    }


    @TileRegister("fluid_holder")
    class TileFluidColumn : TileModTickable() {
        @Module
        val fluid = ModuleFluid(4000)

        override fun getRenderBoundingBox(): AxisAlignedBB {
            return INFINITE_EXTENT_AABB
        }

        override fun tick() {
            for (i in listOf(EnumFacing.UP, EnumFacing.DOWN)) {
                val from = if (i == EnumFacing.DOWN) this else world.getTileEntity(pos.up())
                val target = if (i == EnumFacing.UP) this else world.getTileEntity(pos.down())
                if (from != null && target != null) {
                    val cap = from.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, i)
                    val targetCap = target.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, i.opposite)
                    if (cap != null && targetCap != null) {
                        val tentativeStack = cap.drain(4000, false)
                        if (tentativeStack != null) {
                            val succeeded = targetCap.fill(tentativeStack, false)
                            if (succeeded > 0) {
                                targetCap.fill(cap.drain(succeeded, true), true)
                                from.markDirty()
                                target.markDirty()
                            }
                        }
                    }
                }
            }
        }
    }
}
