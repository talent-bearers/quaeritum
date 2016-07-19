package eladkay.quaritum.common.core;

import eladkay.quaritum.common.Quaritum;
import eladkay.quaritum.api.lib.LibMisc;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper {

    public static Logger LOGGER = LogManager.getLogger(LibMisc.MOD_ID);

    public static void log(Object s) {
        LOGGER.log(Level.INFO, s);
    }

    public static void logDebug(Object s) {
        if (Quaritum.isDevEnv) log(s);
    }

    public static void logErr(Object s) {
        LOGGER.log(Level.ERROR, s);
    }

    public static void logDebugErr(Object s) {
        if (Quaritum.isDevEnv) logErr(s);
    }

}
