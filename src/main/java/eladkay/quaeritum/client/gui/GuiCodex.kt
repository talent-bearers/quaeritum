package eladkay.quaeritum.client.gui

import com.teamwizardry.librarianlib.features.gui.GuiBase
import com.teamwizardry.librarianlib.features.gui.GuiComponent
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite
import com.teamwizardry.librarianlib.features.gui.components.ComponentVoid
import com.teamwizardry.librarianlib.features.network.PacketHandler
import com.teamwizardry.librarianlib.features.sprite.Texture
import eladkay.quaeritum.api.lib.LibMisc
import eladkay.quaeritum.api.spell.EnumSpellElement
import eladkay.quaeritum.client.render.RenderSymbol
import eladkay.quaeritum.common.networking.ElementAddPacket
import net.minecraft.client.settings.KeyBinding
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse




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

    init {
        val componentBackground = ComponentSprite(background, 0, 0)
        mainComponents.add(componentBackground)

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
        }

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

    override fun updateScreen() {
        super.updateScreen()

        for (k in setOf(
                mc.gameSettings.keyBindForward,
                mc.gameSettings.keyBindLeft,
                mc.gameSettings.keyBindBack,
                mc.gameSettings.keyBindRight,
                mc.gameSettings.keyBindSneak,
                mc.gameSettings.keyBindSprint,
                mc.gameSettings.keyBindJump))
            KeyBinding.setKeyBindState(k.keyCode, isKeyDown(k))
    }

    fun isKeyDown(keybind: KeyBinding): Boolean {
        val key = keybind.keyCode
        if (key < 0) {
            val button = 100 + key
            return Mouse.isButtonDown(button)
        }
        return Keyboard.isKeyDown(key)
    }

    override fun doesGuiPauseGame(): Boolean {
        return false
    }
}
