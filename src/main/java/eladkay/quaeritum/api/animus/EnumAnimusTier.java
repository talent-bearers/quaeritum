package eladkay.quaeritum.api.animus;

import eladkay.quaeritum.api.spell.EnumSpellElement;
import net.minecraft.util.IStringSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author WireSegal
 *         Created at 5:00 PM on 2/8/17.
 */
public enum EnumAnimusTier implements IStringSerializable {

    VERDIS(.2, EnumSpellElement.WATER, EnumSpellElement.EARTH),
    LUCIS(.4, EnumSpellElement.FIRE, EnumSpellElement.AIR),
    FERRUS(.8, EnumSpellElement.METAL, EnumSpellElement.ENTROPY),
    ARGENTUS(1.6, EnumSpellElement.FORM, EnumSpellElement.FLOW),
    ATLAS(3.2, EnumSpellElement.CONNECTION, EnumSpellElement.SPIRIT),
    QUAERITUS(6.4, EnumSpellElement.AETHER, EnumSpellElement.SOUL);

    public final double awakenedFillPercentage;
    private final EnumSpellElement elementPrimary;
    private final EnumSpellElement elementSecondary;

    public final String oreName = "essence" + Arrays
            .stream(name().toLowerCase().split("_"))
            .map(EnumAnimusTier::capitalize)
            .collect(Collectors.joining());

    EnumAnimusTier(double awakenedFillPercentage, EnumSpellElement elementPrimary, EnumSpellElement elementSecondary) {
        this.awakenedFillPercentage = awakenedFillPercentage;
        this.elementPrimary = elementPrimary;
        this.elementSecondary = elementSecondary;
    }

    private static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    private static String decapitalize(final String line) {
        return Character.toLowerCase(line.charAt(0)) + line.substring(1);
    }

    @Override
    @NotNull
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    @NotNull
    public static EnumAnimusTier fromMeta(int meta) {
        return values()[meta % values().length];
    }

    public EnumSpellElement getElementPrimary() {
        return elementPrimary;
    }

    public EnumSpellElement getElementSecondary() {
        return elementSecondary;
    }
}
