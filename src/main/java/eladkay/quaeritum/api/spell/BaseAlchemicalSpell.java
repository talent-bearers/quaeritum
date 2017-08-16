package eladkay.quaeritum.api.spell;

import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 *         Created at 10:17 AM on 7/2/17.
 *
 * A helpful class that takes an array and a callable lambda in its constructor, and uses those to implement
 */
public class BaseAlchemicalSpell implements IAlchemicalSpell {
    public interface SpellRunnable {
        void cast(@Nonnull EntityPlayer caster, int trailingAether, int totalSpells);
    }

    @Nonnull
    private final EnumSpellElement[] pattern;
    @Nonnull
    private final SpellRunnable onCast;
    @Nonnull
    private final EnumSpellType type;
    @Nonnull
    private final String locKey;

    public BaseAlchemicalSpell(@Nonnull EnumSpellElement[] pattern, @Nonnull EnumSpellType type, @Nonnull String locKey, @Nonnull SpellRunnable onCast) {
        this.pattern = pattern;
        this.onCast = onCast;
        this.type = type;
        this.locKey = locKey;
    }

    @Nonnull
    @Override
    public EnumSpellElement[] getPattern() {
        return pattern;
    }

    @Override
    public void performEffect(@Nonnull EntityPlayer player, int trailingAether, int totalSpells) {
        onCast.cast(player, trailingAether, totalSpells);
    }

    @Nonnull
    @Override
    public EnumSpellType getTypeOfSpell() {
        return type;
    }

    @Nonnull
    @Override
    public String getLocalizationKey() {
        return locKey;
    }
}
