/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 18:30:59 (GMT)]
 */
package eladkay.quaritum.client.core;

import eladkay.quaritum.api.lib.LibMisc;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.util.List;

public final class TooltipHelper {

    public static void tooltipIfShift(List<String> tooltip, Runnable r) {
        if(GuiScreen.isShiftKeyDown())
            r.run();
        else
            addToTooltip(tooltip, "misc." + LibMisc.MOD_ID + ".shiftForInfo");
    }

    public static void addToTooltip(List<String> tooltip, String s, Object... format) {
        tooltip.add(local(s, format).replaceAll("&", "\u00a7"));
    }

    public static String local(String s, Object... format) {
        try {
            return I18n.format(s, format);
        } catch(Error server) { return s; }
        //this is actually expected
    }

}
