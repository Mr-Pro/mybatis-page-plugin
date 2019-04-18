# mybatis-page-plugin
MyBatis 分页插件，通过MyBatis 过滤器实现，开发时采用版本为`3.4.4`
[jar包地址](https://github.com/Mr-Pro/mybatis-page-plugin/releases/tag/1.0.1.RELEASE)

# 说明


### MyBatis 配置
```xml
<configuration>
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
        <!--<setting name="cacheEnabled" value="false"/>-->
    </settings>
    <plugins>
        <plugin interceptor="cn.lger.mybatis.plugin.PagePlugin">
            <!--可以自定义生成PageSQL的工程类，默认MySQL-->
            <!--<property name="pageSqlFactory" value="cn.lger.plugin.dao.MyPageSqlFactory"/>-->
            <!--此为默认值-->
            <!--<property name="pageSqlFactory" value="cn.lger.mybatis.plugin.factory.MySqlPageSqlFactory"/>-->
        </plugin>
    </plugins>
</configuration>

```
### PageSqlFactory

这是一个专门用于生产出分页语句的工程接口，具体的实现如：
`cn.lger.mybatis.plugin.factory.MySqlPageSqlFactory`,
其可以对MyBatis的绑定SQL重新更改，返回给PagePlugin做处理。
这里只实现了MySQL，如果需要其他数据库支持，直接继承此接口实现即可


### AbstractPage

这是一个可继承重新实现的类，需要提供具体的实现方法，具体实现如：
`cn.lger.mybatis.plugin.page.Page`


****

### DEMO

这里包含了一个[demo](./demo)项目
里面说了写了一些使用方法，如：定制Page和PageSqlFactory
在引入项目前需要导入数据库[test.sql](./demo/test.sql)及修改`application-dev.yml`中的数据库连接配置


