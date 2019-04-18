package cn.lger.mybatis.plugin.utils;

import cn.lger.mybatis.plugin.page.AbstractPage;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-04-18.
 */
public class ThreadLocalUtil {

    private static ThreadLocal<AbstractPage> pageThreadLocal;

    private ThreadLocalUtil() {
    }

    public static ThreadLocal<AbstractPage> getPageThreadLocal() {
        if (pageThreadLocal == null) {
            synchronized (ThreadLocalUtil.class) {
                if (pageThreadLocal == null) {
                    pageThreadLocal = new ThreadLocal<AbstractPage>();
                }
            }
        }
        return pageThreadLocal;
    }
}
