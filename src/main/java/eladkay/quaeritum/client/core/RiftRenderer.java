package eladkay.quaeritum.client.core;

import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
import eladkay.quaeritum.api.util.RandUtilSeed;
import eladkay.quaeritum.common.core.SightHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL14.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL14.GL_FUNC_SUBTRACT;

/**
 * Created by LordSaad.
 */
public class RiftRenderer {

	private final Vec3d center;
	private final long seed;

	private ArrayList<Vec3d> points1 = new ArrayList<>();
	private ArrayList<Vec3d> points2 = new ArrayList<>();
	private ArrayList<Vec3d> points3 = new ArrayList<>();
	private ArrayList<Vec3d> points4 = new ArrayList<>();

	private ArrayList<Vec3d> normalPoints1 = new ArrayList<>();
	private ArrayList<Vec3d> normalPoints2 = new ArrayList<>();
	private ArrayList<Vec3d> normalPoints3 = new ArrayList<>();
	private ArrayList<Vec3d> normalPoints4 = new ArrayList<>();

	private double JAGGED_RESOLUTION = 0.1;
	private double SEGMENT_COUNT = 5;

	private boolean flip1 = true;
	private boolean flip2 = true;
	private boolean flip3 = true;

	public RiftRenderer(Vec3d center, long seed) {
		this.center = center;
		this.seed = seed;
		MinecraftForge.EVENT_BUS.register(this);

		RandUtilSeed rand = new RandUtilSeed(seed);
		ArrayList<Vec3d> points1 = new ArrayList<>();
		ArrayList<Vec3d> points2 = new ArrayList<>();

		points1.add(new Vec3d(0, 20, 0));
		points1.add(new Vec3d(4, 10, 0));
		points1.add(new Vec3d(0, 0, 0));

		points2.add(new Vec3d(0, 20, 0));
		points2.add(new Vec3d(-4, 10, 0));
		points2.add(new Vec3d(0, 0, 0));

		for (int i = 0; i < points1.size() - 1; i++) {
			Vec3d point = points1.get(i);
			Vec3d secPoint = points1.get(i + 1);

			this.points1.add(point);

			Vec3d sub = secPoint.subtract(point);

			for (int j = 0; j < SEGMENT_COUNT; j++) {
				double x = j / SEGMENT_COUNT;
				Vec3d scale = sub.scale(x).add(point).addVector(
						rand.nextDouble(-JAGGED_RESOLUTION, JAGGED_RESOLUTION),
						rand.nextDouble(-JAGGED_RESOLUTION, JAGGED_RESOLUTION),
						0);
				this.points1.add(scale);
			}
		}
		this.points1.add(points1.get(points1.size() - 1));

		for (int i = 0; i < points2.size() - 1; i++) {
			Vec3d point = points2.get(i);
			Vec3d secPoint = points2.get(i + 1);

			this.points2.add(point);

			Vec3d sub = secPoint.subtract(point);

			for (int j = 0; j < SEGMENT_COUNT; j++) {
				double x = j / SEGMENT_COUNT;
				Vec3d scale = sub.scale(x).add(point).addVector(
						rand.nextDouble(-JAGGED_RESOLUTION, JAGGED_RESOLUTION),
						rand.nextDouble(-JAGGED_RESOLUTION, JAGGED_RESOLUTION),
						0);
				this.points2.add(scale);
			}
		}
		this.points2.add(points2.get(points2.size() - 1));

		for (Vec3d point : this.points1) {
			Vec3d newpoint = new Vec3d(point.x + rand.nextDouble(-1, 1), point.y + rand.nextDouble(-1, 1), point.z + rand.nextDouble(-1, 1));
			points3.add(newpoint);
		}

		for (Vec3d point : this.points2) {
			Vec3d newpoint = new Vec3d(point.x + rand.nextDouble(-1, 1), point.y + rand.nextDouble(-1, 1), point.z + rand.nextDouble(-1, 1));
			points4.add(newpoint);
		}

		// NORMALS

		for (int i = 0; i < this.points1.size() - 1; i++) {
			Vec3d point1 = this.points1.get(i);
			Vec3d point2 = this.points1.get(i + 1);
			Vec3d sub = point1.subtract(point2);
			Vec3d cross = sub.crossProduct(new Vec3d(0, 0, 1)).normalize();

			normalPoints1.add(cross);
		}
		for (int i = 0; i < points3.size() - 1; i++) {
			Vec3d point1 = points3.get(i);
			Vec3d point2 = points3.get(i + 1);
			Vec3d sub = point1.subtract(point2);
			Vec3d cross = sub.crossProduct(new Vec3d(0, 0, 1)).normalize();

			normalPoints3.add(cross);
		}
		for (int i = 0; i < this.points2.size() - 1; i++) {
			Vec3d point1 = this.points2.get(i);
			Vec3d point2 = this.points2.get(i + 1);
			Vec3d sub = point1.subtract(point2);
			Vec3d cross = sub.crossProduct(new Vec3d(0, 0, -1)).normalize();

			normalPoints2.add(cross);
		}
		for (int i = 0; i < points4.size() - 1; i++) {
			Vec3d point1 = points4.get(i);
			Vec3d point2 = points4.get(i + 1);
			Vec3d sub = point1.subtract(point2);
			Vec3d cross = sub.crossProduct(new Vec3d(0, 0, -1)).normalize();

			normalPoints4.add(cross);
		}
	}

