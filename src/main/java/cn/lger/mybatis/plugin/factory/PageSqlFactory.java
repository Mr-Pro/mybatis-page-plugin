package cn.lger.mybatis.plugin.factory;

import cn.lger.mybatis.plugin.page.AbstractPage;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-04-17.
 */
public interface PageSqlFactory<T extends AbstractPage> {

    /**
     * 通过发出的sql得到获取总记录数的SQL
     * @param boundSql mybatis 发出的sql
     * @return 查询总记录数SQL
     */
    String getCountSql(String boundSql);

    /**
     * 获取最终发出的SQL
     *
     * @param page 继承自 {@link AbstractPage} 的参数
     * @param boundSql mybatis 发出的sql
     * @return 最终分页SQL
     */
    String getFinalSql(T page, String boundSql);

}
