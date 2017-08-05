package eladkay.quaeritum.client.core;

import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
import eladkay.quaeritum.api.spell.ElementHandler;
import eladkay.quaeritum.api.spell.EnumSpellElement;
import eladkay.quaeritum.client.gui.GuiCodex;
import eladkay.quaeritum.client.render.RenderSymbol;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
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

		if (symbols.length <= 0) return;

		boolean isGui = Minecraft.getMinecraft().currentScreen instanceof GuiCodex;

		if (wasPreviouslyGui != isGui) {
			wasPreviouslyGui = isGui;
			for (int i = 0; i < symbols.length; i++) {
				symbolsTick[i] = 0;
				symbolsShouldTick[i] = false;
			}
		}

		double time = 30;

		double cX = event.getResolution().getScaledWidth() / 2.0;
		double cY = event.getResolution().getScaledHeight() / 2.0;
		double scaleMin = event.getResolution().getScaleFactor() * 15;
		double scaleMax = scaleMin * 5;

		double startingAngle = (event.getPartialTicks() + ClientTickHandler.getTicks()) * Math.PI / 120;
		double angleSep = 2.0 * Math.PI / symbols.length + 2;
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
		}

		GlStateManager.pushMatrix();

		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.enableBlend();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.enableTexture2D();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		for (int i = 0; i < symbolsShouldTick.length; i++) {
			if (symbolsShouldTick[i]) {
				symbolsTick[i] = symbolsTick[i] + 1;
			}
		}

		for (int i = 0; i < symbols.length; i++) {
			EnumSpellElement element = symbols[i];
			if (element == null) continue;

			double scale = symbolsScale[i];

			if (symbolsTick[i] < time) {

				if (!symbolsShouldTick[i]) {
					symbolsShouldTick[i] = true;
				}

				if (wasPreviouslyGui) {
					scale = symbolsScale[i] = 1 - (MathHelper.sqrt(1 - Math.pow(1 - (symbolsTick[i] / time), 2))) * (isGui ? scaleMax : scaleMin) + event.getPartialTicks();
				} else {
					scale = symbolsScale[i] = -scaleMin + -(1 - (MathHelper.sqrt(1 - Math.pow(1 - (symbolsTick[i] / time), 2)))) * (isGui ? scaleMax : scaleMin) + event.getPartialTicks();
				}
			} else if (symbolsShouldTick[i]) {
				symbolsShouldTick[i] = false;
			}

			double angle = startingAngle + (i + 1) * currentAngle;
			double x = cX + scale * MathHelper.cos((float) angle) - 7.5;
			double y = cY + scale * MathHelper.sin((float) angle) - 7.5;
			RenderSymbol.INSTANCE.renderSymbol((float) x, (float) y, element);
		}

		GlStateManager.popMatrix();
	}
}