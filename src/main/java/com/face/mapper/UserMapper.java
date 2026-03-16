package com.face.mapper;

import com.face.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    
    @Insert("INSERT INTO sys_user(username, password, role, create_time) VALUES(#{username}, #{password}, #{role}, NOW())")
    int insert(User user);

    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    User findByUsername(String username);
}
