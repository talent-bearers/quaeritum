package eladkay.quaritum.common.item.soulstones;

import eladkay.quaritum.common.item.ModItems;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemDormantSoulstone extends ItemMod {
    public ItemDormantSoulstone() {
        super(LibNames.DORMANT_SOULSTONE);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (playerIn.isSneaking())
            return new ActionResult<>(EnumActionResult.SUCCESS, new ItemStack(ModItems.attuned));
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if (entityItem.isInWater()) {
            entityItem.setEntityItemStack(new ItemStack(ModItems.oppressive));
        }
        return false;
    }
}
