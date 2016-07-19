package eladkay.quaritum.common.item.misc;

import com.google.common.collect.Multimap;
import eladkay.quaritum.api.animus.AnimusHelper;
import eladkay.quaritum.api.util.ItemNBTHelper;
import eladkay.quaritum.api.util.Vector3;
import eladkay.quaritum.common.Quaritum;
import eladkay.quaritum.common.core.QuaritumMethodHandles;
import eladkay.quaritum.common.core.RayHelper;
import eladkay.quaritum.common.item.base.ItemModSword;
import eladkay.quaritum.common.lib.LibMaterials;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemWorldBlade extends ItemModSword {

    public static final String TAG_TELEPORTED = "teleportTicks";

    boolean shouldUseElucentParticles = true; //lol
    public ItemWorldBlade() {
        super(LibNames.WORLD_BLADE, LibMaterials.MYSTIC);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (worldIn.isRemote && entityIn instanceof EntityPlayer && stack.getItemDamage() > 0 && AnimusHelper.Network.requestAnimus((EntityPlayer) entityIn, 2, 0, true))
            stack.setItemDamage(stack.getItemDamage() - 1);

        int ticks = ItemNBTHelper.getInt(stack, TAG_TELEPORTED, 0);
        if (ticks > 0 && entityIn instanceof EntityLivingBase)
            QuaritumMethodHandles.setSwingTicks((EntityLivingBase) entityIn, ticks);
        ItemNBTHelper.removeEntry(stack, TAG_TELEPORTED);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (state.getBlockHardness(worldIn, pos) > 0)
            AnimusHelper.damageItem(stack, 1, entityLiving, 1, 0);
        return true;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        AnimusHelper.damageItem(stack, 1, attacker, 1, 0);
        return true;
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if (entityLiving instanceof EntityPlayer && ((EntityPlayer) entityLiving).getCooldownTracker().hasCooldown(this)) return false;

        Vector3 pos = Vector3.ZERO;
        boolean hitEntity = false;

        Entity hit = RayHelper.getEntityLookedAt(entityLiving, 8);
        if (hit != null) {
            pos = Vector3.fromEntity(hit);
            hitEntity = true;
        } else {
            RayTraceResult result = RayHelper.raycast(entityLiving, 8);
            if (result == null) {
                pos = Vector3.fromEntity(entityLiving).add(new Vector3(entityLiving.getLookVec()).multiply(8));
            } else {
                switch (result.typeOfHit) {
                    case MISS:
                        pos = Vector3.fromEntity(entityLiving).add(new Vector3(entityLiving.getLookVec()).multiply(8));
                        break;
                    case BLOCK:
                        pos = new Vector3(result.getBlockPos()).add(new Vector3(result.sideHit.getDirectionVec())).add(Vector3.CENTER);
                        break;
                    case ENTITY:
                        pos = Vector3.fromEntity(result.entityHit);
                        hitEntity = true;
                        break;
                }
            }
        }

        pos = new Vector3(pos.x, Math.max(pos.y, entityLiving.posY), pos.z);
        BlockPos blockPos = new BlockPos(pos.x, pos.y, pos.z);

        if (entityLiving.worldObj.getBlockState(blockPos.up()).getCollisionBoundingBox(entityLiving.worldObj, blockPos.up()) == null) {
            if (entityLiving.worldObj.isRemote) for (int i = 0; i < 100; i++)
                entityLiving.worldObj.spawnParticle(EnumParticleTypes.PORTAL,
                        entityLiving.posX + (entityLiving.worldObj.rand.nextDouble() - 0.5D) * (double)entityLiving.width,
                        entityLiving.posY + entityLiving.worldObj.rand.nextDouble() * (double)entityLiving.height - 0.25D,
                        entityLiving.posZ + (entityLiving.worldObj.rand.nextDouble() - 0.5D) * (double)entityLiving.width,
                        (entityLiving.worldObj.rand.nextDouble() - 0.5D) * 2.0D, -entityLiving.worldObj.rand.nextDouble(),
                        (entityLiving.worldObj.rand.nextDouble() - 0.5D) * 2.0D);

            entityLiving.setPosition(pos.x, pos.y, pos.z);
            entityLiving.fallDistance = 0;

            if (entityLiving.worldObj.isRemote)
                for (int i = 0; i < 100; i++) {
                  if(!shouldUseElucentParticles)
                   entityLiving.worldObj.spawnParticle(EnumParticleTypes.PORTAL,
                            entityLiving.posX + (entityLiving.worldObj.rand.nextDouble() - 0.5D) * (double) entityLiving.width,
                            entityLiving.posY + entityLiving.worldObj.rand.nextDouble() * (double) entityLiving.height - 0.25D,
                            entityLiving.posZ + (entityLiving.worldObj.rand.nextDouble() - 0.5D) * (double) entityLiving.width,
                            (entityLiving.worldObj.rand.nextDouble() - 0.5D) * 2.0D,
                            -entityLiving.worldObj.rand.nextDouble(),
                            (entityLiving.worldObj.rand.nextDouble() - 0.5D) * 2.0D);
                    else
                    Quaritum.proxy.spawnStafflikeParticles(
                            entityLiving.worldObj, //world
                            entityLiving.posX + (entityLiving.worldObj.rand.nextDouble() - 0.5D) * (double) entityLiving.width, //x
                            entityLiving.posY + entityLiving.worldObj.rand.nextDouble() * (double) entityLiving.height - 0.25D, //y
                            entityLiving.posZ + (entityLiving.worldObj.rand.nextDouble() - 0.5D) * (double) entityLiving.width //z
                    );
                }

            entityLiving.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1f, 1f);


            if (!hitEntity)
                ItemNBTHelper.setInt(stack, TAG_TELEPORTED, QuaritumMethodHandles.getSwingTicks(entityLiving));

            AnimusHelper.damageItem(stack, 1, entityLiving, 1, 0);

            if (entityLiving instanceof EntityPlayer && entityLiving.worldObj.isRemote)
                ((EntityPlayer) entityLiving).getCooldownTracker().setCooldown(this, (int) ((EntityPlayer) entityLiving).getCooldownPeriod());
        }

        return false;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName());
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
        }

        return multimap;
    }

    @Nonnull
    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }
}
