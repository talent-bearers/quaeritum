package eladkay.quaeritum.common.rituals.diagrams

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.kotlin.NBTTagList
import com.teamwizardry.librarianlib.features.network.PacketHandler
import com.teamwizardry.librarianlib.features.network.sendToAllAround
import com.teamwizardry.librarianlib.features.utilities.client.ClientRunnable
import eladkay.quaeritum.api.rituals.IDiagram
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.rituals.PositionedBlockChalk
import eladkay.quaeritum.client.lib.LibParticles
import eladkay.quaeritum.common.block.tile.TileEntityBlueprint
import eladkay.quaeritum.common.core.QuaeritumInternalHandler
import eladkay.quaeritum.common.networking.MessageAcademyEffect
import eladkay.quaeritum.common.networking.MessageAcademyEffect.Companion.colorFromLocation
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagLong
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.common.util.Constants


/**
 * @author WireSegal
 * Created at 12:04 AM on 12/17/17.
 */
class AcademyOfTheFive : IDiagram {
    override fun getUnlocalizedName(): String {
        return "academy_of_the_five"
    }

    override fun run(world: World, pos: BlockPos, tile: TileEntity) {
        val data = QuaeritumInternalHandler.getTrueSaveData()
        val hash = data.academies.hashCode()
        data.academies.add(pos)
        data.academies.removeIf {
            val tileAt = world.getTileEntity(it)
            it != pos && (tileAt == null || tileAt !is TileEntityBlueprint || tileAt.bestRitual != this)
        }

        val academiesToSend = mutableSetOf<BlockPos>()
        for (academy in data.academies.sortedBy { it.distanceSq(pos) }) {
            if (academy != pos)
                academiesToSend.add(academy)
            if (academiesToSend.size >= 4)
                break
        }

        PacketHandler.NETWORK.sendToAllAround(MessageAcademyEffect(pos, academiesToSend.toTypedArray()),
                world, Vec3d(pos).addVector(0.5, 0.5, 0.5), 64)

        tile.world.getEntitiesWithinAABB(Entity::class.java, AxisAlignedBB(tile.pos).grow(4.0))
                .asSequence()
                .filter { (it is EntityPlayer || it is EntityItem)
                        && tile.pos.distanceSq(it.posX, it.posY, it.posZ) < 16.0 }
                .sortedBy { it.getDistanceSq(tile.pos) }
                .flatMap {
                    if (it is EntityPlayer) {
                        sequenceOf(*(0 until it.inventory.inventoryStackLimit).map { stackId -> it.inventory.getStackInSlot(stackId) }.toTypedArray() )
                    } else
                        sequenceOf((it as EntityItem).item)
                }.filter {
            it.item is ItemStarMap
        }.forEach {
            ItemNBTHelper.setList(it, "poses", NBTTagList(academiesToSend.size) { NBTTagLong(academiesToSend.toList()[it].toLong()) })
        }

        if (data.academies.hashCode() != hash)
            data.markDirty()
    }

    override fun canRitualRun(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        return true
    }

    override fun hasRequiredItems(world: World?, pos: BlockPos, tile: TileEntity): Boolean {
        return true
    }

    override fun buildChalks(chalks: MutableList<PositionedBlock>) {
        chalks.add(PositionedBlockChalk(EnumDyeColor.CYAN, BlockPos(-1, 0, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.ORANGE, BlockPos(0, 0, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.CYAN, BlockPos(1, 0, -3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-2, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-1, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.ORANGE, BlockPos(0, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(1, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(2, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.PURPLE, BlockPos(-3, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-2, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.ORANGE, BlockPos(0, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(2, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(3, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIGHT_BLUE, BlockPos(-3, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIGHT_BLUE, BlockPos(-2, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIGHT_BLUE, BlockPos(-1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GRAY, BlockPos(1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GRAY, BlockPos(2, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.GRAY, BlockPos(3, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.PURPLE, BlockPos(-3, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-2, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLUE, BlockPos(0, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(2, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.LIME, BlockPos(3, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-2, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(-1, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLUE, BlockPos(0, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(1, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.WHITE, BlockPos(2, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BROWN, BlockPos(-1, 0, 3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLUE, BlockPos(0, 0, 3)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BROWN, BlockPos(1, 0, 3)))
    }
}

class ItemStarMap : ItemMod("star_map") {
    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        val stack = playerIn.getHeldItem(handIn)
        if ((ItemNBTHelper.getList(stack, "poses", Constants.NBT.TAG_LONG)?.tagCount() ?: 0) > 0) {
            if (worldIn.isRemote) ClientRunnable.run {
                val poses = ItemNBTHelper.getList(stack, "poses", Constants.NBT.TAG_LONG)
                if (poses != null) for (pos in poses) {
                    val subCent = playerIn.positionVector.addVector(0.0, 0.5, 0.0)
                    val cent = subCent.addVector(0.0, 1.0, 0.0)
                    val position = BlockPos.fromLong((pos as NBTTagLong).long)
                    var dir = Vec3d(position).addVector(0.5, 0.5, 0.5).subtract(subCent)
                    dir = dir.addVector(0.0, -dir.y / 2, 0.0)
                    dir = dir.normalize()
                    (0 until 10)
                            .map { cent.add(dir.scale(1.5 + it / 6.0)) }
                            .forEach { vec -> LibParticles.embers(50, 0.5f, vec, colorFromLocation(position), 0.35) }
                }
            }
            return ActionResult(EnumActionResult.SUCCESS, stack)
        }
        return super.onItemRightClick(worldIn, playerIn, handIn)
    }
}
