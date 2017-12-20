package eladkay.quaeritum.api.spell.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * @author WireSegal
 * Created at 8:47 PM on 7/26/17.
 */
public class NGonSymbolInstruction implements ISymbolInstruction {
    public final float cX, cY, radius, angleOffset;
    public final int sides;

    public NGonSymbolInstruction(float cX, float cY, int sides, float size) {
        this(cX, cY, sides, size, 0f);
    }

    public NGonSymbolInstruction(float cX, float cY, int sides, float size, double angleOffset) {
        this(cX, cY, sides, size, (float) angleOffset);
    }

    public NGonSymbolInstruction(float cX, float cY, int sides, float size, float angleOffset) {
        this.cX = cX * 15;
        this.cY = cY * 15;
        this.sides = sides;
        this.radius = size;
        this.angleOffset = angleOffset;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(ISymbolCarrier element, float x, float y) {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();
        int color = element.color();
        int r = (color & (0xff0000)) >> 16;
        int g = (color & (0x00ff00)) >> 8;
        int b = (color & (0x0000ff));
        buffer.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_COLOR);
        RenderUtil.renderNGon(buffer, cX + x - 0.5, cY + y - 0.5, r / 255f, g / 255f, b / 255f, radius * 15, 5, sides, angleOffset);
        tess.draw();
    }
}
