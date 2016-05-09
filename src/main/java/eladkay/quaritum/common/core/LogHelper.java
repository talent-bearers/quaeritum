package eladkay.quaritum.common.core;

import eladkay.quaritum.common.Quartium;
import eladkay.quaritum.common.lib.LibMisc;

public class LogHelper {
    public static void log(Object s) {
        System.out.println(LibMisc.MOD_ID + ": " + s);
    }

    public static void logDebug(Object s) {
        if (Quartium.isDevEnv) log(s);
    }

    public static void logErr(Object s) {
        System.err.println(LibMisc.MOD_ID + ": " + s);
    }

    public static void logDebugErr(Object s) {
        if (Quartium.isDevEnv) logErr(s);
    }

}
