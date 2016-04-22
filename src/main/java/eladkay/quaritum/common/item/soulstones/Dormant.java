package eladkay.quaritum.common.item.soulstones;

import eladkay.quaritum.client.core.TooltipHelper;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class Dormant extends ItemMod {
    public Dormant() {
        super(LibNames.DORMANT_SOULSTONE);
        setMaxStackSize(1);
        //addPropertyOverride();
    }

    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
        if (itemStack.getTagCompound() != null && !itemStack.getTagCompound().getBoolean(LibNames.TAG_OPPERSSIVE)) {
            if (GuiScreen.isShiftKeyDown()) {
                if (itemStack.getTagCompound().getBoolean(LibNames.TAG_ATTUNED)) {
                    String owner = itemStack.getTagCompound().getString(LibNames.TAG_OWNER);
                    list.add("Attuned to: " + owner);
                } else {
                    list.add("Not attuned");
                }
            } else {
                list.add(TooltipHelper.local("misc.quaritum.shiftForInfo").replaceAll("&", "\u00a7"));
            }

        }
    }

   /* @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if(world.getBlockState(pos).getBlock() instanceof BlockBed || world.getBlockState(pos).getBlock().isBed(world.getBlockState(pos), world, pos, player)) {
            for(EntityPlayer obj : world.playerEntities) {
                if(obj.getBedLocation() != null && obj.getBedLocation().equals(pos)) {
                    NBTTagCompound tag = stack.getTagCompound();
                    if (!tag.getBoolean(LibNames.TAG_ATTUNED) && !tag.getBoolean(LibNames.TAG_OPPERSSIVE)) {
                        tag.setString(LibNames.TAG_OWNER, obj.getName());
                        tag.setUniqueId(LibNames.TAG_UUID, obj.getGameProfile().getId());
                        tag.setBoolean(LibNames.TAG_ATTUNED, true);
                        stack.setTagCompound(tag);
                        stack.setStackDisplayName(TextFormatting.RESET + TooltipHelper.local("tile.quaritum:attunedSoulstone.name"));
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
        return EnumActionResult.FAIL;
    }*/


    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        NBTTagCompound tag = itemStackIn.getTagCompound();
        if (!tag.getBoolean(LibNames.TAG_ATTUNED) && !tag.getBoolean(LibNames.TAG_OPPERSSIVE)) {
            tag.setString(LibNames.TAG_OWNER, playerIn.getName());
            tag.setUniqueId(LibNames.TAG_UUID, playerIn.getGameProfile().getId());
            tag.setBoolean(LibNames.TAG_ATTUNED, true);
            itemStackIn.setTagCompound(tag);
            itemStackIn.setStackDisplayName(TextFormatting.RESET + TooltipHelper.local("tile.quaritum:attunedSoulstone.name"));
            return new ActionResult(EnumActionResult.PASS, itemStackIn);
        }
        return new ActionResult(EnumActionResult.FAIL, itemStackIn);

    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        } /*else if (!(((EntityItem) entityIn).getEntityItem().getTagCompound().getBoolean(LibNames.TAG_ATTUNED)) && entityIn.isInWater() && !(((EntityItem) entityIn).getEntityItem().getTagCompound().getBoolean(LibNames.TAG_OPPERSSIVE))) {
            NBTTagCompound tag = stack.getTagCompound();
            tag.setBoolean(LibNames.TAG_OPPERSSIVE, true);
            ((EntityItem) entityIn).getEntityItem().setTagCompound(tag);
            ((EntityItem) entityIn).getEntityItem().setStackDisplayName(TextFormatting.RESET + TooltipHelper.local("tile.quaritum:oppersiveSoulstone.name"));
        }*/
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if (entityItem.getEntityItem().getTagCompound() == null) {
            entityItem.getEntityItem().setTagCompound(new NBTTagCompound());
        } else if (!(entityItem.getEntityItem().getTagCompound().getBoolean(LibNames.TAG_ATTUNED)) && entityItem.isInWater() && !(entityItem.getEntityItem().getTagCompound().getBoolean(LibNames.TAG_OPPERSSIVE))) {
            NBTTagCompound tag = entityItem.getEntityItem().getTagCompound();
            tag.setBoolean(LibNames.TAG_OPPERSSIVE, true);
            entityItem.getEntityItem().setTagCompound(tag);
            entityItem.getEntityItem().setStackDisplayName(TextFormatting.RESET + TooltipHelper.local("tile.quaritum:oppersiveSoulstone.name"));
        }
        return false;
    }
}
