package eladkay.quaeritum.common.entity

import net.minecraft.entity.EntityLiving
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.IRangedAttackMob
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.util.math.MathHelper

class EntityAIArrowAttack(par1IRangedAttackMob: IRangedAttackMob, par2: Double, par4: Int, par5: Int, par6: Float) : EntityAIBase() {
    private val entityHost: EntityLiving

    private val rangedAttackEntityHost: IRangedAttackMob
    private var attackTarget: EntityLivingBase? = null

    private var rangedAttackTime: Int = 0
    private var entityMoveSpeed: Double = 0.toDouble()
    private var field_96561_g: Int = 0

    private var maxRangedAttackTime: Int = 0
    private var field_96562_i: Float = 0.toFloat()
    private var field_82642_h: Float = 0.toFloat()

    constructor(par1IRangedAttackMob: IRangedAttackMob, par2: Double, par4: Int, par5: Float) : this(par1IRangedAttackMob, par2, par4, par4, par5) {
    }

    init {
        this.rangedAttackTime = -1

        if (par1IRangedAttackMob !is EntityLivingBase) {
            throw IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob")
        } else {
            this.rangedAttackEntityHost = par1IRangedAttackMob
            this.entityHost = par1IRangedAttackMob as EntityLiving
            this.entityMoveSpeed = par2
            this.field_96561_g = par4
            this.maxRangedAttackTime = par5
            this.field_96562_i = par6
            this.field_82642_h = par6 * par6
            this.mutexBits = 3
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    override fun shouldExecute(): Boolean {
        val entitylivingbase = this.entityHost.attackTarget

        if (entitylivingbase == null) {
            return false
        } else {
            this.attackTarget = entitylivingbase
            return true
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    override fun continueExecuting(): Boolean {
        return this.shouldExecute() || !this.entityHost.navigator.noPath()
    }

    /**
     * Resets the task
     */
    override fun resetTask() {
        this.attackTarget = null
        this.rangedAttackTime = -1
    }

    /**
     * Updates the task
     */
    override fun updateTask() {
        val d0 = this.entityHost.getDistanceSq(this.attackTarget!!.posX, this.attackTarget!!.posY, this.attackTarget!!.posZ)
        val flag = this.entityHost.entitySenses.canSee(this.attackTarget!!)

        this.entityHost.navigator.tryMoveToEntityLiving(this.attackTarget!!, this.entityMoveSpeed)

        this.entityHost.lookHelper.setLookPositionWithEntity(this.attackTarget!!, 30.0f, 30.0f)
        val f: Float

        if (--this.rangedAttackTime == 0) {
            if (d0 > this.field_82642_h || !flag) {
                return
            }

            f = MathHelper.sqrt_double(d0) / this.field_96562_i
            var f1 = f

            if (f < 0.1f) {
                f1 = 0.1f
            }

            if (f1 > 1.0f) {
                f1 = 1.0f
            }

            this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget!!, f1)
            this.rangedAttackTime = MathHelper.floor_float(f * (this.maxRangedAttackTime - this.field_96561_g) + this.field_96561_g)
        } else if (this.rangedAttackTime < 0) {
            f = MathHelper.sqrt_double(d0) / this.field_96562_i
            this.rangedAttackTime = MathHelper.floor_float(f * (this.maxRangedAttackTime - this.field_96561_g) + this.field_96561_g)
        }
    }
}