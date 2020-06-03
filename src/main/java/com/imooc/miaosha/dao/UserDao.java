package com.imooc.miaosha.dao;

import com.imooc.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.*;

/**
 * @ClassName UserDao
 * @Description TODO
 * @Date 2020/5/6 7:02
 * @Created by typ
 */
@Mapper
public interface UserDao {

    /**
     * 根据用户ID找到用户
     * @param id    用户ID
     * @return
     */
    @Select("select * from miaosha_user where id = #{id}")
    MiaoshaUser getById(@Param("id") Long id);

    /**
     * 更新用户密码
     * @param userId
     * @param passwd
     */
    @Update("update miaosha_user set password = #{passwd} where id = #{userId}")
    void updatePassword(@Param("userId") long userId,@Param("passwd") String passwd);

    /**
     * 插入用户
     * @param user
     * @return
     */
    @Insert("insert into miaosha_user(id, name)values(#{id}, #{name})")
    public int insert(MiaoshaUser user);
}
