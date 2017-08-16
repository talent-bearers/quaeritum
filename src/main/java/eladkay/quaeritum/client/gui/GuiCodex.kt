package eladkay.quaeritum.client.gui

import com.teamwizardry.librarianlib.features.gui.GuiBase
import com.teamwizardry.librarianlib.features.gui.GuiComponent
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite
import com.teamwizardry.librarianlib.features.gui.components.ComponentVoid
import com.teamwizardry.librarianlib.features.network.PacketHandler
import com.teamwizardry.librarianlib.features.sprite.Texture
import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.api.spell.ElementHandler
import eladkay.quaeritum.api.spell.EnumSpellElement
import eladkay.quaeritum.api.spell.SpellParser
import eladkay.quaeritum.client.render.RenderSymbol
import eladkay.quaeritum.common.networking.ElementAddPacket
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import java.util.*


/**
 * @author WireSegal
 * Created at 1:56 PM on 7/29/17.
 */
class GuiCodex : GuiBase(X_SIZE, Y_SIZE) {
    companion object {
        val X_SIZE = 146
        val Y_SIZE = 180
        val SPRITE_SHEET = Texture(ResourceLocation(LibMisc.MOD_ID, "textures/gui/codex_alchemiae.png"))
        private val background = SPRITE_SHEET.getSprite("background", X_SIZE, Y_SIZE)
        private val active = SPRITE_SHEET.getSprite("active", 14, 22)
        private val inactive = SPRITE_SHEET.getSprite("inactive", 14, 22)
        private val locations = arrayOf(
                64 to 35,
                84 to 40,
                103 to 59,
                108 to 79,
                103 to 99,
                84 to 118,
                64 to 123,
                44 to 118,
                25 to 99,
                20 to 79,
                25 to 59,
                44 to 40
        )
    }

    val symbols: List<ComponentVoid>

    init {
        val componentBackground = ComponentSprite(background, 0, 0)
        mainComponents.add(componentBackground)

        val list = mutableListOf<ComponentVoid>()
        for ((idx, location) in locations.withIndex()) {
            val (x, y) = location
            val componentVoid = ComponentVoid(x, y, 16, 16)
            componentVoid.BUS.hook(GuiComponent.MouseClickEvent::class.java) {
                PacketHandler.NETWORK.sendToServer(ElementAddPacket(idx))
            }
            componentVoid.BUS.hook(GuiComponent.PostDrawEvent::class.java) {
                RenderSymbol.renderSymbol(x + 0.5f, y + 0.5f, EnumSpellElement.values()[idx])
            }
            mainComponents.add(componentVoid)
            list.add(componentVoid)
        }
        symbols = list

        val componentSymbol = ComponentSprite(inactive, 65, 76)
        componentSymbol.BUS.hook(GuiComponent.ComponentTickEvent::class.java, { event ->
            if (event.component.mouseOver && isShiftKeyDown())
                componentSymbol.sprite = active
            else
                componentSymbol.sprite = inactive
        })
        componentSymbol.BUS.hook(GuiComponent.MouseClickEvent::class.java) {
            if (isShiftKeyDown())
                PacketHandler.NETWORK.sendToServer(ElementAddPacket(-1))
            else
                PacketHandler.NETWORK.sendToServer(ElementAddPacket(-2))
        }
        mainComponents.add(componentSymbol)
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)
        val parser = SpellParser(ElementHandler.getReagentsTyped(Minecraft.getMinecraft().player))
        val lines = parser.spells.map { SpellParser.localized(it).formattedText }
        val resolution = ScaledResolution(Minecraft.getMinecraft())
        val shift = Math.max(resolution.scaledWidth / 16, resolution.scaledHeight / 16) - 18
        drawHoveringText(lines, shift, resolution.scaledHeight - shift - 9 * lines.size)
        for ((idx, symbol) in symbols.withIndex())
            if (symbol.mouseOver)
                drawHoveringText(I18n.format("quaeritum.spell." + EnumSpellElement.values()[idx].name.toLowerCase(Locale.ROOT) + ".element"),
                        mouseX, mouseY)
    }

    override fun doesGuiPauseGame(): Boolean {
        return false
    }
}
