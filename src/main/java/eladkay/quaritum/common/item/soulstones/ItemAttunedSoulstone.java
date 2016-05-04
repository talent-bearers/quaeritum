package eladkay.quaritum.common.item.soulstones;

import eladkay.quaritum.common.core.ItemNBTHelper;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNBT;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import java.util.List;

public class ItemAttunedSoulstone extends ItemMod {
    public ItemAttunedSoulstone() {
        super(LibNames.ATTUNED_SOULSTONE);
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltipIfShift(tooltip, () -> tooltip.add("Attuned to " + ItemNBTHelper.getString(stack, LibNBT.TAG_OWNER, "nobody") + "."));
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (ItemNBTHelper.getString(stack, LibNBT.TAG_UUID, null) == null)
            ItemNBTHelper.setString(stack, LibNBT.TAG_UUID, entityIn.getUniqueID().toString());
        if (ItemNBTHelper.getString(stack, LibNBT.TAG_OWNER, null) == null)
            ItemNBTHelper.setString(stack, LibNBT.TAG_OWNER, entityIn.getName());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (playerIn.isSneaking()) {
            ItemNBTHelper.removeEntry(itemStackIn, LibNBT.TAG_OWNER);
            ItemNBTHelper.removeEntry(itemStackIn, LibNBT.TAG_UUID);
            worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.item_armor_equip_iron, SoundCategory.PLAYERS, 1f, 1f);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
        }
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }
}
