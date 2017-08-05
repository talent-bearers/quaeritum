package eladkay.quaeritum.api.spell;

import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
import eladkay.quaeritum.api.spell.render.*;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.awt.*;

import static eladkay.quaeritum.api.spell.render.HalfCircleSymbolInstruction.TRI_OFFSET;
import static eladkay.quaeritum.api.spell.render.HalfCircleSymbolInstruction.TRI_OFFSET_PARAM;
import static eladkay.quaeritum.api.spell.render.LineSymbolInstruction.R2O2;

/**
 * @author WireSegal
 *         Created at 11:20 PM on 7/1/17.
 */
public enum EnumSpellElement implements ISymbolCarrier {
    EARTH(new Color(0x794715), 'V', new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new NGonSymbolInstruction(0.5f, 0.5f, 3, 0.4f, Math.PI / 2),
            new LineSymbolInstruction(R2O2, 1 - R2O2, 1 - R2O2, 1 - R2O2)),
    WATER(new Color(0x1A6BEF), '~', new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new NGonSymbolInstruction(0.5f, 0.5f, 3, 0.4f, Math.PI / 2)),
    FIRE(new Color(0xF98B16), '#', new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new NGonSymbolInstruction(0.5f, 0.5f, 3, 0.4f, -Math.PI / 2)),
    AIR(new Color(0xEFED7F), '^', new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new NGonSymbolInstruction(0.5f, 0.5f, 3, 0.4f, -Math.PI / 2),
            new LineSymbolInstruction(R2O2, R2O2, 1 - R2O2, R2O2)),
    METAL(new Color(0x8E8E8E), 'I', new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new LineSymbolInstruction(R2O2, R2O2, 1 - R2O2, R2O2),
            new LineSymbolInstruction(0.5f, 0.5f, 0.5f, 1f),
            new LineSymbolInstruction(R2O2, R2O2, 0.5f, 0.5f),
            new LineSymbolInstruction(1 - R2O2, R2O2, 0.5f, 0.5f)),
    ENTROPY(new Color(0x7D0D0F), '*', new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new LineSymbolInstruction(R2O2, R2O2, 1 - R2O2, 1 - R2O2),
            new LineSymbolInstruction(1 - R2O2, R2O2, R2O2, 1 - R2O2),
            new LineSymbolInstruction(0.5f, 0f, 0.5f, 1f),
            new LineSymbolInstruction(0f, 0.5f, 1f, 0.5f)),
    FORM(new Color(0x3DD748), 'O', new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new LineSymbolInstruction(0f, 0.5f, 1f, 0.5f)),
    FLOW(new Color(0x33F0E8), '>', new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new LineSymbolInstruction(R2O2, R2O2, 1 - R2O2, R2O2),
            new CircleSymbolInstruction(0.5f, R2O2 + (1 - R2O2) / 4, (1 - R2O2) / 4),
            new CircleSymbolInstruction(0.5f, R2O2 + (1 - R2O2) * 3 / 4 - 0.1f, (1 - R2O2) / 4)),
    CONNECTION(new Color(0x07FF82), '|', new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new HalfCircleSymbolInstruction(0.5f, 1 - R2O2, 0.5f - R2O2, Math.PI),
            new LineSymbolInstruction(0.5f, 0f, 0.5f, 0.5f),
            new LineSymbolInstruction(0.25f, 1 / 3f, 0.75f, 1 / 3f),
            new LineSymbolInstruction(0.25f, 1 / 6f, 0.75f, 1 / 6f)),
    SPIRIT(new Color(0xB4AE28), '@', new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new HalfCircleSymbolInstruction(0.5f, R2O2 / 2, (0.5f - R2O2) / 2, 0),
            new CircleSymbolInstruction(0.5f, 2 * R2O2, R2O2),
            new LineSymbolInstruction(0.25f, 4 * R2O2, 0.75f, 4 * R2O2),
            new LineSymbolInstruction(0.5f, 3 * R2O2, 0.5f, 1f)),
    AETHER(null, ';', new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new NGonSymbolInstruction(0.5f, 0.5f, 3, 0.4f, Math.PI / 2),
            new NGonSymbolInstruction(0.5f, 0.5f, 3, 0.4f, -Math.PI / 2)),
    SOUL(new Color(0xCF42D3), '\'', new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new NGonSymbolInstruction(0.5f, TRI_OFFSET, 3, TRI_OFFSET_PARAM, Math.PI / 2),
            new LineSymbolInstruction(0.5f, TRI_OFFSET + TRI_OFFSET_PARAM, 0.5f, 1f),
            new LineSymbolInstruction(0.25f, 0.5f + (TRI_OFFSET + TRI_OFFSET_PARAM) / 2, 0.75f, 0.5f + (TRI_OFFSET + TRI_OFFSET_PARAM) / 2));

    @Nullable
    private final Color color; // null is treated specially, as rainbow.

    public final char representation;

    public final ISymbolInstruction[] symbolInstructions;

    EnumSpellElement(@Nullable Color color, char representation, ISymbolInstruction... symbolInstructions) {
        this.color = color;
        this.representation = representation;
        this.symbolInstructions = symbolInstructions;
    }

    @Override
    public int color() {
        if (color == null)
            return Color.HSBtoRGB((ClientTickHandler.getTicksInGame() * 2L % 360L) / 360.0F, 0.8F, 1.0F);

        int c = color.getRGB();
        int add = (int)(MathHelper.sin(ClientTickHandler.getTicksInGame() * 0.2f) * 24);
        int r = (c & (0xff0000)) >> 16;
        int g = (c & (0x00ff00)) >> 8;
        int b = (c & (0x0000ff));
        int newR = Math.max(Math.min(r + add, 255), 0);
        int newG = Math.max(Math.min(g + add, 255), 0);
        int newB = Math.max(Math.min(b + add, 255), 0);
        return (newR << 16) | (newG << 8) | newB;
    }

    @Override
    public ISymbolInstruction[] getSymbolInstructions() {
        return symbolInstructions;
    }


}
