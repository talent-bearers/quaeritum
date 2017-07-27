package eladkay.quaeritum.api.spell.render;

import eladkay.quaeritum.api.spell.EnumSpellElement;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import static eladkay.quaeritum.api.spell.render.LineSymbolInstruction.R2O2;

/**
 * @author WireSegal
 *         Created at 8:47 PM on 7/26/17.
 */
public class HalfCircleSymbolInstruction implements ISymbolInstruction {
    public final float cX, cY, radius, angleOffset;

    public static final float TRI_OFFSET_PARAM = (0.5f - R2O2) / MathHelper.cos((float) Math.PI / 6);
    public static final float TRI_OFFSET = MathHelper.sin((float) Math.PI / 6) * TRI_OFFSET_PARAM + R2O2;

    public HalfCircleSymbolInstruction(float cX, float cY, float radius, double angleOffset) {
        this(cX, cY, radius, (float) angleOffset);
    }

    public HalfCircleSymbolInstruction(float cX, float cY, float radius, float angleOffset) {
        this.cX = cX * 15;
        this.cY = cY * 15;
        this.radius = radius;
        this.angleOffset = angleOffset;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(EnumSpellElement element, int x, int y) {
        Tessellator tess = Tessellator.getInstance();
        VertexBuffer buffer = tess.getBuffer();
        int color = element.color();
        int r = (color & (0xff0000)) >> 16;
        int g = (color & (0x00ff00)) >> 8;
        int b = (color & (0x0000ff));
        buffer.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_COLOR);
        RenderUtil.renderHalfCircle(buffer, cX + x - 0.5, cY + y - 0.5, r / 255f, g / 255f, b / 255f, radius * 15, 5, angleOffset);
        tess.draw();
    }
}
