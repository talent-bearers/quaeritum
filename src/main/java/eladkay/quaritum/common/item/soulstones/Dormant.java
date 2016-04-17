package eladkay.quaritum.common.item.soulstones;

import eladkay.quaritum.client.core.TooltipHelper;
import eladkay.quaritum.common.animus.ISoulstone;
import eladkay.quaritum.common.item.base.ItemMod;
import eladkay.quaritum.common.lib.LibNames;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class Dormant extends ItemMod implements ISoulstone {
    public Dormant() {
        super(LibNames.DORMANT_SOULSTONE);
        setMaxStackSize(1);
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

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        NBTTagCompound tag = itemStackIn.getTagCompound();
        if (!tag.getBoolean(LibNames.TAG_ATTUNED) && !tag.getBoolean(LibNames.TAG_OPPERSSIVE)) {
            tag.setString(LibNames.TAG_OWNER, playerIn.getName());
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
        } else if (!(stack.getTagCompound().getBoolean(LibNames.TAG_ATTUNED)) && entityIn.isInWater() && !(stack.getTagCompound().getBoolean(LibNames.TAG_OPPERSSIVE))) {
            NBTTagCompound tag = stack.getTagCompound();
            tag.setBoolean(LibNames.TAG_OPPERSSIVE, true);
            stack.setTagCompound(tag);
            stack.setStackDisplayName(TextFormatting.RESET + TooltipHelper.local("tile.quaritum:oppersiveSoulstone.name"));
        }
    }

    @Override
    public int getAnimusLevel(ItemStack stack) {
        return 0;
    }

    @Override
    public ItemStack deductAnimus(ItemStack stack, int amount) {
        return stack;
    }

    @Override
    public ItemStack addAnimus(ItemStack stack, int amount) {
        return stack;
    }

    @Override
    public int getMaxAnimus() {
        return 0;
    }

    @Override
    public boolean isRechargeable() {
        return false;
    }

    @Override
    public void doPassive(ItemStack stack) {
    }
}
