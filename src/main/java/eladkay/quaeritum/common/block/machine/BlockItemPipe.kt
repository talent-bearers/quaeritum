package eladkay.quaeritum.common.block.machine

import com.teamwizardry.librarianlib.features.autoregister.TileRegister
import com.teamwizardry.librarianlib.features.base.IExtraVariantHolder
import com.teamwizardry.librarianlib.features.base.block.IBlockColorProvider
import com.teamwizardry.librarianlib.features.base.block.tile.BlockModContainer
import com.teamwizardry.librarianlib.features.base.block.tile.TileModTickable
import com.teamwizardry.librarianlib.features.base.block.tile.module.ModuleInventory
import com.teamwizardry.librarianlib.features.base.block.tile.module.ModuleSlots
import com.teamwizardry.librarianlib.features.kotlin.getTileEntitySafely
import com.teamwizardry.librarianlib.features.kotlin.isNotEmpty
import com.teamwizardry.librarianlib.features.saving.Module
import com.teamwizardry.librarianlib.features.saving.NoSync
import com.teamwizardry.librarianlib.features.saving.Save
import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.api.util.RandUtil
import eladkay.quaeritum.common.block.machine.IColorAcceptor.Companion.tryPush
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
import net.minecraftforge.items.ItemHandlerHelper
import net.minecraftforge.items.ItemStackHandler

/**
 * @author WireSegal
 * Created at 12:59 PM on 1/16/18.
 */
class BlockItemPipe : BlockModContainer("pipe", Material.IRON, *EnumDyeColor.values().map { "pipe_${it.getName()}" }.toTypedArray()), IBlockColorProvider, IExtraVariantHolder {
    companion object {
        val UP: PropertyBool = PropertyBool.create("up")
        val DOWN: PropertyBool = PropertyBool.create("down")
        val WEST: PropertyBool = PropertyBool.create("west")
        val EAST: PropertyBool = PropertyBool.create("east")
        val NORTH: PropertyBool = PropertyBool.create("north")
        val SOUTH: PropertyBool = PropertyBool.create("south")
        val COLOR: PropertyEnum<EnumDyeColor> = PropertyEnum.create("color", EnumDyeColor::class.java)

        private val CENTER_AABB = AxisAlignedBB(5 / 16.0, 5 / 16.0, 5 / 16.0, 11 / 16.0, 11 / 16.0, 11 / 16.0)

        private val UP_AABB = AxisAlignedBB(5 / 16.0, 11 / 16.0, 5 / 16.0, 11 / 16.0, 1.0, 11 / 16.0)
        private val DOWN_AABB = AxisAlignedBB(5 / 16.0, 0.0, 5 / 16.0, 11 / 16.0, 5 / 16.0, 11 / 16.0)
        private val NORTH_AABB = AxisAlignedBB(5 / 16.0, 5 / 16.0, 0.0, 11 / 16.0, 11 / 16.0, 5 / 16.0)
        private val SOUTH_AABB = AxisAlignedBB(5 / 16.0, 5 / 16.0, 11 / 16.0, 11 / 16.0, 11 / 16.0, 1.0)
        private val WEST_AABB = AxisAlignedBB(0.0, 5 / 16.0, 5 / 16.0, 5 / 16.0, 11 / 16.0, 11 / 16.0)
        private val EAST_AABB = AxisAlignedBB(11 / 16.0, 5 / 16.0, 5 / 16.0, 1.0, 11 / 16.0, 11 / 16.0)

        val PROPERTIES = mapOf(
                EnumFacing.UP to UP,
                EnumFacing.DOWN to DOWN,
                EnumFacing.WEST to WEST,
                EnumFacing.EAST to EAST,
                EnumFacing.NORTH to NORTH,
                EnumFacing.SOUTH to SOUTH
        )

        val PROP_TO_AABB = mapOf(
                UP to UP_AABB,
                DOWN to DOWN_AABB,
                WEST to WEST_AABB,
                EAST to EAST_AABB,
                NORTH to NORTH_AABB,
                SOUTH to SOUTH_AABB
        )

        private val AABBS = mutableMapOf<IBlockState, AxisAlignedBB>()
    }

    init {
        setHardness(1f)
        for (state in blockState.validStates) {
            var aabb = CENTER_AABB
            for ((prop, bound) in PROP_TO_AABB.entries)
                if (state.getValue(prop)) aabb = aabb.union(bound)
            AABBS.put(state, aabb)
        }
    }

