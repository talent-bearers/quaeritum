package eladkay.quaeritum.api.spell;

import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
import eladkay.quaeritum.api.spell.render.CircleSymbolInstruction;
import eladkay.quaeritum.api.spell.render.ISymbolCarrier;
import eladkay.quaeritum.api.spell.render.ISymbolInstruction;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.awt.*;

/**
 * @author WireSegal
 *         Created at 11:20 PM on 7/1/17.
 */
public enum EnumLegend implements ISymbolCarrier {
    // The Riftmaker (Wire / Ea, Archmage of The Rift (Æther))
    RIFTMAKER(new Color(0xD592DB), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f)), // todo
    // The Lorist (Wire)
    LORIST(new Color(0xD592DB), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f)),
    // Ancient of Discovery (Elad)
    DISCOVERY(new Color(0x1BA50A), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f)), // todo
    // Ancient of Truth (Escapee)
    TRUTH(new Color(0xC515FF), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f)), // todo
    // Ancient of Doorways (Saad)
    DOORWAYS(new Color(0xB12E29), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f)), // todo
    // Ancient of Growth (Vazkii, Archmage of Growth (Fire))
    GROWTH(new Color(0x8FB115), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f)), // todo
    // Ancient of Mysteries (Azanor, Archmage of The Unknowable (Water))
    MYSTERIES(new Color(0xB10078), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f)), // todo
    // Ancient of Twisted Ways (Elucent, Archmage of Dark Paths (Earth))
    TWISTED(new Color(0x4EFF93), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f)), // todo
    // Ancient of Ingenuity (King Lemming, Archmage of Insight (Air))
    INGENUITY(new Color(0x0A85B1), new CircleSymbolInstruction(0.5f, 0.5f, 0.5f)); // todo

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
