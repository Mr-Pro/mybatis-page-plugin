package cn.lger.mybatis.plugin;

import cn.lger.mybatis.plugin.factory.PageSqlFactory;
import cn.lger.mybatis.plugin.page.AbstractPage;
import cn.lger.mybatis.plugin.proxy.PageExecutorProxy;
import cn.lger.mybatis.plugin.utils.SimpleReflectUtil;
import cn.lger.mybatis.plugin.utils.StringUtil;
import cn.lger.mybatis.plugin.utils.ThreadLocalUtil;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * MIT License
 *
 * Copyright (c) 2019 Mr-Pro
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * @author Pro
 * @date 2019-04-17.
 */
@Intercepts({
        @Signature(type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class, Integer.class}),
        @Signature(type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class PagePlugin implements Interceptor {

    private final Log log = LogFactory.getLog(PagePlugin.class);

    private static final String DEFAULT_PAGE_SQL_FACTORY = "cn.lger.mybatis.plugin.factory.MySqlPageSqlFactory";

    private PageSqlFactory<AbstractPage> sqlFactory;

    @Override
    @SuppressWarnings("unchecked")
    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getTarget() instanceof RoutingStatementHandler) {
            RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();
            StatementHandler delegate = (StatementHandler) SimpleReflectUtil.getFieldValue(statementHandler, "delegate");
            //通过反射获取delegate父类BaseStatementHandler的mappedStatement属性
            if (!(delegate instanceof BaseStatementHandler)){
                return invocation.proceed();
            }
            BoundSql boundSql = delegate.getBoundSql();
            //查询出是否含有Page对象
            AbstractPage page = ThreadLocalUtil.getPageThreadLocal().get();
            if (page == null) {
                return invocation.proceed();
            }
            //获取Mybatis当前生成SQL并转为分页SQL
            String sql = boundSql.getSql();
            //mybatis 最终发出的SQL
            String finalSql = sqlFactory.getFinalSql(page, sql);
            if (StringUtil.isBlank(finalSql)) {
                return invocation.proceed();
            }
            MappedStatement mappedStatement = (MappedStatement) SimpleReflectUtil.getSuperClassFieldValue(delegate, "mappedStatement");
            if (mappedStatement == null) {
                return invocation.proceed();
            }
            //拦截到的prepare方法参数是一个Connection对象
            Connection connection = (Connection) invocation.getArgs()[0];
            //给当前的page参数对象设置总记录数，此时BoundSql不能被改变为分页SQL，需要等count统计后
            setPageCount(boundSql, page, mappedStatement, connection);
            //利用反射设置当前BoundSql对应的sql属性为我们建立好的分页Sql语句
            SimpleReflectUtil.setFieldValue(boundSql, "sql", finalSql);
            return invocation.proceed();
        } else if (invocation.getTarget() instanceof Executor){
            long start = 0;
            if (log.isDebugEnabled()) {
                start = System.currentTimeMillis();
                log.debug("Mybatis 分页拦截器开始：" + start);
            }
            AbstractPage page = scanPageArgs(invocation.getArgs()[1]);
            if (page == null){
                if (log.isDebugEnabled()) {
                    long end = System.currentTimeMillis();
                    log.debug("Mybatis 分页拦截器结束：" + end + " 耗时：" + (end - start) + "ms");
                }
                return invocation.proceed();
            }
            ThreadLocalUtil.getPageThreadLocal().set(page);
            Object proceedObj = invocation.proceed();
            page.setPageBody((List) proceedObj);
            if (log.isDebugEnabled()) {
                long end = System.currentTimeMillis();
                log.debug("Mybatis 分页拦截器结束：" + end + " 耗时：" + (end - start) + "ms");
            }

            ThreadLocalUtil.getPageThreadLocal().remove();
            return proceedObj;
        }
        return invocation.proceed();
    }

    private void setPageCount(BoundSql boundSql, AbstractPage page, MappedStatement mappedStatement, Connection connection) {
        //获取到我们自己写在Mapper映射语句中对应的Sql语句
        String sql = boundSql.getSql();
        //通过查询Sql语句获取到对应的计算总记录数的sql语句
        String countSql = sqlFactory.getCountSql(sql);
        //如果为空则不执行count sql
        if (StringUtil.isBlank(countSql)) {
            return;
        }
        //Dao中的参数
        Object param = boundSql.getParameterObject();
        //通过BoundSql获取对应的参数映射
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        //利用Configuration、查询记录数的Sql语句countSql、参数映射关系parameterMappings和参数对象page建立查询记录数对应的BoundSql对象。
        BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, parameterMappings, param);
        //通过mappedStatement、参数对象page和BoundSql对象countBoundSql建立一个用于设定参数的ParameterHandler对象
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, param, countBoundSql);
        //通过connection建立一个countSql对应的PreparedStatement对象。
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(countSql);
            //通过parameterHandler给PreparedStatement对象设置参数
            parameterHandler.setParameters(preparedStatement);
            //之后就是执行获取总记录数的Sql语句和获取结果了。
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int totalRecord = resultSet.getInt(1);
                //给当前的参数page对象设置总记录数
                page.setPageCount(totalRecord);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 扫描出参数中是否包含page
     * @param queryParam mybatis mapper 发出的查询参数
     * @return page
     */
    private AbstractPage scanPageArgs(Object queryParam) {
        //这里是因为mybatis对多个参数会进行map包装
        if (queryParam instanceof Map) {
            for (Object val : ((Map) queryParam).values()) {
                if (val instanceof AbstractPage) {
                    return (AbstractPage) val;
                }
            }
        } else if (queryParam instanceof AbstractPage) {
            return (AbstractPage) queryParam;
        }
        return null;
    }


    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            if (target instanceof CachingExecutor) {
                Executor delegate = null;
                try {
                     delegate = (Executor) SimpleReflectUtil.getFieldValue(target, "delegate");
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (delegate == null) {
                    return Plugin.wrap(target, this);
                }
                return Plugin.wrap(new PageExecutorProxy(delegate), this);
            }
            return Plugin.wrap(target, this);
        } else if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setProperties(Properties properties) {
        String className = properties.getProperty("pageSqlFactory");
        if (StringUtil.isBlank(className)) {
            className = DEFAULT_PAGE_SQL_FACTORY;
        }
        //使用类加载方式创建对应PageSqlFactory实例
        try {
            Class factoryClass = Class.forName(className);
            sqlFactory = (PageSqlFactory<AbstractPage>) factoryClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
