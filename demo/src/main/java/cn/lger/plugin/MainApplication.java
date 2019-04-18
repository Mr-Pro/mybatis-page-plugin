package cn.lger.plugin;

import cn.lger.mybatis.plugin.page.Page;
import cn.lger.mybatis.plugin.utils.ThreadLocalUtil;
import cn.lger.plugin.dao.UserDao;
import cn.lger.plugin.entity.MyPage;
import cn.lger.plugin.entity.UserEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
@SpringBootApplication
public class MainApplication {


    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(MainApplication.class, args);
        UserDao userDao = context.getBean(UserDao.class);

        //自定义分页 MyPage

//        MyPage<UserEntity> page = new MyPage<>(3, 1);
//        System.err.println(userDao.findByName(page, "187636567@qq.com"));
//        System.err.println(page);

        //分页使用
        Page<UserEntity> page = Page.of(2, 3);

        System.err.println(userDao.findAll(page));
        System.err.println(page);

        List<UserEntity> list  = userDao.findByName(page, "187636567@qq.com");
        System.err.println(list);
        System.err.println(page);

        list = userDao.findByName("187636567@qq.com", page);
        System.err.println(list);
        System.err.println(page);

        System.err.println(userDao.findByName("187636567@qq.com"));
    }
}
