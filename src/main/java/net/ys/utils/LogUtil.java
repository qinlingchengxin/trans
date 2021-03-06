package net.ys.utils;

import org.apache.log4j.Logger;

/**
 * User: NMY
 * Date: 18-4-26
 */
public class LogUtil {

    private static Logger log = Logger.getLogger(LogUtil.class);

    public static void error(Exception e) {
        log.error(e, e);
    }

    public static void info(Object msg) {
        log.info(msg);
    }

    public static void debug(Object... messages) {
        for (Object msg : messages) {
            System.out.println(System.currentTimeMillis() + " debug-msg: " + msg);
        }
    }
}
