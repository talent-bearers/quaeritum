package eladkay.quaeritum.api.spell;

import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;

/**
 * @author WireSegal
 *         Created at 11:20 PM on 7/1/17.
 */
public enum EnumSpellElement {
    EARTH(new Color(0x794715), 'V'),
    WATER(new Color(0x1A6BEF), '~'),
    FIRE(new Color(0xF98B16), '#'),
    AIR(new Color(0xEFED7F), '^'),
    METAL(new Color(0x8E8E8E), 'I'),
    ENTROPY(new Color(0x7D0D0F), '*'),
    FORM(new Color(0x3DD748), 'O'),
    FLOW(new Color(0x33F0E8), '>'),
    CONNECTION(new Color(0x07FF82), '|'),
    SPIRIT(new Color(0xB4AE28), '@'),
    AETHER(null, ';'),
    SOUL(new Color(0xCF42D3), '\'');

    @Nullable
    private final Color color; // null is treated specially, as rainbow.

    public final char representation;

    EnumSpellElement(@Nullable Color color, char representation) {
        this.color = color;
        this.representation = representation;
    }

    /**
     * Get the color of a spell element, correlated with a world's time.
     * @param world The world object to use. Usually the client world.
     * @return A color value from 0 to 0xffffff.
     */
    public int color(@Nonnull World world) {
        if (color != null) return color.getRGB();
        return Color.HSBtoRGB((world.getTotalWorldTime() * 2L % 360L) / 360.0F, 0.8F, 1.0F);
    }

}
