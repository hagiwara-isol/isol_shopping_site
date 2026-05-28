package com.isol.shopping.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.isol.shopping.entitiy.ItemData;
import com.isol.shopping.utility.LoggerUtility;

@Repository
public class ItemRepository {

	@Autowired
	private ItemDataMapper itemDataMapper;

	/**
	 * 商品リストを取得するメソッド
	 * 
	 * @return 全ての商品リスト
	 */
	public List<ItemData> getItemList() {
		
		List<Map<String, Object>> result = itemDataMapper.selectAllItemData();
		
		LoggerUtility.info("商品データベースにアクセスしました。");
		List<ItemData> itemList = new ArrayList<ItemData>();
		for (Map<String, Object> low : result) {
			Number stock = (Number) low.get("item_stock");
			if (stock.intValue() == 0) {
				continue;
			} else {
				ItemData item = new ItemData();
				item.setItemId((Number) low.get("item_id"));
				item.setItemName((String) low.get("item_name"));
				item.setItemCategory((String) low.get("item_category"));
				item.setItemValue((Number) low.get("item_value"));
				item.setItemExplain((String) low.get("item_explain"));
				item.setItemStock((Number) low.get("item_stock"));
				item.setItemNo((Number) low.get("item_no"));
				item.setItemSum((Number) low.get("item_sum"));
				item.setBuyTime((String) low.get("item_date"));
				itemList.add(item);
			}
		}
		
		if(result.isEmpty()) {
			LoggerUtility.warn("現在販売できる商品はありません。");
		}
		
//		
		return itemList;
	}

	/**
	 * 商品のIDから商品インスタンスを取得するメソッド
	 * 
	 * @param itemId
	 * @return 商品インスタンス
	 */
	public ItemData getItemData(Number itemId) {
		Map<String, Object> low = itemDataMapper.selectIdItemData(itemId);
		LoggerUtility.info("商品データベースにアクセスしました。");
		ItemData item = new ItemData();

		item.setItemId((Number) low.get("item_id"));
		item.setItemName((String) low.get("item_name"));
		item.setItemCategory((String) low.get("item_category"));
		item.setItemValue((Number) low.get("item_value"));
		item.setItemExplain((String) low.get("item_explain"));
		item.setItemStock((Number) low.get("item_stock"));
		item.setItemNo((Number) low.get("item_no"));
		item.setItemSum((Number) low.get("item_sum"));
		item.setBuyTime((String) low.get("item_date"));

//		ItemData item = itemDataMapper.selectIdItemData(itemId);
		
		if(low.isEmpty()) {
			LoggerUtility.error("商品ID{}の情報を取得することができませんでした。", itemId);
		}
		
		return item;
	}

	/**
	 * 商品の小計と購入個数を登録するメソッド
	 * 
	 * @param item
	 * @param itemCount
	 * @param itemSum
	 */
	public void setItemBuy(ItemData item, Number itemCount, Number itemSum) {
		// mysqlのアイテムデータの情報をアップデート
		// 購入個数
		itemDataMapper.setItemCount(itemCount, item.getItemId());
		// 小計
		itemDataMapper.setItemSum(itemSum, item.getItemId());
		LoggerUtility.info("商品データベースにアクセスしました。");
	}

	/**
	 * 商品の小計と購入個数を登録するメソッド
	 * 
	 * @param item
	 * @param itemCount
	 * @param itemSum
	 */
	public void setCartBuy(List<ItemData> itemList, List<Number> itemCounts) {

		// 商品ごとにアップデート
		Iterator<Number> cList = itemCounts.iterator();
		for (ItemData item : itemList) {
			Number count = cList.next();
			// 購入個数
			itemDataMapper.setItemCount(count, item.getItemId());
			// 小計
			itemDataMapper.setItemSum(item.getItemSum(), item.getItemId());
			LoggerUtility.info("商品データベースにアクセスしました。");
			
			LoggerUtility.debug("商品ID:{}の小計{}円,購入個数{}個を登録しました。", item.getItemSum(), count);
		}
	}

	/**
	 * アイテムの在庫を確認するメソッド
	 * 
	 * @param itemId アイテムID
	 * @return 在庫 int型
	 */
	public int stockCheck(Number itemId) {
		// 商品IDからアイテムインスタンスを取得
		Map<String, Object> result = itemDataMapper.selectIdItemData(itemId);

		LoggerUtility.info("商品データベースにアクセスしました。");

		Number stock = (Number) result.get("item_stock");
		// int型で返却
		return stock.intValue();
	}

	/**
	 * 商品の在庫を減らすメソッド
	 * 
	 * @param itemId
	 * @param itemCount
	 */
	public void minusStock(Number itemId, Number itemCount) {
		int stock = stockCheck(itemId);
		// 在庫から購入個数を引く
		int result = stock - itemCount.intValue();
		Number s = (Number) result;

		// 引いた数を在庫として登録・アップデートする
		itemDataMapper.changeItemStock(s, itemId);
		LoggerUtility.info("商品データベースにアクセスしました。");

		// 差し引いた後の在庫確認
		// 在庫数がマイナスになった場合エラーのログを流す
		if (result < 0) {
			LoggerUtility.error("商品ID:{}の残り在庫数がマイナスです。", itemId);
		}
	}

}
