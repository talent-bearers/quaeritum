package eladkay.quaeritum.api.spell;

import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
import eladkay.quaeritum.api.spell.render.*;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.awt.*;

import static eladkay.quaeritum.api.spell.render.LineSymbolInstruction.CIRCLE_EDGE;
import static eladkay.quaeritum.api.spell.render.LineSymbolInstruction.R2O2;

/**
 * @author WireSegal
 *         Created at 11:20 PM on 7/1/17.
 */
public enum EnumLegend implements ISymbolCarrier {
    // The Riftmaker (Wire / Ea, Archmage of The Rift (Æther))
    RIFTMAKER(new Color(0xD592DB), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new HalfCircleSymbolInstruction(R2O2, R2O2, R2O2 * 5 / 4, Math.PI * 7 / 4),
            new HalfCircleSymbolInstruction(1 - R2O2, R2O2, R2O2 * 5 / 4, Math.PI * 1 / 4),
            new HalfCircleSymbolInstruction(R2O2, 1 - R2O2, R2O2 * 5 / 4, Math.PI * 5 / 4),
            new HalfCircleSymbolInstruction(1 - R2O2, 1 - R2O2, R2O2 * 5 / 4, Math.PI * 3 / 4)),
    // The Lorist (Wire)
    LORIST(new Color(0xD592DB), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f)),
    // Kalimatai (Luna)
    BINDER(new Color(0xDB8B29), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new NGonSymbolInstruction(0.5f, 0.5f, 4, 0.5f),
            new NGonSymbolInstruction(0.5f, 0.5f, 4, 0.5f, Math.PI / 4)),
    // Ancient of Discovery (Elad)
    DISCOVERY(new Color(0x1BA50A), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f)), // todo
    // Ancient of Truth (Escapee)
    TRUTH(new Color(0xC515FF), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new CircleSymbolInstruction(0.5f, 0.5f, 0.2f),
            new LineSymbolInstruction(R2O2, R2O2, 0.5f, 0.3f),
            new LineSymbolInstruction(1 - R2O2, R2O2, 0.7f, 0.5f),
            new LineSymbolInstruction(R2O2, 1 - R2O2, 0.3f, 0.5f),
            new LineSymbolInstruction(1 - R2O2, 1 - R2O2, 0.5f, 0.7f)),
    // Ancient of Doorways (Saad)
    DOORWAYS(new Color(0xB12E29), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f)), // todo
    // Ancient of Growth (Vazkii, Archmage of Growth (Fire))
    GROWTH(new Color(0xEFFF65), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new LineSymbolInstruction(0.5f, 1f, 0.5f, 0.75f),
            new LineSymbolInstruction(0.5f, 0.75f, 0.25f, 0.5f),
            new LineSymbolInstruction(0.5f, 0.75f, 0.75f, 0.5f)),
    // Ancient of Mysteries (Azanor, Archmage of The Unknowable (Water))
    MYSTERIES(new Color(0xB10078), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new HalfCircleSymbolInstruction(0.5f, 1 - R2O2, 0.5f - R2O2, Math.PI),
            new HalfCircleSymbolInstruction(0.5f, R2O2, 0.5f - R2O2, 0)),
    // Ancient of Twisted Ways (Elucent, Archmage of Dark Paths (Earth))
    TWISTED(new Color(0x4EFF93), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new LineSymbolInstruction(0.5f, 0.5f, 0.5f, 0.25f),
            new LineSymbolInstruction(0.5f, 0.25f, 0.25f, 0.5f),
            new LineSymbolInstruction(0.25f, 0.5f, 0.5f, 0.75f),
            new LineSymbolInstruction(0.5f, 0.75f, 0.85f, 0.4f),
            new LineSymbolInstruction(0.85f, 0.4f, 0.5f, 0f)),
    // Ancient of Ingenuity (King Lemming, Archmage of Insight (Air))
    INGENUITY(new Color(0x0A85B1), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new CircleSymbolInstruction(0.5f, 0.5f, 0.25f),
            new CircleSymbolInstruction(0.5f, 0.5f, 0.125f)),
    // Ancient of Shattered Flows (Thecodewarrior)
    SHATTERED(new Color(0x33FF41), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new LineSymbolInstruction(0.25f, 1 - CIRCLE_EDGE, 0.5f, 0.25f),
            new LineSymbolInstruction(0.5f, 0.25f, CIRCLE_EDGE, 0.25f),
            new LineSymbolInstruction(1 - CIRCLE_EDGE, 0.75f, 0.5f, 0.75f),
            new LineSymbolInstruction(0.5f, 0.75f, 0.75f, CIRCLE_EDGE)),
    // Ancient of the Archive (CobrasBane)
    ARCHIVE(new Color(0), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f)),
    // Ancient of Elsewhere (Tristaric)
    ELSEWHERE(new Color(0xFF004C), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new NGonSymbolInstruction(0.5f, 0.5f, 4, 0.5f),
            new LineSymbolInstruction(0.5f, 0f, 0.5f, 1f),
            new LineSymbolInstruction(0f, 0.5f, 1f, 0.5f));

    @Nullable
    private final Color color; // null is treated specially, as rainbow.

    public final ISymbolInstruction[] symbolInstructions;

    EnumLegend(@Nullable Color color, ISymbolInstruction... symbolInstructions) {
        this.color = color;
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
