package eladkay.quaeritum.api.spell;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.teamwizardry.librarianlib.common.util.ItemNBTHelper;
import eladkay.quaeritum.api.lib.LibMisc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ISpellProvider extends IBauble {
    @Nullable
    IBaubleSpell getSpell(@Nonnull ItemStack bauble, int slot);

    String TAG_COOLDOWN = LibMisc.MOD_ID + ".cooldown";
    String TAG_MAX_COOLDOWN = LibMisc.MOD_ID + ".maxcooldown";

    static void setCooldown(@Nonnull ItemStack stack, int cooldown) {
        setCooldown(stack, cooldown, false);
    }

    static void setCooldown(@Nonnull ItemStack stack, int cooldown, boolean max) {
        ItemNBTHelper.setInt(stack, TAG_COOLDOWN, cooldown);
        if (cooldown == 0)
            ItemNBTHelper.setInt(stack, TAG_MAX_COOLDOWN, 0);
        else if (max)
            ItemNBTHelper.setInt(stack, TAG_MAX_COOLDOWN, cooldown);
    }

    static int getCooldown(@Nonnull ItemStack stack) {
        return ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0);
    }
    static int getMaxCooldown(@Nonnull ItemStack stack) {
        return ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0);
    }

    static void tickCooldown(@Nonnull EntityPlayer player, @Nonnull ItemStack stack, int baubleSlot) {
        int cooldown = getCooldown(stack);
        if (cooldown == 0) return;
        cooldown--;
        IBaubleSpell spell = ((ISpellProvider) stack.getItem()).getSpell(stack, baubleSlot);
        if (spell != null)
            spell.onCooldownTick(player, stack, baubleSlot, cooldown);
        setCooldown(stack, cooldown);
    }

    @Nonnull
    static BaubleType baubleFromSlot(int slot) {
        switch (slot) {
            case 0:
                return BaubleType.AMULET;
            case 1:
            case 2:
                return BaubleType.RING;
            case 3:
                return BaubleType.BELT;
            case 4:
                return BaubleType.HEAD;
            case 5:
                return BaubleType.BODY;
            case 6:
                return BaubleType.CHARM;
            default:
                return BaubleType.TRINKET;
        }
    }
}
