package eladkay.quaeritum.api.spell;

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

    final class Helper {
        private Helper() {}

        private static final String TAG_COOLDOWN = LibMisc.MOD_ID + ".cooldown";

        public static void setCooldown(@Nonnull ItemStack stack, int cooldown) {
            ItemNBTHelper.setInt(stack, TAG_COOLDOWN, cooldown);
        }

        public static int getCooldown(@Nonnull ItemStack stack) {
            return ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0);
        }

        public static void tickCooldown(@Nonnull EntityPlayer player, @Nonnull ItemStack stack, int baubleSlot) {
            int cooldown = getCooldown(stack);
            if (cooldown == 0) return;
            cooldown--;
            IBaubleSpell spell = ((ISpellProvider) stack.getItem()).getSpell(stack, baubleSlot);
            if (spell != null)
                spell.onCooldownTick(player, stack, baubleSlot, cooldown);
            setCooldown(stack, cooldown);
        }
    }
}
