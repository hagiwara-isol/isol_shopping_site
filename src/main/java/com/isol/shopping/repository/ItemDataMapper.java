package com.isol.shopping.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ItemDataMapper {
	
	//全ての商品をリスト化
	@Select("SELECT * FROM item_data")
	List<Map<String, Object>> selectAllItemData();
	
	//商品IDから1つの商品を返却
	@Select("SELECT * FROM item_data WHERE item_id = #{itemId}")
	Map<String, Object> selectIdItemData(Number itemId);
	
	//商品の購入個数を登録
	@Update("UPDATE item_data SET item_count= #{itemCount} WHERE item_id= #{itemId}")
	void setItemCount(Number itemCount, Number itemId);
	
	//商品の小計を登録
	@Update("UPDATE item_data SET item_sum= #{itemSum} WHERE item_id= #{itemId}")
	void setItemSum(Number itemSum, Number itemId);
	
	//商品の在庫を変更
	@Update("UPDATE item_data SET item_stock= #{itemStock} WHERE item_id= #{itemId}")
	void changeItemStock(Number itemStock, Number itemId);
	
	//商品の購入日を
	@Update("UPDATE item_data SET item_date= #{buyTime} WHERE item_id= #{itemId}")
	void updateBuyTime(String buyTime, Number itemId);
}
