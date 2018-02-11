package eladkay.quaeritum.common.entity

import com.google.common.base.Optional
import net.minecraft.block.Block
import net.minecraft.block.BlockFalling
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.crash.CrashReportCategory
import net.minecraft.entity.Entity
import net.minecraft.entity.MoverType
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class EntityDroppingBlock : Entity {
    var fallTime: Int = 0
    var shouldDropItem = true
    var tileEntityData: NBTTagCompound? = null

    constructor(worldIn: World) : super(worldIn)

    constructor(worldIn: World, x: Double, y: Double, z: Double, fallingBlockState: IBlockState) : super(worldIn) {
        this.block = fallingBlockState
        this.preventEntitySpawning = true
        this.setSize(0.98f, 0.98f)
        this.setPosition(x, y + ((1.0f - this.height) / 2.0f).toDouble(), z)
        this.motionX = 0.0
        this.motionY = 0.0
        this.motionZ = 0.0
        this.prevPosX = x
        this.prevPosY = y
        this.prevPosZ = z
        this.origin = BlockPos(this)
    }

    fun withDrop(boolean: Boolean): EntityDroppingBlock {
        shouldDropItem = boolean
        return this
    }

    var origin: BlockPos
        get() = this.dataManager.get(ORIGIN)
        set(value) = this.dataManager.set(ORIGIN, value)

    var block: IBlockState
        get() {
            val optional = this.dataManager.get(STATE)
            if (optional.isPresent)
                return optional.get()
            return Blocks.AIR.defaultState
        }
        set(value) = dataManager.set(STATE, Optional.of(value))

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    override fun canTriggerWalking(): Boolean {
        return false
    }

    override fun entityInit() {
        this.dataManager.register(ORIGIN, BlockPos.ORIGIN)
        this.dataManager.register(STATE, Optional.absent())
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    override fun canBeCollidedWith(): Boolean {
        return !this.isDead
    }

    /**
     * Called to updateTfidfSearches the entity's position/logic.
     */
    override fun onUpdate() {
        val block = this.block.block

        if (this.block.material == Material.AIR) this.setDead()
        else {
            this.prevPosX = this.posX
            this.prevPosY = this.posY
            this.prevPosZ = this.posZ

            this.fallTime++

            if (!this.hasNoGravity()) this.motionY -= 0.04

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ)
            this.motionX *= 0.98
            this.motionY *= 0.98
            this.motionZ *= 0.98

            if (!this.world.isRemote) {
                val selfPos = BlockPos(this)

                if (this.onGround) {
                    val stateInWorld = this.world.getBlockState(selfPos)

                    if (this.world.isAirBlock(BlockPos(this.posX, this.posY - 0.001, this.posZ)) &&
                            BlockFalling.canFallThrough(this.world.getBlockState(BlockPos(this.posX, this.posY - 0.001, this.posZ)))) {
                        this.onGround = false
                        return
                    }

                    this.motionX *= 0.7
                    this.motionZ *= 0.7
                    this.motionY *= -0.5

                    if (stateInWorld.block != Blocks.PISTON_EXTENSION) {
                        this.setDead()

                        if (block is BlockFalling) block.onBroken(this.world, selfPos)

                        if (this.shouldDropItem && this.world.gameRules.getBoolean("doTileDrops"))
                            this.entityDropItem(ItemStack(block, 1, block.damageDropped(this.block)), 0.0f)
                    }

                } else if (this.fallTime > 100 && (selfPos.y < 1 || selfPos.y > 256) || this.fallTime > 600) {
                    if (this.shouldDropItem && this.world.gameRules.getBoolean("doTileDrops"))
                        this.entityDropItem(ItemStack(block, 1, block.damageDropped(this.block)), 0.0f)

                    this.setDead()
                }
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    override fun writeEntityToNBT(compound: NBTTagCompound) {
        val block = this.block.block
        val rl = Block.REGISTRY.getNameForObject(block)
        compound.setString("Block", rl.toString())
        compound.setByte("Data", block.getMetaFromState(this.block).toByte())
        compound.setInteger("Time", this.fallTime)
        compound.setBoolean("DropItem", this.shouldDropItem)

        if (this.tileEntityData != null) compound.setTag("TileEntityData", this.tileEntityData!!)
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    override fun readEntityFromNBT(compound: NBTTagCompound) {
        val i = compound.getByte("Data").toInt() and 255

        val block = (if (compound.hasKey("Block", 8)) Block.getBlockFromName(compound.getString("Block"))
        else if (compound.hasKey("TileID", 99)) Block.getBlockById(compound.getInteger("TileID"))
        else Block.getBlockById(compound.getByte("Tile").toInt() and 255)) ?: Blocks.AIR
        this.block = block.getStateFromMeta(i)

        this.fallTime = compound.getInteger("Time")

        if (compound.hasKey("DropItem", 99)) this.shouldDropItem = compound.getBoolean("DropItem")

        if (compound.hasKey("TileEntityData", 10)) this.tileEntityData = compound.getCompoundTag("TileEntityData")

        if (block == null || block.defaultState.material == Material.AIR) {
            this.block = Blocks.SAND.defaultState
        }
    }

    override fun addEntityCrashInfo(category: CrashReportCategory) {
        super.addEntityCrashInfo(category)

        val block = this.block.block
        category.addCrashSection("Imitating block ID", Block.getIdFromBlock(block))
        category.addCrashSection("Imitating block data", block.getMetaFromState(this.block))
    }

    /**
     * Return whether this entity should be rendered as on fire.
     */
    @SideOnly(Side.CLIENT)
    override fun canRenderOnFire(): Boolean {
        return false
    }

    override fun ignoreItemEntityData(): Boolean {
        return true
    }

    companion object {
        private val ORIGIN = EntityDataManager.createKey(EntityDroppingBlock::class.java, DataSerializers.BLOCK_POS)
        private val STATE = EntityDataManager.createKey(EntityDroppingBlock::class.java, DataSerializers.OPTIONAL_BLOCK_STATE)
    }
}
