package eladkay.quaeritum.client.core;

import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
import eladkay.quaeritum.api.spell.ElementHandler;
import eladkay.quaeritum.api.spell.EnumSpellElement;
import eladkay.quaeritum.api.spell.render.RenderUtil;
import eladkay.quaeritum.client.gui.GuiCodex;
import eladkay.quaeritum.client.render.RenderSymbol;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;

/**
 * Created by LordSaad.
 */
public class HudSymbolRenderer {

	public static HudSymbolRenderer INSTANCE = new HudSymbolRenderer();

	private EnumSpellElement[] symbols = new EnumSpellElement[9];
	private boolean[] symbolsShouldTick = new boolean[9];
	private int[] symbolsTick = new int[9];
	private double[] symbolsScale = new double[9];

	private double currentAngle = -1;
	private double angleTick = 0;
	private double prevAngle = 0;
	private boolean angleShouldTick = false;

	private boolean wasPreviouslyGui = false;

	private boolean sepShouldTick = true;
	private int sepTick = 0;
	private double sepScale = 0;

	private HudSymbolRenderer() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void addSymbol(EnumSpellElement spellElement) {
		symbols[symbols.length - 1] = spellElement;
		int index = symbols.length - 1;
		symbolsTick[index] = 0;
		symbolsShouldTick[index] = false;
		symbolsScale[index] = 0.0;
	}

	private void clear() {
		symbols = new EnumSpellElement[9];
		symbolsShouldTick = new boolean[9];
		Arrays.fill(symbolsShouldTick, false);
		symbolsTick = new int[9];
		symbolsScale = new double[9];
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void render(RenderGameOverlayEvent.Post event) {
		if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

		EntityPlayer player = Minecraft.getMinecraft().player;

		EnumSpellElement[] elements = ElementHandler.getReagentsTyped(Minecraft.getMinecraft().player);

		boolean isGui = Minecraft.getMinecraft().currentScreen instanceof GuiCodex;
		double cX = event.getResolution().getScaledWidth() / 2.0;
		double cY = event.getResolution().getScaledHeight() / 2.0;
		double scaleMin = event.getResolution().getScaleFactor() * 15;
		double scaleMax = scaleMin * 5;
		double time = 30;

		if (elements.length > symbols.length) {
			for (int i = symbols.length; i < elements.length; i++) {
				symbolsTick[i] = 0;
				symbolsShouldTick[i] = false;
				symbolsScale[i] = 0.0;
			}
			angleShouldTick = true;
			angleTick = 0;
		} else if (elements.length < symbols.length) {
			clear();
		}
		symbols = elements;

		if (wasPreviouslyGui != isGui) {
			wasPreviouslyGui = isGui;
			for (int i = 0; i < symbols.length; i++) {
				symbolsTick[i] = 0;
				symbolsShouldTick[i] = false;
			}
			sepShouldTick = true;
			sepTick = 0;
		}

		double startingAngle = (event.getPartialTicks() + ClientTickHandler.getTicks()) * Math.PI / 120;
		double angleSep = 2.0 * Math.PI / (symbols.length + 1);
		if (currentAngle == -1) {
			prevAngle = currentAngle = angleSep;
			angleTick = time;
		}

		if (currentAngle < angleSep) {
			if (!angleShouldTick) angleShouldTick = true;
		}
		if (angleTick < time) {
			angleTick++;
			currentAngle = currentAngle + (angleTick / time) * (angleSep - currentAngle);
		} else {
			angleShouldTick = false;
			prevAngle = angleSep;
		}


		for (int i = 0; i < symbolsShouldTick.length; i++) {
			if (symbolsShouldTick[i]) {
				symbolsTick[i] = symbolsTick[i] + 1;
			}
		}

		// SEPARATOR //
		{
			if (!isGui && symbols.length <= 0) return;
			if (sepShouldTick) sepTick++;

			double scale = sepScale;

			if (sepTick < time) {

				if (!sepShouldTick) {
					sepShouldTick = true;
				}

				if (wasPreviouslyGui) {
					scale = sepScale = Math.abs(1 - (MathHelper.sqrt(1 - Math.pow(1 - (sepTick / time), 2))) * (isGui ? scaleMax : scaleMin) + event.getPartialTicks());
				} else {
					scale = sepScale = Math.abs(-scaleMin + -(1 - (MathHelper.sqrt(1 - Math.pow(1 - (sepTick / time), 2)))) * (isGui ? scaleMax : scaleMin) + event.getPartialTicks());
				}
			} else if (sepShouldTick) {
				sepShouldTick = false;
			}

			GlStateManager.color(1f, 1f, 1f, 1f);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
			GlStateManager.disableTexture2D();
			Tessellator tess = Tessellator.getInstance();
			VertexBuffer buffer = tess.getBuffer();
			buffer.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_COLOR);
			RenderUtil.renderNGon(buffer,
					cX + MathHelper.cos((float) startingAngle) * scale - 0.5,
					cY + MathHelper.sin((float) startingAngle) * scale - 0.5,
					1f, 1f, 1f, 7.5, 5.0, RenderUtil.SEGMENTS_CIRCLE);
			tess.draw();
		}
		// SEPARATOR //

		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.enableBlend();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.enableTexture2D();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		if (symbols.length > 0)
			for (int i = 0; i < symbols.length; i++) {
				EnumSpellElement element = symbols[i];
				if (element == null) continue;

				double scale = symbolsScale[i];

				if (symbolsTick[i] < time) {

					if (!symbolsShouldTick[i]) {
						symbolsShouldTick[i] = true;
					}

					if (wasPreviouslyGui) {
						scale = symbolsScale[i] = Math.abs(1 - (MathHelper.sqrt(1 - Math.pow(1 - (symbolsTick[i] / time), 2))) * (isGui ? scaleMax : scaleMin) + event.getPartialTicks());
					} else {
						scale = symbolsScale[i] = Math.abs(-scaleMin + -(1 - (MathHelper.sqrt(1 - Math.pow(1 - (symbolsTick[i] / time), 2)))) * (isGui ? scaleMax : scaleMin) + event.getPartialTicks());
					}
				} else if (symbolsShouldTick[i]) {
					symbolsShouldTick[i] = false;
				}

				double angle = startingAngle + (i + 1) * currentAngle;
				double x = cX + scale * MathHelper.cos((float) angle) - 7.5;
				double y = cY + scale * MathHelper.sin((float) angle) - 7.5;
				RenderSymbol.INSTANCE.renderSymbol((float) x, (float) y, element);
			}

		//GlStateManager.popMatrix();
	}
}