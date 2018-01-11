package eladkay.quaeritum.common.item.misc

import com.teamwizardry.librarianlib.features.base.item.ItemModSword
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.helpers.threadLocal
import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.api.animus.EnumAnimusTier
import eladkay.quaeritum.api.contract.ContractRegistry
import eladkay.quaeritum.common.item.ItemContractScroll
import eladkay.quaeritum.common.item.ModItems
import eladkay.quaeritum.common.item.oath
import eladkay.quaeritum.common.lib.LibMaterials
import eladkay.quaeritum.common.networking.PuffMessage
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

/**
 * @author WireSegal
 * Created at 4:15 PM on 1/10/18.
 */
class ItemOathScar : ItemModSword("oath_scar", LibMaterials.SCAR) {

    private val TAG_STORAGE = "stored"
    private val TAG_BLOCK = "block"
    private val TAG_META = "meta"
    private val TAG_TILE = "tile"
    private val TAG_TICK_STORED = "tick"

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    private fun getStored(stack: ItemStack): Pair<IBlockState, NBTTagCompound?>? {
        val comp = ItemNBTHelper.getCompound(stack, TAG_STORAGE) ?: return null

        val blockRL = comp.getString(TAG_BLOCK)
        val meta = comp.getInteger(TAG_META)
        val tileData: NBTTagCompound? = if (comp.hasKey(TAG_TILE)) comp.getCompoundTag(TAG_TILE) else null

        val block = Block.getBlockFromName(blockRL) ?: return null
        val state = block.getStateFromMeta(meta)

        return state to tileData
    }

    private fun setStored(stack: ItemStack, tickTime: Long, state: IBlockState?, tile: NBTTagCompound?) {
        ItemNBTHelper.setLong(stack, TAG_TICK_STORED, tickTime)

        if (state == null) {
            ItemNBTHelper.removeEntry(stack, TAG_STORAGE)
            return
        }

        if (tile != null) {
            tile.removeTag("id")
            tile.removeTag("x")
            tile.removeTag("y")
            tile.removeTag("z")
        }

        val comp = NBTTagCompound()
        comp.setString(TAG_BLOCK, state.block.registryName.toString())
        comp.setByte(TAG_META, state.block.getMetaFromState(state).toByte())
        if (tile != null)
            comp.setTag(TAG_TILE, tile)
        ItemNBTHelper.set(stack, TAG_STORAGE, comp)
    }

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        val stackInOtherHand = playerIn.getHeldItem(if (handIn == EnumHand.MAIN_HAND) EnumHand.OFF_HAND else EnumHand.MAIN_HAND)
        if (stackInOtherHand.item == ModItems.scroll && stackInOtherHand.itemDamage == 1 && !worldIn.isRemote) {
            val oath = ContractRegistry.getOathFromId(stackInOtherHand.oath)
            if (oath != null && ContractRegistry.getNameFromOath(oath).toString() == "quaeritum:connection") {
                oath.fireContract(playerIn, stackInOtherHand, worldIn, playerIn.position)
            }
            return ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn))
        }
        return super.onItemRightClick(worldIn, playerIn, handIn)
    }

    private var lockedWorld: World? by threadLocal { null }

    @SubscribeEvent
    fun joinWorld(event: EntityJoinWorldEvent) {
        if (event.world == lockedWorld) event.isCanceled = true
    }

    @SubscribeEvent
    fun onPunchBlock(event: PlayerInteractEvent.LeftClickBlock) {
        val stack = event.itemStack
        if (stack.item != this) return
        val player = event.entityPlayer
        val pos = event.pos
        val world = player.world

        val time = ItemNBTHelper.getLong(stack, TAG_TICK_STORED, 0L)
        if (world.totalWorldTime - time < 2) return

        val stored = getStored(stack)
        if (!world.isRemote) {
            if (stored == null) {
                if (AnimusHelper.Network.requestAnimus(player, 1, EnumAnimusTier.ATLAS, true)) {
                    val blockState = world.getBlockState(pos)

                    if (!world.isBlockLoaded(pos) || !world.isBlockModifiable(player, pos) ||
                            blockState.block.getPlayerRelativeBlockHardness(blockState, player, world, pos) <= 0 ||
                            MinecraftForge.EVENT_BUS.post(BlockEvent.BreakEvent(world, pos, blockState, player)))
                        return

                    val tile = player.world.getTileEntity(pos)
                    val tileData = tile?.writeToNBT(NBTTagCompound())
                    setStored(stack, player.world.totalWorldTime, blockState, tileData)
                    ItemContractScroll.puff(PuffMessage(Vec3d(pos).addVector(0.5, 0.0, 0.5), scatter = 0.25, amount = 50, color = Color(0x3030BF)), world)

                    val prevLock = lockedWorld
                    lockedWorld = world
                    if (tile != null) world.removeTileEntity(pos)
                    world.destroyBlock(pos, false)
                    lockedWorld = prevLock

                    world.playSound(null, pos, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f)
                }
            } else {
                var setPosAt = pos
                val stateAt = world.getBlockState(pos)
                val block = stateAt.block

                val attrib = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).attributeValue.toFloat()
                val dist = if (player.isCreative) attrib else attrib - 0.5f
                val vec3d = player.getPositionEyes(1f)
                val vec3d1 = player.getLook(1f)
                val vec3d2 = vec3d.addVector(vec3d1.x * dist, vec3d1.y * dist, vec3d1.z * dist)
                val trace = world.rayTraceBlocks(vec3d, vec3d2, false, false, true) ?: return
                val facing = trace.sideHit

                if (!block.isReplaceable(world, pos)) setPosAt = pos.offset(facing)

                if (player.canPlayerEdit(setPosAt, facing, stack) && world.mayPlace(block, setPosAt, false, facing, null)) {
                    world.playSound(null, setPosAt, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1f, 0.75f)
                    ItemContractScroll.puff(PuffMessage(Vec3d(setPosAt).addVector(0.5, 0.0, 0.5), scatter = 0.25, amount = 50, color = Color(0x3030BF)), world)

                    val state = stored.first
                    val tileData = stored.second

                    world.playEvent(2001, pos, Block.getStateId(state))

                    world.setBlockState(setPosAt, state)
                    world.neighborChanged(setPosAt, state.block, setPosAt)
                    val tile = world.getTileEntity(setPosAt)
                    if (tileData != null && tile != null) {
                        val data = tile.writeToNBT(NBTTagCompound())
                        for (key in tileData.keySet)
                            data.setTag(key, tileData.getTag(key))
                        tile.readFromNBT(data)
                        tile.markDirty()
                    }
                    setStored(stack, player.world.totalWorldTime, null, null)
                }
                event.isCanceled = true
            }
        }
    }

    override fun getRarity(stack: ItemStack): EnumRarity {
        return EnumRarity.EPIC
    }
}
