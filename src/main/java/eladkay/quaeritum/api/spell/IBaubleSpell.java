package eladkay.quaeritum.api.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IBaubleSpell {

    @Nonnull
    ItemStack getIconStack(@Nonnull ItemStack bauble, int slot);

    @Nonnull
    String getSpellName(@Nonnull ItemStack bauble, int slot);

    default int getCooldown(@Nonnull EntityPlayer player, @Nonnull ItemStack bauble, int slot) {
        return 0;
    }

    boolean onCast(@Nonnull EntityPlayer player, @Nonnull ItemStack bauble, int slot);

    default void onCooldownTick(@Nonnull EntityPlayer player, @Nonnull ItemStack bauble, int slot, int cooldownRemaining) {
        // NO-OP
    }
}
