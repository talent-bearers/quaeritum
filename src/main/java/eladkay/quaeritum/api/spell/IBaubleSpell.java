package eladkay.quaeritum.api.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

public interface IBaubleSpell {

    @NotNull
    ItemStack getIconStack(@NotNull ItemStack bauble, int slot);

    @NotNull
    String getSpellName(@NotNull ItemStack bauble, int slot);

    default int getCooldown(@NotNull EntityPlayer player, @NotNull ItemStack bauble, int slot) {
        return 0;
    }

    boolean onCast(@NotNull EntityPlayer player, @NotNull ItemStack bauble, int slot);

    default void onCooldownTick(@NotNull EntityPlayer player, @NotNull ItemStack bauble, int slot, int cooldownRemaining) {
        // NO-OP
    }
}
