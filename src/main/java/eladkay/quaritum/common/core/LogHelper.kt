package eladkay.quaritum.common.core

import eladkay.quaritum.api.lib.LibMisc
import eladkay.quaritum.common.Quaritum
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager

object LogHelper {

    var LOGGER = LogManager.getLogger(LibMisc.MOD_ID)

    fun log(s: Any) {
        LOGGER.log(Level.INFO, s)
    }

    fun logDebug(s: Any) {
        if (Quaritum.isDevEnv) log(s)
    }

    fun logErr(s: Any) {
        LOGGER.log(Level.ERROR, s)
    }

    fun logDebugErr(s: Any) {
        if (Quaritum.isDevEnv) logErr(s)
    }

}
