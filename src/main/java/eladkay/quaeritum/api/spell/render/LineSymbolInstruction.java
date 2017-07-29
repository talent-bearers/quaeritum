package eladkay.quaeritum.api.spell.render;

import eladkay.quaeritum.api.spell.EnumSpellElement;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * @author WireSegal
 *         Created at 8:47 PM on 7/26/17.
 */
public class LineSymbolInstruction implements ISymbolInstruction {
    public final float x1, y1, x2, y2;

    public static final float R2O2 = 1 / (float) Math.sqrt(2) - 0.5f;

    public LineSymbolInstruction(float x1, float y1, float x2, float y2) {
        this.x1 = x1 * 15;
        this.y1 = y1 * 15;
        this.x2 = x2 * 15;
        this.y2 = y2 * 15;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(EnumSpellElement element, float x, float y) {
        Tessellator tess = Tessellator.getInstance();
        VertexBuffer buffer = tess.getBuffer();
        int color = element.color();
        int r = (color & (0xff0000)) >> 16;
        int g = (color & (0x00ff00)) >> 8;
        int b = (color & (0x0000ff));
        buffer.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_COLOR);
        RenderUtil.renderLine(buffer, x1 + x - 0.5, y1 + y - 0.5, x2 + x - 0.5, y2 + y - 0.5, r / 255f, g / 255f, b / 255f, 2f);
        tess.draw();
    }
}
