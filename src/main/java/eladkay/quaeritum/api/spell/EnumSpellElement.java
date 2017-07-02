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
    EARTH(new Color(0x794715)),
    WATER(new Color(0x1A6BEF)),
    FIRE(new Color(0xF98B16)),
    AIR(new Color(0xEFED7F)),
    METAL(new Color(0x8E8E8E)),
    ENTROPY(new Color(0x7D0D0F)),
    FORM(new Color(0x3DD748)),
    CONNECTION(new Color(0x19C3E5)),
    SPIRIT(new Color(0xB4AE28)),
    AETHER(null),
    SOUL(new Color(0xCF42D3));

    @Nullable
    private final Color color; // null is treated specially, as rainbow.

    EnumSpellElement(@Nullable Color color) {
        this.color = color;
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