	private static BufferBuilder pos(BufferBuilder vb, Vec3d pos) {
		return vb.pos(pos.x, pos.y, pos.z);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void render(RenderWorldLastEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().player;

        if (!SightHandler.INSTANCE.hasTheSight(player)) return;

        GlStateManager.pushMatrix();

		double interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
		double interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
		double interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();

		GlStateManager.translate(-interpPosX, -interpPosY, -interpPosZ);

		GlStateManager.disableCull();
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.disableTexture2D();

		Color color = new Color(0x96A500B7, true);

		GlStateManager.rotate(Minecraft.getMinecraft().player.rotationYaw, 0, -1, 0);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vb = tessellator.getBuffer();

		// RIFT

		GL14.glBlendEquation(GL_FUNC_SUBTRACT);
        GlStateManager.depthMask(false);

		vb.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);
		for (int i = 0; i < points1.size(); i += (flip1 ? 1 : 0)) {
			Vec3d point;
			if (flip1) point = points1.get(i);
			else point = points3.get(i);

			flip1 = !flip1;

			point = point.add(
					new Vec3d(
							MathHelper.sin((float) (((ClientTickHandler.getTicks() / 30.0) + point.lengthVector()))),
							MathHelper.sin((float) (((ClientTickHandler.getTicks() / 30.0) + point.lengthVector() * 0.5))),
							MathHelper.sin((float) (((ClientTickHandler.getTicks() / 30.0) + point.lengthVector() * 1.5)))).scale(0.3));

			pos(vb, point).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
		}
		tessellator.draw();

		vb.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);
		for (int i = 0; i < points2.size(); i += (flip2 ? 1 : 0)) {
			Vec3d point;
			if (flip2) point = points2.get(i);
			else point = points4.get(i);

			flip2 = !flip2;

			point = point.add(
					new Vec3d(
							MathHelper.sin((float) (((ClientTickHandler.getTicks() / 30.0) + point.lengthVector()))),
							MathHelper.sin((float) (((ClientTickHandler.getTicks() / 30.0) + point.lengthVector() * 0.5))),
							MathHelper.sin((float) (((ClientTickHandler.getTicks() / 30.0) + point.lengthVector() * 1.5)))).scale(0.3));

			pos(vb, point).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
		}
		tessellator.draw();

		vb.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);
		for (int i = 0; i < points3.size(); i += (flip3 ? 1 : 0)) {
			Vec3d point;
			if (flip3) point = points3.get(i);
			else point = points4.get(i);

			flip3 = !flip3;

			point = point.add(
					new Vec3d(
							MathHelper.sin((float) (((ClientTickHandler.getTicks() / 30.0) + point.lengthVector()))),
							MathHelper.sin((float) (((ClientTickHandler.getTicks() / 30.0) + point.lengthVector() * 0.5))),
							MathHelper.sin((float) (((ClientTickHandler.getTicks() / 30.0) + point.lengthVector() * 1.5)))).scale(0.3));

			pos(vb, point).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
		}
		tessellator.draw();

		GL14.glBlendEquation(GL_FUNC_ADD);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();
	}
}
