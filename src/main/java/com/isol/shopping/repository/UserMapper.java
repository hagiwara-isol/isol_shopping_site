package com.isol.shopping.repository;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
	
	@Select("SELECT * FROM user_data WHERE login_id = #{loginId} AND password = #{password}")
	Map<String, Object> selectUserData(String loginId, String password);
	
	@Insert("INSERT INTO user_data (login_id, user_name, password) VALUES (#{loginId}, #{userName}, #{password})")
	void insertUserData(String loginId, String userName, String password);
}
