package eladkay.quaeritum.api.spell;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 *         Created at 3:52 AM on 7/2/17.
 *
 * A simple data class to hold both an alchemical spell and an integer.
 */
public final class SpellInfo {
    @Nonnull
    private final IAlchemicalSpell spell;
    private final int trailingAether;

    public SpellInfo(@Nonnull IAlchemicalSpell spell, int trailingAether) {
        this.spell = spell;
        this.trailingAether = trailingAether;
    }

    @Nonnull
    public IAlchemicalSpell getSpell() {
        return spell;
    }

    public int getTrailingAether() {
        return trailingAether;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpellInfo spellInfo = (SpellInfo) o;

        return trailingAether == spellInfo.trailingAether && spell.equals(spellInfo.spell);
    }

    @Override
    public int hashCode() {
        int result = spell.hashCode();
        result = 31 * result + trailingAether;
        return result;
    }
}
