package eladkay.quaritum.common.entity;

import eladkay.quaritum.common.block.ModBlocks;
import eladkay.quaritum.common.block.flowers.BlockAnimusFlower;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class EntityChaosborn extends EntityMob implements IRangedAttackMob {
    private int quality;

    //public static final IAttribute myAttribute = new RangedAttribute();
    // private static final DataParameter<Integer> QUALITY = new DataParameter<Integer>();
    public EntityChaosborn(World worldIn, int quality) {
        super(worldIn);
        this.quality = quality;

    }

    //Obligatory.
    public EntityChaosborn(World worldIn) {
        this(worldIn, 0);
    }

    public EntityChaosborn(World worldIn, int quality, BlockPos pos) {
        super(worldIn);
        setPosition(pos.getX(), pos.getY(), pos.getZ());
        this.quality = quality;

    }

    public EntityChaosborn(World worldIn, BlockPos pos) {
        this(worldIn, 0, pos);
    }

    public EntityChaosborn(World world, int x, int y, int z) {
        this(world, new BlockPos(x, y, z));

    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D * (quality > 0 ? quality : 1));
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand p_184645_2_, ItemStack stack) {
        player.addChatComponentMessage(new TextComponentString("Health: " + getHealth()));
        if (player.isSneaking()) quality++;
        return super.processInteract(player, p_184645_2_, stack);
    }

    @Override
    protected void dropLoot(boolean wasRecentlyHit, int looting, DamageSource source) {
        if (source.getSourceOfDamage() instanceof EntityPlayer && wasRecentlyHit) {
            ItemStack itemstack = new ItemStack(ModBlocks.flower, Math.max(Math.min(3, looting), 1) * quality + 1, BlockAnimusFlower.Variants.ARCANE.ordinal());
            this.entityDropItem(itemstack, 0.0F);
        }
        super.dropLoot(wasRecentlyHit, looting, source);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        // this.dataWatcher.register(SECOND_HEAD_TARGET, Integer.valueOf(0));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
        super.readEntityFromNBT(nbtTagCompound);
        quality = nbtTagCompound.getInteger("quality");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
        super.writeEntityToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("quality", quality);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 40, 20.0F));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entityLivingBase, float v) {
        entityLivingBase.heal(-4);
    }
}
