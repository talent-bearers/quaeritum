package eladkay.quaeritum.client.core;

import eladkay.quaeritum.api.util.RandUtil;
import eladkay.quaeritum.api.util.RandUtilSeed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by LordSaad.
 */
public class RiftRenderer {

	private final Vec3d center;
	private final long seed;

	private net.minecraft.client.renderer.VertexBuffer BUILDER = new net.minecraft.client.renderer.VertexBuffer(0xFFFF);
	private ArrayList<Vec3d> points = new ArrayList<>();
	private ArrayList<Vec3d> points1 = new ArrayList<>();
	private ArrayList<Vec3d> points2 = new ArrayList<>();

	private double DIST = 2.5;
	private double RANDOM_VARIATION = 2; // end-to-end distance randomness
	private double HEIGHT_VARIATION = 2; // Rift Tilt
	private double JAGGED_RESOLUTION = 0.1;
	private double SEGMENT_COUNT = 100;
	private double HEIGHT = 3.5;

	private boolean flip = true;

	public RiftRenderer(Vec3d center, long seed) {
		this.center = center;
		this.seed = seed;
		MinecraftForge.EVENT_BUS.register(this);

		RandUtilSeed rand = new RandUtilSeed(seed);

		Vec3d point1 = new Vec3d(
				rand.nextDouble(DIST - RANDOM_VARIATION, DIST),
				rand.nextDouble(-HEIGHT_VARIATION, HEIGHT_VARIATION),
				rand.nextDouble(DIST - RANDOM_VARIATION, DIST));

		Vec3d point2 = new Vec3d(
				rand.nextDouble(DIST, DIST + RANDOM_VARIATION),
				rand.nextDouble(-HEIGHT_VARIATION, HEIGHT_VARIATION),
				rand.nextDouble(DIST, DIST + RANDOM_VARIATION));

		Vec3d diff = point1.subtract(point2);

		double SEPARATION = diff.lengthVector() / JAGGED_RESOLUTION;
		double sep = SEPARATION / 2;

		points1.add(point1);
		for (int i = 0; i < SEGMENT_COUNT; i++) {
			double x = i / SEGMENT_COUNT;
			double maximumHeight = (1 - Math.abs(0.5 - x) * 2) * HEIGHT;
			points1.add(diff
					.scale(x + rand.nextDouble(-sep, sep))
					.addVector(0, rand.nextDouble(-JAGGED_RESOLUTION, JAGGED_RESOLUTION) + maximumHeight, 0));
		}
		points1.add(point2);

		points2.add(point1);
		for (int i = 0; i < SEGMENT_COUNT; i++) {
			double x = i / SEGMENT_COUNT;
			double maximumHeight = (1 - Math.abs(0.5 - x) * 2) * HEIGHT;
			points2.add(diff
					.scale(x + rand.nextDouble(-sep, sep))
					.addVector(0, rand.nextDouble(-JAGGED_RESOLUTION, JAGGED_RESOLUTION) - maximumHeight, 0));
		}
		points2.add(point2);
	}

	private static VertexBuffer pos(VertexBuffer vb, Vec3d pos) {
		return vb.pos(pos.x, pos.y, pos.z);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void render(RenderWorldLastEvent event) {
		GlStateManager.pushMatrix();
		EntityPlayer player = Minecraft.getMinecraft().player;

		double interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
		double interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
		double interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();

		GlStateManager.translate(-interpPosX, -interpPosY, -interpPosZ);
		//GlStateManager.translate(-interpPosX, -interpPosY, -interpPosZ);

		GlStateManager.depthMask(false);
		GlStateManager.disableCull();
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();

		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vb = tessellator.getBuffer();
		vb.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
		for (int i = 0; i < points1.size(); i += (flip ? 1 : 0)) {
			Vec3d point;
			if (flip) point = points1.get(i);
			else point = points2.get(i);

			flip = !flip;

			Color color = new Color(RandUtil.nextFloat(), RandUtil.nextFloat(), RandUtil.nextFloat());

			pos(vb, point).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
		}
		tessellator.draw();

		//GL11.glEnable(GL11.GL_STENCIL_TEST);
		//GL11.glColorMask(false, false, false, false);
		//GL11.glStencilFunc(GL_ALWAYS, 0, 1);
		//GL11.glStencilOp(GL_KEEP, GL_KEEP, GL_INVERT);
		//GL11.glStencilMask(1);
		////GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE);
//
		//Tessellator tessellator = Tessellator.getInstance();
		//VertexBuffer vb = tessellator.getBuffer();
		//vb.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
//
		//for (int i = 0; i < points.size() - 1; i++) {
		//	Vec3d current = points.get(i).add(center);
		//	Color color = new Color(RandUtil.nextFloat(), RandUtil.nextFloat(), RandUtil.nextFloat());
//
		//	pos(vb, current).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
		//}
//
		//GL11.glColorMask(true, true, true, true);
		//GL11.glStencilFunc(GL_EQUAL, 1, 1);
		//GL11.glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
//
//
		//GL11.glDisable(GL_STENCIL_TEST);

		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();
	}
}