    override val blockColorFunction: ((state: IBlockState, world: IBlockAccess?, pos: BlockPos?, tintIndex: Int) -> Int)?
        get() = { state, _, _, idx ->
            if (idx == 0) state.getValue(COLOR).colorValue else -1
        }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { stack, idx ->
            if (idx == 0) EnumDyeColor.byMetadata(stack.itemDamage).colorValue else -1
        }

    override val extraVariants: Array<out String> get() = arrayOf("pipe")

    override val meshDefinition: ((stack: ItemStack) -> ModelResourceLocation)?
        //get() = { ModelHandler.getResource(LibMisc.MOD_ID, "pipe")!! } FIXME
        get() = { ModelResourceLocation("${LibMisc.MOD_ID}:pipe", "inventory") }

    @SideOnly(Side.CLIENT)
    override fun getBlockLayer(): BlockRenderLayer {
        return BlockRenderLayer.CUTOUT
    }

    override fun damageDropped(state: IBlockState) = getMetaFromState(state)

    fun getBoxes(state: IBlockState, worldIn: World, pos: BlockPos, isActualState: Boolean = true): List<AxisAlignedBB> {
        val ret = mutableListOf(CENTER_AABB)
        val actualState = if (isActualState) getActualState(state, worldIn, pos) else state
        for ((prop, bound) in PROP_TO_AABB.entries) if (actualState.getValue(prop))
            ret.add(bound)
        return ret
    }

    override fun collisionRayTrace(blockState: IBlockState, worldIn: World, pos: BlockPos, start: Vec3d, end: Vec3d): RayTraceResult? {
        return getBoxes(blockState, worldIn, pos).mapNotNull { rayTrace(pos, start, end, it) }.maxBy { it.hitVec.squareDistanceTo(end) }
    }

    override fun isFullCube(state: IBlockState) = false
    override fun isOpaqueCube(blockState: IBlockState) = false

    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState {
        var returnState = state
        for ((facing, prop) in PROPERTIES.entries) returnState = returnState.withProperty(prop, connectedOnSide(facing, pos, worldIn))
        return returnState
    }

    override fun doesSideBlockRendering(state: IBlockState, world: IBlockAccess, pos: BlockPos, face: EnumFacing): Boolean {
        val target = world.getBlockState(pos.offset(face))
        return target.block == this && target.getValue(COLOR) == state.getValue(COLOR)
    }

    fun connectedOnSide(facing: EnumFacing, pos: BlockPos, worldIn: IBlockAccess): Boolean {
        val posOffset = pos.offset(facing)
        val state = worldIn.getBlockState(posOffset)
        if ((state.block as? IConnectsToPipe)?.doesConnect(facing, pos, worldIn) == true)
            return true

        val tile = worldIn.getTileEntitySafely(pos)
        val tileAt = worldIn.getTileEntity(pos.offset(facing))
        if (tileAt is IColorAcceptor && tile is IColorAcceptor)
            return tileAt.getColor() == 0 || tile.getColor() == 0 || tile.getColor() == tileAt.getColor()
        return tileAt?.hasCapability(ITEM_HANDLER_CAPABILITY, facing.opposite) == true
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos) = AABBS[getActualState(state, source, pos)]
            ?: CENTER_AABB

    override fun addCollisionBoxToList(state: IBlockState, worldIn: World, pos: BlockPos, entityBox: AxisAlignedBB, collidingBoxes: MutableList<AxisAlignedBB>, entityIn: Entity?, isActualState: Boolean) {
        for (box in getBoxes(state, worldIn, pos))
            addCollisionBoxToList(pos, entityBox, collidingBoxes, box)
    }

    override fun createBlockState() = BlockStateContainer(this, UP, DOWN, WEST, EAST, NORTH, SOUTH, COLOR)
    override fun getMetaFromState(state: IBlockState) = state.getValue(COLOR).metadata
    override fun getStateFromMeta(meta: Int): IBlockState = defaultState.withProperty(COLOR, EnumDyeColor.byMetadata(meta))
    override fun canHarvestBlock(world: IBlockAccess, pos: BlockPos, player: EntityPlayer) = true

    override fun getBlockFaceShape(worldIn: IBlockAccess?, state: IBlockState?, pos: BlockPos?, face: EnumFacing?): BlockFaceShape {
        return BlockFaceShape.CENTER_BIG
    }

    override fun createTileEntity(world: World, state: IBlockState): TileEntity? {
        return TileItemPipe()
    }
}

