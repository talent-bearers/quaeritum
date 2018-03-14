package eladkay.quaeritum.api.spell;

import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
import eladkay.quaeritum.api.spell.render.*;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.awt.*;

import static eladkay.quaeritum.api.spell.render.CircleSymbolInstruction.RADIUS_MOD;
import static eladkay.quaeritum.api.spell.render.LineSymbolInstruction.*;

/**
 * @author WireSegal
 * Created at 11:20 PM on 7/1/17.
 */
@SuppressWarnings("SpellCheckingInspection")
public enum EnumLegend implements ISymbolCarrier {
    // The Riftmaker (Wire / Ea, Ancient of Secrets Kept, Archmage of the Rift (Æther))
    RIFTMAKER(new Color(0xD592DB), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new HalfCircleSymbolInstruction(R2O2, R2O2, R2O2 * 5 / 4, Math.PI * 7 / 4),
            new HalfCircleSymbolInstruction(1 - R2O2, R2O2, R2O2 * 5 / 4, Math.PI * 1 / 4),
            new HalfCircleSymbolInstruction(R2O2, 1 - R2O2, R2O2 * 5 / 4, Math.PI * 5 / 4),
            new HalfCircleSymbolInstruction(1 - R2O2, 1 - R2O2, R2O2 * 5 / 4, Math.PI * 3 / 4)),
    // The Lorist (Wire)
    LORIST(new Color(0xD592DB), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f)),
    // Kalimatai, She Who Binds (Luna)
    BINDER(new Color(0xDB8B29), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new NGonSymbolInstruction(0.5f, 0.5f, 4, 0.5f),
            new NGonSymbolInstruction(0.5f, 0.5f, 4, 0.5f, Math.PI / 4)),
    // Ancient of Discovery (Elad (before the Blink, Escapee))
    DISCOVERY(new Color(0x1BA50A), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new NGonSymbolInstruction(0.5f, 0.5f, 8, 0.5f),
            new NGonSymbolInstruction(0.5f, 0.5f, 4, 0.5f, Math.PI / 4),
            new CircleSymbolInstruction(0.5f, 0.5f, 0.25f)),
    // Ancient of Truth (Escapee, Archmage of Truth (Soul))
    TRUTH(new Color(0xC515FF), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new CircleSymbolInstruction(0.5f, 0.5f, 0.2f),
            new LineSymbolInstruction(R2O2, R2O2, 0.5f, 0.3f),
            new LineSymbolInstruction(1 - R2O2, R2O2, 0.7f, 0.5f),
            new LineSymbolInstruction(R2O2, 1 - R2O2, 0.3f, 0.5f),
            new LineSymbolInstruction(1 - R2O2, 1 - R2O2, 0.5f, 0.7f)),
    // Ancient of Doorways (Saad (before the Blink, Mithion))
    DOORWAYS(new Color(0xB12E29), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new CircleSymbolInstruction(0.5f, 0.75f, 0.25f),
            new LineSymbolInstruction(0.5f - RADIUS_MOD, 0.75f - RADIUS_MOD, 1 - CIRCLE_EDGE, 0.25f),
            new LineSymbolInstruction(0.5f + RADIUS_MOD, 0.75f - RADIUS_MOD, CIRCLE_EDGE, 0.25f),
            new LineSymbolInstruction(0.5f - RADIUS_MOD, 0.75f + RADIUS_MOD, 1 - CIRCLE_EDGE, 0.25f),
            new LineSymbolInstruction(0.5f + RADIUS_MOD, 0.75f + RADIUS_MOD, CIRCLE_EDGE, 0.25f)),
    // Ancient of Growth (Vazkii, Archmage of Growth (Earth))
    GROWTH(new Color(0xEFFF65), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new LineSymbolInstruction(0.5f, 1f, 0.5f, 0.75f),
            new LineSymbolInstruction(0.5f, 0.75f, 0.25f, 0.5f),
            new LineSymbolInstruction(0.5f, 0.75f, 0.75f, 0.5f)),
    // Ancient of Mysteries (Azanor, Archmage of Answers (Air))
    MYSTERIES(new Color(0xB10078), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new HalfCircleSymbolInstruction(0.5f, 1 - R2O2, 0.5f - R2O2, Math.PI),
            new HalfCircleSymbolInstruction(0.5f, R2O2, 0.5f - R2O2, 0)),
    // Ancient of Twisted Ways (Elucent (before the Blink, Emoniph), Archmage of Twisted Paths (Water))
    TWISTED(new Color(0x4EFF93), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new LineSymbolInstruction(0.5f, 0.5f, 0.5f, 0.25f),
            new LineSymbolInstruction(0.5f, 0.25f, 0.25f, 0.5f),
            new LineSymbolInstruction(0.25f, 0.5f, 0.5f, 0.75f),
            new LineSymbolInstruction(0.5f, 0.75f, 0.85f, 0.4f),
            new LineSymbolInstruction(0.85f, 0.4f, 0.5f, 0f)),
    // Ancient of Ingenuity (King Lemming (before the Blink, AlexIIL), Archmage of Ingenuity (Fire))
    INGENUITY(new Color(0x0A85B1), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new CircleSymbolInstruction(0.5f, 0.5f, 0.25f),
            new CircleSymbolInstruction(0.5f, 0.5f, 0.125f)),
    // Ancient of Shattered Flows (Thecodewarrior)
    SHATTERED(new Color(0x33FF41), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new LineSymbolInstruction(0.25f, 1 - CIRCLE_EDGE, 0.5f, 0.375f),
            new LineSymbolInstruction(0.5f, 0.375f, CIRCLE_EDGE_2, 0.375f),
            new LineSymbolInstruction(1 - CIRCLE_EDGE_2, 0.625f, 0.5f, 0.625f),
            new LineSymbolInstruction(0.5f, 0.625f, 0.75f, CIRCLE_EDGE)),
    // Ancient of the Archive (CobrasBane)
    ARCHIVE(new Color(0xB1163B), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new CircleSymbolInstruction(0.35f, 0.35f, 0.25f),
            new CircleSymbolInstruction(0.75f, 0.25f, 0.1f),
            new CircleSymbolInstruction(0.8f, 0.55f, 0.1f),
            new CircleSymbolInstruction(0.25f, 0.75f, 0.1f),
            new CircleSymbolInstruction(0.55f, 0.8f, 0.1f)),
    // Ancient of Elsewhere (Tristaric)
    ELSEWHERE(new Color(0x177FE8), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new NGonSymbolInstruction(0.5f, 0.5f, 4, 0.5f),
            new LineSymbolInstruction(0.5f, 0f, 0.5f, 1f),
            new LineSymbolInstruction(0f, 0.5f, 1f, 0.5f)),
    // Ancient of Disdain (wayoftime)
    DISDAIN(new Color(0xFF3F0C), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new CircleSymbolInstruction(0.5f, 0.75f, 0.25f),
            new LineSymbolInstruction(0.5f, 0f, 0.5f, 0.5f)),
    // Ancient of Declaration (machinespray)
    DECLARATION(new Color(0xFF00FF), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new LineSymbolInstruction(0.25f, 1 - CIRCLE_EDGE, 0f, 0.5f),
            new LineSymbolInstruction(0.25f, 1 - CIRCLE_EDGE, 0.5f, 0.5f),
            new LineSymbolInstruction(0.75f, 1 - CIRCLE_EDGE, 1f, 0.5f),
            new LineSymbolInstruction(0.75f, 1 - CIRCLE_EDGE, 0.5f, 0.5f),
            new LineSymbolInstruction(1f, 0.5f, 0.5f, 1f),
            new LineSymbolInstruction(0f, 0.5f, 0.5f, 1f),
            new LineSymbolInstruction(0.5f, 0.5f, 0.5f, 1f)),
    // Ancient of Wonder (HellfirePVP)
    WONDER(new Color(0x27E1EC), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new LineSymbolInstruction(R2O2, 1 - R2O2, 0.5f, 0f),
            new LineSymbolInstruction(1 - R2O2, 1 - R2O2, 0.5f, 0f)),
    // Ancients of the Deep (Kino, Jon, and Bailey, the Glacidius Team)
    DEEP(new Color(0x144694), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new LineSymbolInstruction(0.25f, 0.25f, 0.75f, 0.25f),
            new LineSymbolInstruction(R2O2, 1 - R2O2, 0.6f, 1 - R2O2),
            new LineSymbolInstruction(R2O2, 1 - R2O2 - 0.1f, 0.25f, 0.35f),
            new LineSymbolInstruction(0.7f, 1 - R2O2, 0.4f, 1 - R2O2 - 0.3f)),
    // Ancient of Sloth (InsomniaKitten)
    SLOTH(new Color(0xB92CFF), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f),
            new LineSymbolInstruction(R2O2, R2O2, 1 - R2O2, 1 - R2O2),
            new LineSymbolInstruction(R2O2, 1 - R2O2, 1 - R2O2, R2O2),
            new CircleSymbolInstruction(0.5f, 1 - R2O2, R2O2));

    public final ISymbolInstruction[] symbolInstructions;
    @Nullable
    private final Color color; // null is treated specially, as rainbow.

    EnumLegend(@Nullable Color color, ISymbolInstruction... symbolInstructions) {
        this.color = color;
        this.symbolInstructions = symbolInstructions;
    }

    @Override
    public int color() {
        if (color == null)
            return Color.HSBtoRGB((ClientTickHandler.getTicksInGame() * 2L % 360L) / 360.0F, 0.8F, 1.0F);

        int c = color.getRGB();
        int add = (int) (MathHelper.sin(ClientTickHandler.getTicksInGame() * 0.2f) * 24);
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
