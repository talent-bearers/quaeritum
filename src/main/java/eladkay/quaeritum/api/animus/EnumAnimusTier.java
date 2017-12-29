package eladkay.quaeritum.api.animus;

import eladkay.quaeritum.api.spell.EnumSpellElement;
import net.minecraft.util.IStringSerializable;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author WireSegal
 * Created at 5:00 PM on 2/8/17.
 */
public enum EnumAnimusTier implements IStringSerializable {

    VERDIS(.2, EnumSpellElement.WATER, EnumSpellElement.EARTH, new Color(0x40FF40)),
    LUCIS(.4, EnumSpellElement.FIRE, EnumSpellElement.AIR, new Color(0xFFA030)),
    FERRUS(.8, EnumSpellElement.METAL, EnumSpellElement.ENTROPY, new Color(0x303030)),
    ARGENTUS(1.6, EnumSpellElement.FORM, EnumSpellElement.FLOW, new Color(0xFFE020)),
    ATLAS(3.2, EnumSpellElement.CONNECTION, EnumSpellElement.SPIRIT, new Color(0x2020FF)),
    QUAERITUS(6.4, EnumSpellElement.AETHER, EnumSpellElement.SOUL, new Color(0xFFFFFF));

    public final double awakenedFillPercentage;
    public final String oreName = "essence" + Arrays
            .stream(name().toLowerCase().split("_"))
            .map(EnumAnimusTier::capitalize)
            .collect(Collectors.joining());
    private final EnumSpellElement elementPrimary;
    private final EnumSpellElement elementSecondary;
    public final Color tierColor;

    EnumAnimusTier(double awakenedFillPercentage, EnumSpellElement elementPrimary, EnumSpellElement elementSecondary, Color tierColor) {
        this.awakenedFillPercentage = awakenedFillPercentage;
        this.elementPrimary = elementPrimary;
        this.elementSecondary = elementSecondary;
        this.tierColor = tierColor;
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

    @Override
    @NotNull
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public EnumSpellElement getElementPrimary() {
        return elementPrimary;
    }

    public EnumSpellElement getElementSecondary() {
        return elementSecondary;
    }
}
