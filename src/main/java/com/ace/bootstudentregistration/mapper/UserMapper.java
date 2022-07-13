package com.ace.bootstudentregistration.mapper;

import java.util.List;

import javax.websocket.server.PathParam;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ace.bootstudentregistration.model.UserBean;

@Mapper
public interface UserMapper {

    @Select("select count(*) from user where binary `email`=#{email} && binary `password`=#{password}")
    Boolean checkLogin(@PathParam("email") String email, @PathParam("password") String password);

    @Select("select * from user where email=#{email}")
    UserBean selectUserByEmail(@PathParam("email") String email);

    @Select("select * from user where id=#{id}")
    UserBean selectUserById(@PathParam("id") String id);

    @Select("select * from user")
    List<UserBean> selectAllUsers();

    @Select("select count(*) from user where binary `email`=#{email}")
    Boolean checkEmailExists(@PathParam("email") String email);

    @Insert("insert into user values(#{id}, #{email}, #{name}, #{password}, #{userRole})")
    Integer insertUser(UserBean user);

    @Delete("delete from user where id=#{id}")
    void deleteUserById(@PathParam("id") String id);

    @Update("update user set email=#{email}, name=#{name}, password=#{password}, userRole=#{userRole} where id=#{id}")
    void updateUser(UserBean user);

    @Select("select * from user where id like #{id} or name like #{name}")
    List<UserBean> selectUserListByIdOrName(String id, String name);

}