interface IConnectsToPipe {
    fun doesConnect(facing: EnumFacing, pos: BlockPos, worldIn: IBlockAccess): Boolean
}

interface IColorAcceptor {
    fun setColor(facing: EnumFacing, color: Int)

    fun getColor(facing: EnumFacing): Int

    fun getColor(): Int

    fun acceptsColor(facing: EnumFacing, color: Int, strict: Boolean): Boolean

    companion object {
        fun tryPush(world: World, stack: ItemStack, pos: BlockPos, facing: EnumFacing, color: Int, strict: Boolean): ItemStack {
            val tileAt = world.getTileEntity(pos.offset(facing))
            if (tileAt != null && tileAt.hasCapability(ITEM_HANDLER_CAPABILITY, facing.opposite)) {
                val cap = tileAt.getCapability(ITEM_HANDLER_CAPABILITY, facing.opposite)
                if (cap != null) {
                    if (tileAt !is IColorAcceptor || tileAt.acceptsColor(facing, color, strict)) {
                        val remainStack = ItemHandlerHelper.insertItem(cap, stack, false)
                        if (remainStack.count != stack.count) {
                            if (tileAt is IColorAcceptor)
                                tileAt.setColor(facing.opposite, color)

                            return remainStack
                        }
                    }
                }
            }
            return stack
        }
    }
}

@TileRegister("item_pipe")
class TileItemPipe : TileModTickable(), IColorAcceptor {
    @Module
    @NoSync
    val items = ModuleInventory(object : ItemStackHandler(6) {
        override fun setStackInSlot(slot: Int, stack: ItemStack) {
            colors[slot] = 0
            super.setStackInSlot(slot, stack)
        }
    }).setSides(null)

    @NoSync
    @Save
    var colors = intArrayOf(0, 0, 0, 0, 0, 0)

    init {
        for (facing in EnumFacing.VALUES)
            initModule("sided_" + facing.name2, ModuleSlots(items.handler,
                    facing.index, facing.index + 1).setSides(facing))
    }

    override fun setColor(facing: EnumFacing, color: Int) {
        colors[facing.index] = color
    }

    override fun getColor(facing: EnumFacing): Int {
        val stackAt = items.handler.getStackInSlot(facing.index)
        val pipeColor = getColor()
        if (stackAt.isEmpty)
            return pipeColor

        val color = colors[facing.index]
        if (pipeColor != 0 && color == 0)
            return pipeColor
        return color
    }

    override fun getColor(): Int {
        val pipe = world.getBlockState(pos)
        if (pipe.block !is BlockItemPipe)
            return 0
        return pipe.getValue(BlockItemPipe.COLOR).metadata
    }

    override fun acceptsColor(facing: EnumFacing, color: Int, strict: Boolean): Boolean {
        if (color == 0 && !strict) return true
        val colorAt = getColor(facing)
        return colorAt == color || (colorAt == 0 && !strict)
    }

    fun pushFrom(facing: EnumFacing) {
        val stackAt = items.handler.getStackInSlot(facing.index)

        if (stackAt.isEmpty) return

        val color = getColor(facing)

        var remainder = tryPush(world, stackAt, pos, facing.opposite, color, true)
        for (pushFacing in EnumFacing.VALUES.shuffle()) if (pushFacing.axis != facing.axis) {
            remainder = tryPush(world, remainder, pos, pushFacing, color, true)
            if (remainder.isEmpty)
                break
        }

        if (remainder.isNotEmpty) {
            remainder = tryPush(world, stackAt, pos, facing.opposite, color, false)
            for (pushFacing in EnumFacing.VALUES.shuffle()) if (pushFacing.axis != facing.axis) {
                remainder = tryPush(world, remainder, pos, pushFacing, color, false)
                if (remainder.isEmpty)
                    break
            }
        }

        items.handler.setStackInSlot(facing.index, remainder)
        if (remainder.isNotEmpty)
            setColor(facing, color)
    }

    private inline fun <reified T> Array<T>.shuffle(): List<T> {
        val toList = mutableListOf<T>()
        val fromList = toMutableList()
        while (fromList.isNotEmpty()) {
            val idx = RandUtil.nextInt(fromList.size)
            toList.add(fromList[idx])
            fromList.removeAt(idx)
        }
        return toList
    }

    override fun tick() {
        if (!world.isRemote)
            for (facing in EnumFacing.VALUES)
                pushFrom(facing)
    }
}
