package eladkay.quaeritum.common.entity

import com.google.common.collect.Lists
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.*
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.util.DamageSource
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.World

class EntityChaosborn : EntityMob {
    // implements IRangedAttackMob {
    //The Quality of the Soulstone dropped into the Temple of the Rift, in order to summon a Chaosborn.
    //Determines drops and difficulty.
    var quality = 0
    var range = 64f

    //Constructor that's actually supposed to be used.
    constructor(worldIn: World, quality: Int) : super(worldIn) {
        this.quality = quality
        setSize(1f, 1f)
    }

    constructor(world: World, quality: Int, x: Int, y: Int, z: Int) : this(world, quality, BlockPos(x, y, z))

    constructor(world: World, quality: Int, x: Double, y: Double, z: Double) : super(world) {
        setPosition(x, y, z)
        this.quality = quality
        setSize(1f, 1f)
    }

    constructor(worldIn: World, quality: Int, pos: BlockPos) : super(worldIn) {
        setPosition(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
        this.quality = quality
        setSize(1f, 1f)

    }

    //General constructor
    constructor(worldIn: World) : this(worldIn, 1) {

        this.tasks.addTask(1, EntityAISwimming(this))
        //this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.5D, false));
        this.tasks.addTask(3, EntityAIWander(this, 0.30))
        this.tasks.addTask(4, EntityAIWatchClosest(this, EntityPlayer::class.java, 8.0f))
        this.tasks.addTask(5, EntityAILookIdle(this))
        //  this.tasks.addTask(4, new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F));
        this.targetTasks.addTask(1, EntityAIHurtByTarget(this, false))
        this.targetTasks.addTask(2, EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer::class.java, true))


    }

    override fun applyPlayerInteraction(player: EntityPlayer?, vec: Vec3d?, hand: EnumHand?): EnumActionResult {
        player!!.sendMessage(TextComponentString("And his rarity is " + quality))
        return EnumActionResult.SUCCESS
    }

    override fun isEntityInvulnerable(source: DamageSource): Boolean {
        return source === DamageSource.ON_FIRE
    }

    override fun isAIDisabled(): Boolean {
        return false
    }

    override fun canDespawn(): Boolean {
        return false
    }

    //Botania code :p
    //Gets all player in a radius.
    private //	float range = 64F;
    val playersAround: List<EntityPlayer>
        get() {
            val source = source
            val l = world.getEntitiesWithinAABB<EntityPlayer>(EntityPlayer::class.java, AxisAlignedBB(source.x + 0.5 - range, source.y + 0.5 - range, source.z + 0.5 - range, source.x.toDouble() + 0.5 + range.toDouble(), source.y.toDouble() + 0.5 + range.toDouble(), source.z.toDouble() + 0.5 + range.toDouble()))
            val ret = Lists.newArrayList<EntityPlayer>()
            ret.addAll(l.filter({ player -> !player.isCreative }))
            return ret
        }

    //BlockPos of this entity.
    private val source: BlockPos
        get() = BlockPos(this.posX, this.posY, this.posZ)

    override fun applyEntityAttributes() {
        super.applyEntityAttributes()
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).baseValue = ((quality + 1) * 100).toDouble()
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).baseValue = 256.0
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).baseValue = 0.25
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).baseValue = 0.75
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).baseValue = ((quality + 1) * 5).toDouble()
    }

    //Used to apply status effects.
    override fun onLivingUpdate() {
        super.onLivingUpdate()
        playersAround.filter({ poorThing -> poorThing.posX > this.posX + 32 || poorThing.posY > this.posY + 32 || poorThing.posZ > this.posZ + 32 || poorThing.posX < this.posX - 32 || poorThing.posY < this.posY - 32 || poorThing.posZ < this.posZ - 32 }).forEach({ poorThing ->
            poorThing.addPotionEffect(PotionEffect(Potion.REGISTRY.getObject(ResourceLocation("minecraft:nausea"))!!, 10,
                    quality + 2))
            poorThing.addPotionEffect(PotionEffect(Potion.REGISTRY.getObject(ResourceLocation("minecraft:wither"))!!, 10, quality + 2))
        })
    }

    override fun attackEntityAsMob(entityIn: Entity): Boolean {
        if (entityIn.isEntityInvulnerable(DamageSource.ON_FIRE)) return false
        entityIn.setFire(10)
        if (entityIn is EntityPlayer) {
            entityIn.inventory.damageArmor(15f)
        }
        return super.attackEntityAsMob(entityIn)
    }

    //Attack an Entity with a ranged attack.
    //  @Override
    fun attackEntityWithRangedAttack(p_82196_1_: EntityLivingBase,
                                     p_82196_2_: Float) {
        val entityarrow = EntityArrowFire(this.world, this, p_82196_1_, 1.6f, (14 - this.world.difficulty.id * 4).toFloat())
        val i = 3 //Equivalent to a power 3 bow.
        val j = 1 //Equivalent to a punch 2 bow.
        entityarrow.damage = (p_82196_2_ * 2.0f).toDouble() + this.rand.nextGaussian() * 0.25 + (this.world.difficulty.id.toFloat() * 0.11f).toDouble()
        entityarrow.damage = entityarrow.damage + i.toDouble() * 0.5 + 0.5
        entityarrow.setKnockbackStrength(j)
        entityarrow.setFire(100)

        //  this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(entityarrow)

    }


}
