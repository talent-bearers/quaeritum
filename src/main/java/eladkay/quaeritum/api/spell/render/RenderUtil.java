package eladkay.quaeritum.api.spell.render;

import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.util.math.MathHelper;

/**
 * @author WireSegal
 *         Created at 8:32 PM on 7/26/17.
 */
public final class RenderUtil {

    private static final int SEGMENTS_CIRCLE = 36;

    public static void renderCircle(VertexBuffer buffer, double cX, double cY, float r, float g, float b, double radius, double thickness) {
        renderNGon(buffer, cX, cY, r, g, b, radius, thickness, SEGMENTS_CIRCLE);
    }

    public static void renderHalfCircle(VertexBuffer buffer, double cX, double cY, float r, float g, float b, double radius, double thickness, double angleOffset) {
        double outerRadius = radius + thickness;
        double centralRadius = radius - thickness / 2;

        for (int i = 0; i <= SEGMENTS_CIRCLE / 2; i++) {
            double angle = i * Math.PI / SEGMENTS_CIRCLE + angleOffset;
            float normX = MathHelper.cos((float)angle);
            float normY = MathHelper.sin((float)angle);
            double innerX = cX + normX * radius;
            double innerY = cY + normY * radius;
            double outerX = cX + normX * outerRadius;
            double outerY = cY + normY * outerRadius;
            buffer.pos(outerX, outerY, 0).color(0, 0, 0, 0).endVertex();
            buffer.pos(innerX, innerY, 0).color(r, g, b, 0.8f).endVertex();
        }

        for (int i = 0; i <= SEGMENTS_CIRCLE / 2; i++) {
            double angle = i * 2 * Math.PI / SEGMENTS_CIRCLE + angleOffset;
            float normX = MathHelper.cos((float)angle);
            float normY = MathHelper.sin((float)angle);
            double innerX = cX + normX * radius;
            double innerY = cY + normY * radius;
            double centralX = cX + normX * centralRadius;
            double centralY = cY + normY * centralRadius;
            buffer.pos(innerX, innerY, 0).color(r, g, b, 0.8f).endVertex();
            buffer.pos(centralX, centralY, 0).color(0, 0, 0, 0).endVertex();
        }
    }

    public static void renderLine(VertexBuffer buffer, double x1, double y1, double x2, double y2, float r, float g, float b, double thickness) {
        float atan = fastAtan2((float) (y2 - y1), (float) (x2 - x1));
        for (int i = 0; i < SEGMENTS_CIRCLE; i++) {
            double angle = i * Math.PI / SEGMENTS_CIRCLE + atan + Math.PI / 2;
            float normX = MathHelper.cos((float)angle);
            float normY = MathHelper.sin((float)angle);
            double outerX = x1 + normX * thickness;
            double outerY = y1 + normY * thickness;
            buffer.pos(outerX, outerY, 0).color(0, 0, 0, 0).endVertex();
            buffer.pos(x1, y1, 0).color(r, g, b, 0.8f).endVertex();
        }
        for (int i = 0; i < SEGMENTS_CIRCLE; i++) {
            double angle = i * Math.PI / SEGMENTS_CIRCLE + atan - Math.PI / 2;
            float normX = MathHelper.cos((float)angle);
            float normY = MathHelper.sin((float)angle);
            double outerX = x2 + normX * thickness;
            double outerY = y2 + normY * thickness;
            buffer.pos(outerX, outerY, 0).color(0, 0, 0, 0).endVertex();
            buffer.pos(x2, y2, 0).color(r, g, b, 0.8f).endVertex();
        }
        double angle = atan + Math.PI / 2;
        float normX = MathHelper.cos((float)angle);
        float normY = MathHelper.sin((float)angle);
        double outerX = x1 + normX * thickness;
        double outerY = y1 + normY * thickness;
        buffer.pos(outerX, outerY, 0).color(0, 0, 0, 0).endVertex();
        buffer.pos(x1, y1, 0).color(r, g, b, 0.8f).endVertex();
    }

    public static void renderNGon(VertexBuffer buffer, double cX, double cY, float r, float g, float b, double radius, double thickness, double sides) {
        renderNGon(buffer, cX, cY, r, g, b, radius, thickness, sides, 0);
    }

    public static void renderNGon(VertexBuffer buffer, double cX, double cY, float r, float g, float b, double radius, double thickness, double sides, double angleOffset) {
        double outerRadius = radius + thickness;
        double centralRadius = radius - thickness / 2;

        for (int i = 0; i <= sides; i++) {
            double angle = i * 2 * Math.PI / sides + angleOffset;
            float normX = MathHelper.cos((float)angle);
            float normY = MathHelper.sin((float)angle);
            double innerX = cX + normX * radius;
            double innerY = cY + normY * radius;
            double outerX = cX + normX * outerRadius;
            double outerY = cY + normY * outerRadius;
            buffer.pos(outerX, outerY, 0).color(0, 0, 0, 0).endVertex();
            buffer.pos(innerX, innerY, 0).color(r, g, b, 0.8f).endVertex();
        }

        for (int i = 0; i <= sides; i++) {
            double angle = i * 2 * Math.PI / sides + angleOffset;
            float normX = MathHelper.cos((float)angle);
            float normY = MathHelper.sin((float)angle);
            double innerX = cX + normX * radius;
            double innerY = cY + normY * radius;
            double centralX = cX + normX * centralRadius;
            double centralY = cY + normY * centralRadius;
            buffer.pos(innerX, innerY, 0).color(r, g, b, 0.8f).endVertex();
            buffer.pos(centralX, centralY, 0).color(0, 0, 0, 0).endVertex();
        }
    }

    private static float fastAtan2(float y, float x) {
        if (x == 0) return 1.57079637f;
        float ay = MathHelper.abs(y);
        float ax = MathHelper.abs(x);
        float a = Math.min(ax, ay) / Math.max(ax, ay);
        float s = a * a;
        float r = ((-0.0464964749f * s + 0.15931422f) * s - 0.327622764f) * s * a + a;
        if (ay > ax) r = 1.57079637f - r;
        if (x < 0) r = 3.14159274f - r;
        if (y < 0) r = -r;
        return r;
    }
}
