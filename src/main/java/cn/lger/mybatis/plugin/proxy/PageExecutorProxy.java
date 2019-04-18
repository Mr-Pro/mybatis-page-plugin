package cn.lger.mybatis.plugin.proxy;

import cn.lger.mybatis.plugin.page.AbstractPage;
import cn.lger.mybatis.plugin.utils.ThreadLocalUtil;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-04-18.
 */
public class PageExecutorProxy extends CachingExecutor {

    public PageExecutorProxy(Executor delegate) {
        super(delegate);
    }

    /**
     * 由于使用缓存，不确定用户端会不会使用rowBounds
     * 不使用则存在分页与不分页都有可能查询出缓存数据（脏数据），
     * 这里cacheKey生成需要再根据自己的分页计算，所以RowBounds采用自己实现
     * @param ms MappedStatement
     * @param parameterObject Object
     * @param rowBounds RowBounds
     * @param boundSql BoundSql
     * @return CacheKey
     */
    @Override
    public CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql) {
        AbstractPage page = ThreadLocalUtil.getPageThreadLocal().get();
        if (page != null) {
            rowBounds = new RowBounds(page.offset(), page.limit());
        }
        return super.createCacheKey(ms, parameterObject, rowBounds, boundSql);
    }
}
