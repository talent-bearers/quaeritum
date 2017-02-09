package eladkay.quaeritum.api.animus;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author WireSegal
 *         Created at 5:00 PM on 2/8/17.
 */
public enum EnumAnimusTier {

    VERDIS(.1),
    LUCIS(.2),
    FERRUS(.3),
    ARGENTUS(.6),
    ATLAS(.75),
    QUAERITUS(1);

    public final double awakenedFillPercentage;

    public final String oreName = decapitalize(Arrays
            .stream(name().toLowerCase().split("_"))
            .map(EnumAnimusTier::capitalize)
            .collect(Collectors.joining()));

    EnumAnimusTier(double awakenedFillPercentage) {
        this.awakenedFillPercentage = awakenedFillPercentage;
    }

    private static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    private static String decapitalize(final String line) {
        return Character.toLowerCase(line.charAt(0)) + line.substring(1);
    }


    @NotNull
    public static EnumAnimusTier fromMeta(int meta) {
        return values()[meta % values().length];
    }
}
