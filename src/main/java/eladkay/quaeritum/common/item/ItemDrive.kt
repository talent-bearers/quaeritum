package eladkay.quaeritum.common.item

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.kotlin.minus
import com.teamwizardry.librarianlib.features.kotlin.motionVec
import com.teamwizardry.librarianlib.features.kotlin.plus
import com.teamwizardry.librarianlib.features.kotlin.times
import com.teamwizardry.librarianlib.features.network.PacketHandler
import com.teamwizardry.librarianlib.features.network.sendToAllAround
import com.teamwizardry.librarianlib.features.utilities.RaycastUtils
import com.teamwizardry.librarianlib.features.utilities.client.ClientRunnable
import eladkay.quaeritum.api.animus.AnimusHelper
import eladkay.quaeritum.api.animus.EnumAnimusTier
import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.common.networking.MessageDriveEffect
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.awt.Color

/**
 * @author WireSegal
 * Created at 6:52 PM on 12/26/17.
 */
@Suppress("LeakingThis")
abstract class ItemDrive(name: String, val minTier: EnumAnimusTier) : ItemMod("${name}_drive") {
    init {
        setMaxStackSize(1)
        addPropertyOverride(ResourceLocation(LibMisc.MOD_ID, "left")) { stack, _, entity ->
            val default: EnumHandSide = ClientRunnable.produce { Minecraft.getMinecraft().player }?.primaryHand
                    ?: EnumHandSide.RIGHT
            val hand = if (entity != null) when {
                entity.heldItemMainhand === stack -> entity.primaryHand
                entity.heldItemOffhand === stack -> entity.primaryHand.opposite()
                else -> default
            } else default

            if (hand == EnumHandSide.LEFT) 1f else 0f
        }
    }

    override fun getMaxItemUseDuration(stack: ItemStack): Int {
        return 72000
    }

    override fun getItemUseAction(stack: ItemStack): EnumAction {
        return EnumAction.BOW
    }

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        val stack = playerIn.getHeldItem(handIn)
        if (AnimusHelper.Network.requestAnimus(playerIn, 10, minTier, !worldIn.isRemote)) {
            playerIn.activeHand = handIn
            return ActionResult(EnumActionResult.SUCCESS, stack)
        }
        return super.onItemRightClick(worldIn, playerIn, handIn)
    }

    override fun onUsingTick(stack: ItemStack, player: EntityLivingBase, count: Int) {
        if (player is EntityPlayer && !player.world.isRemote) {
            if (AnimusHelper.Network.requestAnimus(player, 1, EnumAnimusTier.LUCIS, true)) {
                val from = player.positionVector
                        .add(0.0, player.getEyeHeight() * 0.75, 0.0)
                        .add(player.lookVec)

                val cast = RaycastUtils.raycast(player, 16.0)

                val vec = player.positionVector.add(0.0, player.getEyeHeight() / 2.0, 0.0)
                val dist = if (cast == null || cast.typeOfHit == RayTraceResult.Type.MISS) 16.0 else cast.hitVec.distanceTo(vec)

                var motion = player.lookVec.scale(dist / 16.0) + Vec3d(0.0, player.getEyeHeight() * 0.01, 0.0).scale(0.5).add(player.motionVec)
                if (player.onGround)
                    motion = motion.add(0.0, 0.08, 0.0)
                colors.forEach {
                    PacketHandler.NETWORK.sendToAllAround(MessageDriveEffect(from, motion, it),
                            player.world, from, 64)
                }

                if (count % 2 == 0) {
                    val to = player.lookVec.scale(dist).add(vec)

                    val aabb = AxisAlignedBB(from.x, from.y, from.z, to.x, to.y, to.z)

                    player.world.getEntitiesWithinAABB(EntityLivingBase::class.java, aabb.grow(1.0)) {
                        val bb = it?.entityBoundingBox
                        bb != null && (it != player || player.rotationPitch == 90F) && intersectsBox(from, to, bb.grow(1.0))
                    }.forEach {
                                affectEntity(stack, player, count, it)
                            }
                }
            }
        } else if (!player.world.isRemote) player.stopActiveHand()
    }


    abstract fun affectEntity(stack: ItemStack, player: EntityPlayer, count: Int, target: EntityLivingBase)

    abstract val colors: Iterable<Color>

    private var hit = Vec3d.ZERO

    fun getIntersection(fDst1: Double, fDst2: Double, l1: Vec3d, l2: Vec3d): Boolean {
        if ((fDst1 * fDst2) >= 0.0f) return false
        if (fDst1 == fDst2) return false
        hit = l1 + (l2 - l1) * (-fDst1 / (fDst2 - fDst1))
        return true
    }

    fun inBox(b1: Vec3d, b2: Vec3d, axis: EnumFacing.Axis): Boolean {
        return when (axis) {
            EnumFacing.Axis.X -> hit.z > b1.z &&
                    hit.z < b2.z &&
                    hit.y > b1.y &&
                    hit.y < b2.y
            EnumFacing.Axis.Y -> hit.z > b1.z &&
                    hit.z < b2.z &&
                    hit.x > b1.x &&
                    hit.x < b2.x
            EnumFacing.Axis.Z -> hit.x > b1.x &&
                    hit.x < b2.x &&
                    hit.y > b1.y &&
                    hit.y < b2.y
            else -> false
        }
    }

    fun intersectsBox(l1: Vec3d, l2: Vec3d, aabb: AxisAlignedBB): Boolean {
        val b1 = Vec3d(aabb.minX, aabb.minY, aabb.minZ)
        val b2 = Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ)
        if (l2.x < b1.x && l1.x < b1.x) return false
        if (l2.x > b2.x && l1.x > b2.x) return false
        if (l2.y < b1.y && l1.y < b1.y) return false
        if (l2.y > b2.y && l1.y > b2.y) return false
        if (l2.z < b1.z && l1.z < b1.z) return false
        if (l2.z > b2.z && l1.z > b2.z) return false
        if (l1.x > b1.x && l1.x < b2.x &&
                l1.y > b1.y && l1.y < b2.y &&
                l1.z > b1.z && l1.z < b2.z) return true
        return getIntersection(l1.x - b1.x, l2.x - b1.x, l1, l2) && inBox(b1, b2, EnumFacing.Axis.X)
                || getIntersection(l1.y - b1.y, l2.y - b1.y, l1, l2) && inBox(b1, b2, EnumFacing.Axis.Y)
                || getIntersection(l1.z - b1.z, l2.z - b1.z, l1, l2) && inBox(b1, b2, EnumFacing.Axis.Z)
                || getIntersection(l1.x - b2.x, l2.x - b2.x, l1, l2) && inBox(b1, b2, EnumFacing.Axis.X)
                || getIntersection(l1.y - b2.y, l2.y - b2.y, l1, l2) && inBox(b1, b2, EnumFacing.Axis.Y)
                || getIntersection(l1.z - b2.z, l2.z - b2.z, l1, l2) && inBox(b1, b2, EnumFacing.Axis.Z)
    }
}
