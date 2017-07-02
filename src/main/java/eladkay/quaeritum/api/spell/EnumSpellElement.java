package eladkay.quaeritum.api.spell;

import net.minecraft.world.World;

import java.awt.*;

/**
 * @author WireSegal
 *         Created at 11:20 PM on 7/1/17.
 */
public enum EnumSpellElement {
    EARTH(0x603D0E),
    WATER(0x1325EF),
    FIRE(0xF98B16),
    AIR(0xEFED7F),
    METAL(0x8E8E8E),
    ENTROPY(0x631010),
    FORM(0xA09F85),
    CONNECTION(0x19C3E5),
    SPIRIT(0x91820E),
    AETHER(Integer.MAX_VALUE),
    SOUL(0xA93AAD);

    private final int color; // Integer.MAX_VALUE is treated specially, as rainbow.

    EnumSpellElement(int color) {
        this.color = color;
    }


    public int color(World world) {
        if (color != Integer.MAX_VALUE) return color;
        return Color.HSBtoRGB((world.getTotalWorldTime() * 2L % 360L) / 360.0F, 0.8F, 1.0F);
    }

}
