/**
 * This class was created by . It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 *
 * File Created @ [16/01/2016, 18:30:59 (GMT)]
 */
package eladkay.quaritum.client.core

import eladkay.quaritum.api.lib.LibMisc
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.resources.I18n

object TooltipHelper {

    fun tooltipIfShift(tooltip: MutableList<String>, r: Runnable) {
        if (GuiScreen.isShiftKeyDown())
            r.run()
        else
            addToTooltip(tooltip, "misc." + LibMisc.MOD_ID + ".shiftForInfo")
    }

    fun addToTooltip(tooltip: MutableList<String>, s: String, vararg format: Any) {
        tooltip.add(local(s, *format).replace("&".toRegex(), "\u00a7"))
    }

    fun local(s: String, vararg format: Any): String {
        try {
            return I18n.format(s, *format)
        } catch (server: Error) {
            return s
        }

        //this is actually expected
    }

}
