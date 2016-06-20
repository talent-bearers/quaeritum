package eladkay.quaritum.common.item.misc;

import eladkay.quaritum.client.core.TooltipHelper;
import eladkay.quaritum.common.item.base.ItemModSword;
import eladkay.quaritum.common.lib.LibMaterials;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ItemWorldBlade extends ItemModSword {
    public ItemWorldBlade() {
        super(LibNames.WORLD_BLADE, LibMaterials.MYSTIC);
        addPropertyOverride(new ResourceLocation("blocking"), (stack, worldIn, entityIn) -> entityIn != null && entityIn.isSneaking() && (((EntityPlayer) entityIn).inventory.offHandInventory.length == 0 || !ItemStack.areItemStacksEqual(((EntityPlayer) entityIn).inventory.offHandInventory[0], stack)) ? 1.0F : 0.0F);
      /*  this.addPropertyOverride(new ResourceLocation("blocking"), (stack, worldIn, entityIn) -> {
            if (entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack) return 1f;
            else return 0f;
        });*/
    }

    private static boolean isInUse(EntityPlayer player) {
        return player.isSneaking();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            tooltip.add("Sneak in order to become invisible and resistant to damage");
            tooltip.add("Infused with elemental power");
            tooltip.add("Has to be in your main hand");
        });
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        EntityPlayer player = (EntityPlayer) entityIn;
        if (!isSelected || (player.inventory.offHandInventory.length > 0 && ItemStack.areItemStacksEqual(player.inventory.offHandInventory[0], stack)))
            return;
        if (player.isSneaking()) {
            Random rand = new Random();
            BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
            player.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(new ResourceLocation("minecraft:invisibility")), 10,
                    1));
            player.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(new ResourceLocation
                    ("minecraft:resistance")), 10, 5));
            if (new Random().nextInt() % 5 == 0) {
                // Quartium.proxy.spawnParticleMagixFX(player.getEntityWorld(), player.posX+2.0*(random.nextFloat()-0.5), player.posY+2.0*(random.nextFloat()-0.5)+1.0, player.posZ+2.0*(random.nextFloat()-0.5), player.posX, player.posY+1.0, player.posZ, 1, 1, 1);
                try {
                    for (int i = 0; i < 8; i++) {
                        worldIn.spawnParticle(EnumParticleTypes.SPELL_WITCH, pos.getX() + 0.5, pos.getY() + rand.nextDouble(), pos.getZ() + 0.5, 1, 1, 1);
                        worldIn.spawnParticle(EnumParticleTypes.SPELL_WITCH, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 1, 1, 1);
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
          /* else {
               Quartium.proxy.spawnParticleMagixFX(player.getEntityWorld(), player.posX+2.0*(random.nextFloat()-0.5), player.posY+2.0*(random.nextFloat()-0.5)+1.0, player.posZ+2.0*(random.nextFloat()-0.5), player.posX, player.posY+1.0, player.posZ, 1, 1, 1);
           }*/

        }
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return !isInUse((EntityPlayer) target) && super.hitEntity(stack, target, attacker);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        return isInUse(player);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BLOCK;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        // playerIn.setSneaking(false);
        playerIn.setSneaking(true);
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return false;
    }

}
