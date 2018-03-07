package eladkay.quaeritum.common.block.machine

import com.mojang.authlib.GameProfile
import com.teamwizardry.librarianlib.features.autoregister.TileRegister
import com.teamwizardry.librarianlib.features.base.block.tile.BlockModContainer
import com.teamwizardry.librarianlib.features.base.block.tile.TileMod
import com.teamwizardry.librarianlib.features.kotlin.isNotEmpty
import eladkay.quaeritum.common.lib.LibNames
import net.minecraft.block.Block
import net.minecraft.block.BlockPistonBase
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.FakePlayer
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import java.util.*

/**
 * @author WireSegal
 * Created at 10:40 AM on 2/3/17.
 */
class BlockInterface : BlockModContainer(LibNames.INTERFACE, Material.WOOD) {

    companion object {
        val FACING: PropertyDirection = PropertyDirection.create("facing")

        val profile = GameProfile(UUID.fromString("7C1D207B-1413-4B13-813F-4464C44A509D"), "[InterfaceInteraction]")

    }

    override fun getStateForPlacement(world: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase, hand: EnumHand): IBlockState {
        return defaultState.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer))
    }

    override fun createBlockState() = BlockStateContainer(this, FACING)
    override fun getMetaFromState(state: IBlockState) = state.getValue(FACING).index
    override fun getStateFromMeta(meta: Int): IBlockState = defaultState.withProperty(FACING, EnumFacing.getFront(meta))

    override fun createTileEntity(world: World, state: IBlockState) = TileInterface()

    @TileRegister("interface")
    class TileInterface : TileMod() {

        private val interfaceHandler = WorldInterface()

        @Suppress("UNCHECKED_CAST")
        override fun <T : Any> getCapability(capability: Capability<T>, facing: EnumFacing?) =
                if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                    interfaceHandler as T
                else super.getCapability(capability, facing)

        override fun hasCapability(capability: Capability<*>, facing: EnumFacing?) =
                capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing)

        private inner class WorldInterface : IItemHandler {
            private fun getContainedForSlot(world: World, facing: EnumFacing, pos: BlockPos, slot: Int, n: Int, take: Boolean): ItemStack {
                val posAt = pos.offset(facing)
                val blockAt = world.getBlockState(posAt)
                if (slot == 1) {
                    val items = world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(posAt))
                    if (items.isNotEmpty()) {
                        val takeFrom = mutableListOf<EntityItem>()
                        var total = 0
                        val iterator = items.iterator()
                        var first = EntityItem(world)
                        while (first.item.isEmpty)
                            first = iterator.next()
                        takeFrom.add(first)
                        total += first.item.count
                        if (n > total) for (item in iterator) {
                            if (ItemStack.areItemStackTagsEqual(item.item, first.item) && ItemStack.areItemsEqual(item.item, first.item)) {
                                total += item.item.count
                                takeFrom.add(item)
                                if (n > total) break
                            }
                        }


                        val copy = first.item.copy()
                        val subtractAmount = copy.count
                        for (i in takeFrom) {
                            val toTake = Math.min(i.item.count, total)
                            copy.count += toTake
                            if (take) {
                                i.item.count -= toTake
                                if (i.item.isEmpty)
                                    i.setDead()
                            }

                            total -= toTake
                        }

                        copy.count -= subtractAmount

                        return copy
                    }
                } else if (slot == 0) {
                    if (BlockPistonBase.canPush(blockAt, world, posAt, facing, true, facing)) {
                        Block.captureDrops.set(true)
                        Block.capturedDrops.get().clear()
                        blockAt.block.dropBlockAsItem(world, posAt, blockAt, 0)
                        val drops = Block.capturedDrops.get().toList()
                        Block.captureDrops.set(false)
                        if (drops.isNotEmpty()) {
                            val toDrop = mutableListOf<ItemStack>()
                            var total = n
                            val iterator = drops.iterator()
                            val first = iterator.next()
                            if (first.count > total)
                                toDrop.add(first.apply { count -= total })
                            total -= first.count

                            for (item in iterator) {
                                if (total > 0 && ItemStack.areItemStackTagsEqual(item, first) && ItemStack.areItemsEqual(item, first)) {
                                    if (item.count > total)
                                        toDrop.add(item.apply { count -= total })

                                    total -= item.count
                                } else
                                    toDrop.add(item)
                            }

                            val ret = first.copy().apply { count = n - Math.max(total, 0) }
                            if (take) {
                                world.destroyBlock(posAt, false)
                                for (item in toDrop) Block.spawnAsEntity(world, posAt, item)
                            }
                            return ret
                        }
                    }
                }

                return ItemStack.EMPTY
            }

            override fun getSlots(): Int {
                return 2
            }

            override fun getStackInSlot(slot: Int): ItemStack {
                validateSlotIndex(slot)
                return ItemStack.EMPTY
            }

            override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
                validateSlotIndex(slot)

                if (simulate) return ItemStack.EMPTY

                val facing = world.getBlockState(pos).getValue(FACING)
                val worldS = world
                if (worldS is WorldServer) {
                    if (slot == 0) {
                        val player = FakePlayer(worldS, profile)

                        var resultStack = stack.copy()

                        player.capabilities.isCreativeMode = false
                        player.rotationYaw = facing.horizontalAngle
                        player.rotationPitch = if (facing == EnumFacing.UP) -90f else if (facing == EnumFacing.DOWN) 90f else 0f
                        player.posX = pos.x + 0.5 + facing.frontOffsetX * 0.75
                        player.posY = pos.y + 0.5 - player.eyeHeight + facing.frontOffsetY * 0.75
                        player.posZ = pos.z + 0.5 + facing.frontOffsetZ * 0.75

                        player.setHeldItem(EnumHand.MAIN_HAND, resultStack)
                        val result = resultStack.onItemUse(player, worldS, pos.offset(facing), EnumHand.MAIN_HAND, facing.opposite, 0f, 0f, 0f)
                        if (resultStack.isNotEmpty && result == EnumActionResult.PASS) {
                            resultStack = stack.copy()
                            resultStack = resultStack.useItemRightClick(worldS, player, EnumHand.MAIN_HAND).result
                        }

                        if (resultStack.isNotEmpty) {
                            val item = EntityItem(worldS,
                                    pos.x + 0.5 + facing.frontOffsetX * 0.625,
                                    pos.y + 0.5 + facing.frontOffsetY * 0.625,
                                    pos.z + 0.5 + facing.frontOffsetZ * 0.625,
                                    resultStack)
                            item.motionX = facing.frontOffsetX * 0.25
                            item.motionY = facing.frontOffsetY * 0.25 + 0.1
                            item.motionZ = facing.frontOffsetZ * 0.25
                            world.spawnEntity(item)
                        }
                    } else {
                        val item = EntityItem(worldS,
                                pos.x + 0.5 + facing.frontOffsetX * 0.625,
                                pos.y + 0.5 + facing.frontOffsetY * 0.625,
                                pos.z + 0.5 + facing.frontOffsetZ * 0.625,
                                stack)
                        item.motionX = facing.frontOffsetX * 0.25
                        item.motionY = facing.frontOffsetY * 0.25 + 0.1
                        item.motionZ = facing.frontOffsetZ * 0.25
                        world.spawnEntity(item)
                    }
                }

                return ItemStack.EMPTY
            }

            override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
                if (amount == 0)
                    return ItemStack.EMPTY

                validateSlotIndex(slot)

                return getContainedForSlot(world, world.getBlockState(pos).getValue(FACING), pos, slot, amount, !simulate)
            }

            override fun getSlotLimit(slot: Int): Int {
                return 64
            }

            private fun validateSlotIndex(slot: Int) {
                if (slot < 0 || slot >= slots)
                    throw RuntimeException("Slot $slot not in valid range - [0,$slots)")
            }
        }
    }
}
