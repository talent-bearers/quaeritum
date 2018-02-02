package eladkay.quaeritum.common.rituals.diagrams

import com.teamwizardry.librarianlib.features.network.PacketHandler
import com.teamwizardry.librarianlib.features.network.sendToAllAround
import eladkay.quaeritum.api.animus.EnumAnimusTier
import eladkay.quaeritum.api.rituals.IDiagram
import eladkay.quaeritum.api.rituals.IDiagram.Helper.itemEquals
import eladkay.quaeritum.api.rituals.PositionedBlock
import eladkay.quaeritum.api.rituals.PositionedBlockChalk
import eladkay.quaeritum.common.block.tile.TileEntityBlueprint
import eladkay.quaeritum.common.networking.MessageTowerApplied
import eladkay.quaeritum.common.networking.MessageTowerEffect
import eladkay.quaeritum.common.networking.PuffMessage
import eladkay.quaeritum.common.potions.PotionRooted
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Items
import net.minecraft.init.MobEffects
import net.minecraft.init.SoundEvents
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.Teleporter
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import net.minecraftforge.common.DimensionManager
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingDamageEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color




/**
 * @author WireSegal
 * Created at 9:25 PM on 1/12/18.
 */
class TheTwistedTower : IDiagram {

    companion object {
        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        @SubscribeEvent
        fun towerSavesYou(event: LivingDamageEvent) {
            val player = event.entityLiving
            if (event.amount > player.health &&
                    player is EntityPlayer && player.entityData.hasKey("quaeritum-tower")) {
                val tower = BlockPos.fromLong(player.entityData.getLong("quaeritum-tower"))
                player.entityData.removeTag("quaeritum-tower")
                event.isCanceled = true

                player.heal(0.5f)
                player.addPotionEffect(PotionEffect(MobEffects.WEAKNESS, 600, 1, true, true))
                player.addPotionEffect(PotionEffect(MobEffects.REGENERATION, 600, 1, true, true))
                player.addPotionEffect(PotionEffect(PotionRooted, 200, 0, true, true))
                if (player is EntityPlayerMP) {
                    PacketHandler.NETWORK.sendTo(MessageTowerApplied(Long.MAX_VALUE), player)

                    val world = DimensionManager.getWorld(0)
                    val teAt = world.getTileEntity(tower)
                    if (teAt != null && teAt is TileEntityBlueprint && teAt.bestRitual is TheTwistedTower) {

                        val oldVec = player.positionVector
                        val oldWorld = player.world

                        if (player.dimension != 0)
                            TowerTeleporter.teleportHome(player, tower.x + 0.5, tower.y + 0.5, tower.z + 0.5)
                        else
                            player.connection.setPlayerLocation(tower.x + 0.5, tower.y + 0.5, tower.z + 0.5, player.rotationYaw, player.rotationPitch)

                        oldWorld.playSound(null, oldVec.x, oldVec.y, oldVec.z, SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1F, 1F)
                        PacketHandler.NETWORK.sendToAllAround(PuffMessage(oldVec, color = Color(0xFFFF40),
                                scatter = 0.25, amount = 100, verticalMin = 0.06, verticalMax = 0.09),
                                oldWorld, oldVec, 64)

                        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1F, 1F)
                        PacketHandler.NETWORK.sendToAllAround(PuffMessage(player.positionVector, color = Color(0xFFFF40),
                                scatter = 0.25, amount = 100, verticalMin = 0.06, verticalMax = 0.09),
                                world, player.positionVector, 64)
                    }
                }
            }
        }
    }

    class TowerTeleporter(private val serverWorld: WorldServer, private val x: Double, private val y: Double, private val z: Double) : Teleporter(serverWorld) {

        companion object {
            fun teleportHome(player: EntityPlayerMP, x: Double, y: Double, z: Double) {
                val oldDimension = player.entityWorld.provider.dimension
                val server = player.entityWorld.minecraftServer ?: return
                val worldServer = DimensionManager.getWorld(0)
                player.addExperienceLevel(0)

                server.playerList.transferPlayerToDimension(player, 0, TowerTeleporter(worldServer, x, y, z))
                player.setPositionAndUpdate(x, y, z)
                if (oldDimension == 1) {
                    player.setPositionAndUpdate(x, y, z)
                    worldServer.spawnEntity(player)
                    worldServer.updateEntityWithOptionalForce(player, false)
                }
            }
        }

        override fun placeInPortal(pEntity: Entity, rotationYaw: Float) {
            serverWorld.getBlockState(BlockPos(this.x.toInt(), this.y.toInt(), this.z.toInt()))

            pEntity.setPosition(this.x, this.y, this.z)
            pEntity.motionX = 0.0
            pEntity.motionY = 0.0
            pEntity.motionZ = 0.0
        }

    }

    override fun getUnlocalizedName(): String {
        return "twistedtower"
    }

    override fun run(world: World, pos: BlockPos, tile: TileEntity) {
        val player = world.getClosestPlayer(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5, 4.0) {
            it is EntityPlayerMP && !it.isSpectator && it.entityData.hasKey("quaeritum-prospective") &&
                    it.entityData.getLong("quaeritum-prospective") == pos.toLong()
        } as? EntityPlayerMP ?: return

        val itemsFound = mutableMapOf<Any, EntityItem>()
        for (stack in IDiagram.Helper.entitiesAroundAltar(tile, 4.0))
            for (item in items)
                if (item !in itemsFound && itemEquals(stack.item, item))
                    itemsFound[item] = stack

        if (player.entityData.getLong("quaeritum-tower") != pos.toLong() &&
                items.none { itemsFound[it] == null } &&
                IDiagram.Helper.consumeAnimusForRitual(tile, true, 1000, EnumAnimusTier.ARGENTUS)) {
            PacketHandler.NETWORK.sendToAllAround(PuffMessage(player.positionVector, color = Color(0x40FF40),
                    scatter = 0.5, amount = 100, verticalMin = 0.03, verticalMax = 0.06),
                    world, player.positionVector, 64)

            for ((_, item) in itemsFound) {
                if (item.item.item == Items.GHAST_TEAR)
                    PacketHandler.NETWORK.sendToAllAround(PuffMessage(item.positionVector, color = Color(0xA0A0A0), amount = 100, verticalMin = 0.04, verticalMax = 0.05),
                            world, item.positionVector, 64)
                else
                    PacketHandler.NETWORK.sendToAllAround(PuffMessage(item.positionVector, color = Color(0xF0F040), amount = 100, verticalMin = 0.04, verticalMax = 0.05),
                            world, item.positionVector, 64)

                item.item.shrink(1)
                if (item.item.isEmpty)
                    item.setDead()
            }

            player.entityData.removeTag("quaeritum-prospective")
            player.entityData.setLong("quaeritum-tower", pos.toLong())
            PacketHandler.NETWORK.sendTo(MessageTowerApplied(pos.toLong()), player)
        } else PacketHandler.NETWORK.sendToAllAround(PuffMessage(player.positionVector, color = Color(0xFF4040),
                scatter = 0.5, amount = 100, verticalMin = 0.03, verticalMax = 0.06),
                world, player.positionVector, 64)
    }

    override fun getPrepTime(world: World, pos: BlockPos, tile: TileEntity): Int {
        return 100
    }

    override fun onPrepUpdate(world: World, pos: BlockPos, tile: TileEntity, ticksRemaining: Int): Boolean {
        if (ticksRemaining == getPrepTime(world, pos, tile) - 1) {
            val player = world.getClosestPlayer(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5, 4.0, false)
                    ?: return false
            player.entityData.setLong("quaeritum-prospective", pos.toLong())
        }

        val player = world.getClosestPlayer(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5, 4.0) {
            it is EntityPlayer && !it.isSpectator && it.entityData.hasKey("quaeritum-prospective") &&
                    it.entityData.getLong("quaeritum-prospective") == pos.toLong()
        } ?: return false

        player.addPotionEffect(PotionEffect(PotionRooted, 10, 0, true, true))
        val posVec = Vec3d(pos).addVector(0.5, 0.125, 0.5)
        PacketHandler.NETWORK.sendToAllAround(MessageTowerEffect(posVec, player.positionVector.addVector(0.0, player.getEyeHeight() * 1.5, 0.0)),
                world, posVec, 64)

        PacketHandler.NETWORK.sendToAllAround(PuffMessage(player.positionVector, color = Color(0x341C0E),
                amount = 10, verticalMin = 0.06, verticalMax = 0.09, scatter = 0.2),
                world, player.positionVector, 64)

        return true
    }

    override fun canRitualRun(world: World, pos: BlockPos, tile: TileEntity): Boolean {
        return tile.world.provider.dimension == 0 &&
                IDiagram.Helper.consumeAnimusForRitual(tile, false, 1000, EnumAnimusTier.ARGENTUS)
    }

    val items = listOf(
            "ingotGold",
            ItemStack(Items.GHAST_TEAR)
    )

    override fun hasRequiredItems(world: World, pos: BlockPos, tile: TileEntity): Boolean {
        return IDiagram.Helper.matches(IDiagram.Helper.stacksAroundAltar(tile, 4.0), items)
    }

    override fun buildChalks(chalks: MutableList<PositionedBlock>) {
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(-2, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLUE, BlockPos(-1, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(0, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BROWN, BlockPos(1, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(2, 0, -2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLUE, BlockPos(-2, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLUE, BlockPos(-1, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(0, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BROWN, BlockPos(1, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BROWN, BlockPos(2, 0, -1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(-2, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(-1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(1, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(2, 0, 0)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BROWN, BlockPos(-2, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BROWN, BlockPos(-1, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(0, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLUE, BlockPos(1, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLUE, BlockPos(2, 0, 1)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(-2, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BROWN, BlockPos(-1, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(0, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLUE, BlockPos(1, 0, 2)))
        chalks.add(PositionedBlockChalk(EnumDyeColor.BLACK, BlockPos(2, 0, 2)))
    }
}
