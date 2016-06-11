package eladkay.quaritum.common.entity;

import eladkay.quaritum.common.item.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class EntityChaosborn extends EntityMob implements IRangedAttackMob {
    //The Quality of the Soulstone dropped into the Temple of the Rift, in order to summon a Chaosborn.
    //Determines drops and difficulty.
    public int quality = 0;
    public float range = 64;

    //Constructor that's actually supposed to be used.
    public EntityChaosborn(World worldIn, int quality) {
        super(worldIn);
        this.quality = quality;
        setSize(1, 1);
    }

    public EntityChaosborn(World world, int quality, int x, int y, int z) {
        this(world, quality, new BlockPos(x, y, z));
    }

    public EntityChaosborn(World world, int quality, double x, double y, double z) {
        super(world);
        setPosition(x, y, z);
        this.quality = quality;
        setSize(1, 1);
    }

    public EntityChaosborn(World worldIn, int quality, BlockPos pos) {
        super(worldIn);
        setPosition(pos.getX(), pos.getY(), pos.getZ());
        this.quality = quality;
        setSize(1, 1);

    }

    //General constructor
    public EntityChaosborn(World worldIn) {
        this(worldIn, 1);

        this.tasks.addTask(1, new EntityAISwimming(this));
        //this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.5D, false));
        this.tasks.addTask(3, new EntityAIWander(this, 0.30D));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.tasks.addTask(4, new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));


    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    //Botania code :p
    //Gets all player in a radius.
    private List<EntityPlayer> getPlayersAround() {
        BlockPos source = getSource();
        //	float range = 64F;
        return worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(source.getX()
                + 0.5 - range, source.getY() + 0.5 - range, source.getZ() + 0.5 - range, source.getX() + 0.5 + range, source.getY() + 0.5 + range, source.getZ() + 0.5 + range));
    }

    //BlockPos of this entity.
    private BlockPos getSource() {
        return new BlockPos(this.posX, this.posY, this.posZ);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((quality + 1) * 100);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(256.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.75D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue((quality + 1) * 5);
    }

    //Unused.
    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
    }

    //Used to apply status effects.
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        getPlayersAround().stream().filter(poorThing -> poorThing.posX > (this.posX + 32) || poorThing.posY > (this.posY + 32) || poorThing.posZ > (this.posZ + 32) || poorThing.posX < (this.posX - 32) || poorThing.posY < (this.posY - 32) || poorThing.posZ < (this.posZ - 32)).forEach(poorThing -> {
            poorThing.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(new ResourceLocation("minecraft:nausea")), 10,
                    (quality +
                            2)));
            poorThing.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(new ResourceLocation
                    ("minecraft:wither")), 10, (quality + 2)));
        });
    }

    //Specifies item drops.
    @Override
    protected Item getDropItem() {

        //And here, the world of Magical flora begins.
        return ModItems.altas;
    }

    //Attack an Entity with a ranged attack.
    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_,
                                             float p_82196_2_) {
        EntityArrowFire entityarrow = new EntityArrowFire(this.worldObj, this, p_82196_1_, 1.6F, (float) (14 - this.worldObj.getDifficulty().getDifficultyId() * 4));
        int i = 3; //Equivalent to a power 3 bow.
        int j = 1; //Equivalent to a punch 2 bow.
        entityarrow.setDamage((double) (p_82196_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (double) ((float) this.worldObj.getDifficulty().getDifficultyId() * 0.11F));
        entityarrow.setDamage(entityarrow.getDamage() + (double) i * 0.5D + 0.5D);
        entityarrow.setKnockbackStrength(j);
        entityarrow.setFire(100);

        //  this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.worldObj.spawnEntityInWorld(entityarrow);

    }


}