package com.isol.shopping.service;

import java.util.List;

import com.isol.shopping.entitiy.ItemData;

public interface ItemInputService {
	
	
	/**
	 * 商品ごとの購入価格を計算するメソッド
	 * 
	 * @param itemData  商品のインスタンス
	 * @param itemCount 商品の購入数
	 * @return 商品インスタンス
	 */
	ItemData itemBuyCalc(ItemData itemdata, Number itemCount);
	
	/**
	 * 商品の購入日、購入Noをmysqlに登録するメソッド
	 * 
	 * @param itemData 購入した商品のアイテムインスタンス
	 * @return アイテムインスタンス
	 */
	ItemData setItemBuy(ItemData itemData);
	
	/**購入した商品すべての合計金額を計算するメソッド
	 *@param itemList 商品リスト
	 *@return 合計金額
	 */
	Number allSum(List<ItemData> itemList);
	
	/**カートの中の商品の小計をそれぞれ計算し、リストに格納して返却するメソッド
	 *@param list 購入商品リスト
	 *@param itemCounts それぞれの商品の購入個数
	 *@return 商品の小計を含んだ商品リスト
	 */
	List<ItemData> cartBuyCalc(List<ItemData> list, List<Number> itemCounts);
	
	/**
	 * 在庫を確認し、購入個数が在庫を上回るかを判定するメソッド
	 * 
	 * @param itemId 商品ID
	 * @param itemCount 購入個数
	 * @return 購入個数が在庫を上回った場合在庫数を返す<br>上回らない場合nullを返す
	 */
	Number stockJudge(Number itemId, Number itemCount);
	
	/**在庫を確認し、カートの中の商品の購入個数が在庫を上回るかを判定するメソッド
	 *@param list アイテムカートの中身
	 *@param itemCount 購入個数
	 *@return カートの中の商品のうち一つでも購入個数が在庫を下回った場合、在庫数を返す<br>上回らない場合nullを返す
	 */
	Number cartStockJudge(List<ItemData> list, Number itemCount);
}
