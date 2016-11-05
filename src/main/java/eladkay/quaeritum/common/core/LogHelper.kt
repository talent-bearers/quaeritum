package eladkay.quaeritum.common.core

import com.teamwizardry.librarianlib.LibrarianLib
import eladkay.quaeritum.api.lib.LibMisc
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager

object LogHelper {

    var LOGGER = LogManager.getLogger(LibMisc.MOD_ID)

    fun log(s: Any) {
        LOGGER.log(Level.INFO, s)
    }

    fun logDebug(s: Any) {
        if (LibrarianLib.DEV_ENVIRONMENT) log(s)
    }

    fun logErr(s: Any) {
        LOGGER.log(Level.ERROR, s)
    }

    fun logDebugErr(s: Any) {
        if (LibrarianLib.DEV_ENVIRONMENT) logErr(s)
    }

}
