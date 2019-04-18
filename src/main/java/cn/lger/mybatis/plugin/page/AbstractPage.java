package cn.lger.mybatis.plugin.page;

import java.util.List;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-04-17.
 */
public abstract class AbstractPage<T> {

    public abstract void setPageBody(List<T> list);

    public abstract void setPageCount(int count);

    public abstract int limit();

    public abstract int offset();

}
