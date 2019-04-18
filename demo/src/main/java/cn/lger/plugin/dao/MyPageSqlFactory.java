package cn.lger.plugin.dao;

import cn.lger.mybatis.plugin.factory.PageSqlFactory;
import cn.lger.plugin.entity.MyPage;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-04-17.
 */
public class MyPageSqlFactory implements PageSqlFactory<MyPage> {

    @Override
    public String getCountSql(String boundSql) {
        return null;
    }

    @Override
    public String getFinalSql(MyPage page, String boundSql) {
        boundSql = boundSql
                + " LIMIT "
                + page.offset()
                + ", "
                + page.getSize();
        System.err.println(page.getCurrPage());
        return boundSql;
    }

}