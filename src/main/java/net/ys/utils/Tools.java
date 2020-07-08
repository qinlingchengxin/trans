package net.ys.utils;

import net.ys.constant.X;

import java.security.MessageDigest;

/**
 * User: NMY
 * Date: 16-9-8
 */
public class Tools {

    public static String genMD5(String key) {
        try {
            if (key == null || "".equals(key.trim())) {
                return "";
            }
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(key.getBytes(X.ENCODING.U));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bs.length; i++) {
                sb.append(Character.forDigit((bs[i] >>> 4) & 0x0F, 16)).append(Character.forDigit(bs[i] & 0x0F, 16));
            }
            return sb.toString();
        } catch (Exception e) {
        }
        return "";
    }
}
