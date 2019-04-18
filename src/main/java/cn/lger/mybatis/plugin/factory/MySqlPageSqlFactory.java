package cn.lger.mybatis.plugin.factory;

import cn.lger.mybatis.plugin.page.AbstractPage;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-04-18.
 */
public class MySqlPageSqlFactory implements PageSqlFactory<AbstractPage> {

    @Override
    public String getCountSql(String boundSql) {
        return "SELECT COUNT(1) AS TEMP FROM (" + boundSql + ") AS TEMP_DATA";
    }

    @Override
    public String getFinalSql(AbstractPage page, String boundSql) {
        boundSql = boundSql
                + " LIMIT "
                + page.offset()
                + ", "
                + page.limit();
        return boundSql;
    }

}
