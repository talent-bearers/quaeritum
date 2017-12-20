package eladkay.quaeritum.api.spell;

import java.util.Locale;

/**
 * @author WireSegal
 * Created at 11:20 PM on 7/1/17.
 */
public enum EnumSpellType {
    /**
     * EVOCATION is creating something from a simple pattern
     */
    EVOCATION,
    /**
     * INCARNATION is creating something new
     */
    INCARNATION,
    /**
     * ALTERATION is changing or destroying something that exists
     */
    ALTERATION,
    /**
     * RESTORATION is creating what once was
     */
    RESTORATION;

    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }
}
