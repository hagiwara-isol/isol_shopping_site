package com.isol.shopping.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HistoryMapper {

	@Insert("INSERT INTO item_history (item_name, item_sum, item_date, item_no, user_id) VALUES(#{itemName}, #{itemSum}, #{buyTime}, #{itemNo}, #{userId})")
	void insertHistory(String itemName, Number itemSum, String buyTime, Number itemNo, Number userId);
	
	@Select("SELECT * FROM item_history WHERE user_id = #{userId} ORDER BY item_id DESC")
	List<Map<String, Object>> selectHistory(Number userId);
}
