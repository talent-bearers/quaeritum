package eladkay.quaeritum.api.animus;

import org.jetbrains.annotations.NotNull;

/**
 * @author WireSegal
 *         Created at 5:00 PM on 2/8/17.
 */
public enum EnumAnimusTier {
    VERDIS, LUCIS, FERRUS, ARGENTUS, ATLAS, QUAERITUS;

    @NotNull
    public static EnumAnimusTier fromMeta(int meta) {
        return values()[meta % values().length];
    }
}
