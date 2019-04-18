package cn.lger.plugin.dao;

import cn.lger.mybatis.plugin.page.Page;
import cn.lger.plugin.entity.MyPage;
import cn.lger.plugin.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-04-17.
 */
@Mapper
public interface UserDao {

    /**
     * 查找所有
     *
     * @param page page
     * @return List<UserEntity>
     */
    List<UserEntity> findAll(Page<UserEntity> page);

    /**
     * 根据名字查找
     *
     * @param name name
     * @param page page
     * @return List<UserEntity>
     */
    List<UserEntity> findByName(@Param("name") String name, Page<UserEntity> page);

    /**
     * 根据名字查找
     * @param name name
     * @param page page
     * @return List<UserEntity>
     */
    List<UserEntity> findByName(Page<UserEntity> page, @Param("name") String name);

    /**
     * 根据名字查找
     * @param name name
     * @return List<UserEntity>
     */
    List<UserEntity> findByName(@Param("name") String name);

    /**
     * 根据名字查找
     * @param name name
     * @param page page
     * @return List<UserEntity>
     */
    List<UserEntity> findByName(MyPage<UserEntity> page, @Param("name") String name);

}
