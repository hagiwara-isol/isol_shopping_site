package com.isol.shopping.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.isol.shopping.entitiy.ItemData;
import com.isol.shopping.utility.LoggerUtility;

@Repository
public class HistoryRepository {
	
	@Autowired
	private HistoryMapper historyMapper;
	
	/**mysqlに購入した商品のリストを登録するメソッド
	 *@param itemList 購入した商品リスト
	 */
	public void setItemHistory(List<ItemData> itemList, Number userId) {
		// TODO 自動生成されたメソッド・スタブ
		LoggerUtility.info("データベース[item_history]にアクセスしました");
		
//		String sql = "INSERT INTO item_history (item_name, item_sum, item_date, item_no, user_id) VALUES (?, ?, ?, ?, ?)";
		
		for(ItemData item : itemList) {
		
		historyMapper.insertHistory(item.getItemName(), item.getItemSum(), item.getBuyTime(), item.getItemNo(), userId);
		
		LoggerUtility.debug("商品の名前:{}、小計:{}円、購入日:{}、商品No:{}、購入ユーザーID:{}を登録しました。",
				item.getItemName(), item.getItemSum(), item.getBuyTime(), item.getItemNo(), userId);
		}
		
	}	
	/**購入商品履歴を返却するメソッド　ユーザーによって変える
	 *@return 購入商品リスト
	 */
	public List<ItemData> getItemHistory(Number userId) {
//		// TODO 自動生成されたメソッド・スタブ
//		String sql = "SELECT * FROM item_history WHERE user_id = ?";
//
//		// SQLの実行
//		// 結果をList<Map<Sring, object>>型で返却
//		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, userId);
//		
//		List<ItemData> itemList = new ArrayList<ItemData>();
//		
//		//購入履歴が無い場合nullを返す
//		if(result.isEmpty()) {
//			return null;
//		}
//		//購入履歴がある場合、商品リストを取得する
//		for(Map<String, Object> low : result) {
//				ItemData itemData = new ItemData();
//				itemData.setItemId((Number)low.get("item_id"));
//				itemData.setItemName((String)low.get("item_name"));
//				itemData.setItemSum((Number)low.get("item_sum"));
//				itemData.setBuyTime((String)low.get("item_date"));
//				itemData.setItemNo((Number)low.get("item_no"));
//				itemList.add(itemData);
//		}
		
		List<Map<String, Object>> result = historyMapper.selectHistory(userId);
		
		List<ItemData> itemList = new ArrayList<ItemData>();
		
		//購入履歴が無い場合nullを返す
		if(result.isEmpty()) {
			return null;
		}
		//購入履歴がある場合、商品リストを取得する
		for(Map<String, Object> low : result) {
				ItemData itemData = new ItemData();
				itemData.setItemId((Number)low.get("item_id"));
				itemData.setItemName((String)low.get("item_name"));
				itemData.setItemSum((Number)low.get("item_sum"));
				itemData.setBuyTime((String)low.get("item_date"));
				itemData.setItemNo((Number)low.get("item_no"));
				itemList.add(itemData);
		}
		
		
		return itemList;
	}
}