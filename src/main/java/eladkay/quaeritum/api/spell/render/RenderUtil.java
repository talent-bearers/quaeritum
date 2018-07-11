package eladkay.quaeritum.api.spell.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.MathHelper;

/**
 * @author WireSegal
 * Created at 8:32 PM on 7/26/17.
 */
public final class RenderUtil {

    public static final int SEGMENTS_CIRCLE = 36;
    public static float alphaMultiplier = 1f;

    private static void renderCap(BufferBuilder buffer, double cX, double cY, float r, float g, float b, double thickness, double normalAngle, int sides) {
        for (int i = 0; i < sides; i++) {
            double angle = i * Math.PI / sides + normalAngle;
            float normX = cos(angle);
            float normY = sin(angle);
            double outerX = cX + normX * thickness;
            double outerY = cY + normY * thickness;
            buffer.pos(outerX, outerY, 0).color(0, 0, 0, 0).endVertex();
            buffer.pos(cX, cY, 0).color(r, g, b, 0.75f * alphaMultiplier).endVertex();
        }
    }

    private static void renderCurlingCap(BufferBuilder buffer, double cX, double cY, float r, float g, float b, double thickness, double normalAngle, int sides, boolean ccw) {
        int direction = ccw ? -1 : 1;

        for (int i = 0; i < sides; i++) {
            double angle = i * Math.PI / sides + normalAngle;
            float normX = cos(angle);
            float normXOffset = cos(angle - normalAngle);
            float normY = sin(angle);
            double length = thickness / (2 + direction * normXOffset);
            double outerX = cX + normX * length;
            double outerY = cY + normY * length;
            buffer.pos(outerX, outerY, 0).color(0, 0, 0, 0).endVertex();
            buffer.pos(cX, cY, 0).color(r, g, b, 0.75f * alphaMultiplier).endVertex();
        }
    }

    private static void renderArcSegment(BufferBuilder buffer, double cX, double cY, float r, float g, float b, double radius, double outerRadius, double angleOffset, int sides, double radians, boolean repeatLast) {
        int segments = repeatLast ? sides + 1 : sides;
        for (int i = 0; i < segments; i++)
            renderArcPoint(buffer, cX, cY, r, g, b, radius, outerRadius, angleOffset, sides, radians, i);
    }

    private static void renderArcPoint(BufferBuilder buffer, double cX, double cY, float r, float g, float b, double radius, double outerRadius, double angleOffset, int sides, double radians, int pointIndex) {
        double angle = pointIndex * radians / sides + angleOffset;
        float normX = cos(angle);
        float normY = sin(angle);
        double innerX = cX + normX * radius;
        double innerY = cY + normY * radius;
        double outerX = cX + normX * outerRadius;
        double outerY = cY + normY * outerRadius;
        buffer.pos(outerX, outerY, 0).color(0, 0, 0, 0).endVertex();
        buffer.pos(innerX, innerY, 0).color(r, g, b, 0.75f * alphaMultiplier).endVertex();
    }

    private static void renderInnerArcSegment(BufferBuilder buffer, double cX, double cY, float r, float g, float b, double radius, double outerRadius, double angleOffset, int sides, double radians, boolean repeatLast) {
        int segments = repeatLast ? sides + 1 : sides;
        for (int i = segments; i >= 0; i--)
            renderArcPoint(buffer, cX, cY, r, g, b, radius, outerRadius, angleOffset, sides, radians, i);
    }

    private static void renderConnectingSegment(BufferBuilder buffer, double cX, double cY, float r, float g, float b, double radius, double outerRadius, double angleOffset) {
        float normX = cos(angleOffset);
        float normY = sin(angleOffset);
        double innerX = cX + normX * radius;
        double innerY = cY + normY * radius;
        double outerX = cX + normX * outerRadius;
        double outerY = cY + normY * outerRadius;

        buffer.pos(outerX, outerY, 0).color(0, 0, 0, 0).endVertex();
        buffer.pos(innerX, innerY, 0).color(r, g, b, 0.75f * alphaMultiplier).endVertex();
    }

    public static void renderHalfCircle(BufferBuilder buffer, double cX, double cY, float r, float g, float b, double radius, double thickness, double angleOffset) {
        renderArc(buffer, cX, cY, r, g, b, radius, thickness, angleOffset, Math.PI);
    }

    public static void renderArc(BufferBuilder buffer, double cX, double cY, float r, float g, float b, double radius, double thickness, double angleOffset, double radians) {
        if (radians >= 2 * Math.PI) {
            renderNGon(buffer, cX, cY, r, g, b, radius, thickness, SEGMENTS_CIRCLE, angleOffset);
            return;
        }

        double outerRadius = radius + thickness;
        double centralRadius = radius - thickness / 2;

        double xShift = cos(angleOffset) * radius;
        double yShift = sin(angleOffset) * radius;

        renderArcSegment(buffer, cX, cY, r, g, b, radius, outerRadius, angleOffset, SEGMENTS_CIRCLE, radians, false);
        renderCurlingCap(buffer, cX - xShift, cY - yShift, r, g, b, thickness, angleOffset + radians, SEGMENTS_CIRCLE, true);
        renderInnerArcSegment(buffer, cX, cY, r, g, b, radius, centralRadius, angleOffset, SEGMENTS_CIRCLE, radians, true);
        renderCurlingCap(buffer, cX + xShift, cY + yShift, r, g, b, thickness, angleOffset - radians, SEGMENTS_CIRCLE, false);
    }

    public static void renderLine(BufferBuilder buffer, double x1, double y1, double x2, double y2, float r, float g, float b, double thickness) {
        float atan = fastAtan2(y2 - y1, x2 - x1);
        renderCap(buffer, x1, y1, r, g, b, thickness, atan + Math.PI / 2, SEGMENTS_CIRCLE);
        renderCap(buffer, x2, y2, r, g, b, thickness, atan - Math.PI / 2, SEGMENTS_CIRCLE);
        renderConnectingSegment(buffer, x1, y1, r, g, b, 0, thickness, atan + Math.PI / 2);
    }

    public static void renderNGon(BufferBuilder buffer, double cX, double cY, float r, float g, float b, double radius, double thickness, int sides) {
        renderNGon(buffer, cX, cY, r, g, b, radius, thickness, sides, 0);
    }

    public static void renderNGon(BufferBuilder buffer, double cX, double cY, float r, float g, float b, double radius, double thickness, int sides, double angleOffset) {
        double outerRadius = radius + thickness;
        double centralRadius = radius - thickness / 2;

        renderArcSegment(buffer, cX, cY, r, g, b, radius, outerRadius, angleOffset, sides, 2 * Math.PI, true);
        renderInnerArcSegment(buffer, cX, cY, r, g, b, radius, centralRadius, angleOffset, sides, 2 * Math.PI, false);
        renderConnectingSegment(buffer, cX, cY, r, g, b, radius, outerRadius, angleOffset);
    }

    public static float sin(double a) {
        return MathHelper.sin((float) a);
    }

    public static float cos(double a) {
        return MathHelper.cos((float) a);
    }

    public static float fastAtan2(double y, double x) {
        return (float) MathHelper.atan2(y, x);
    }
}
