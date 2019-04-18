package cn.lger.mybatis.plugin.utils;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-04-18.
 */
public class StringUtil {

    private StringUtil() {}

    public static boolean isBlank(String s) {
        return s == null || "".equals(s.trim());
    }
}
