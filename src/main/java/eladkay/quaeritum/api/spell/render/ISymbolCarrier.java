package eladkay.quaeritum.api.spell.render;

import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

/**
 * @author WireSegal
 * Created at 8:17 PM on 7/26/17.
 */
public interface ISymbolCarrier {

    int color();

    int rawColor();

    static int trueColor(Color color) {
        if (color == null)
            return Color.HSBtoRGB((ClientTickHandler.getTicksInGame() * 2L % 360L) / 360.0F, 0.8F, 1.0F);
        return color.getRGB();
    }

    @SideOnly(Side.CLIENT)
    static int timeShift(Color color) {
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

    ISymbolInstruction[] getSymbolInstructions();
}
