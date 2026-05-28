package com.isol.shopping.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isol.shopping.entitiy.ItemData;
import com.isol.shopping.repository.ItemDataMapper;
import com.isol.shopping.repository.ItemRepository;
import com.isol.shopping.utility.LoggerUtility;

@Service
public class ItemInputServiceImpl implements ItemInputService {

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	private ItemDataMapper itemDataMapper;

	/**
	 * 商品ごとの購入価格を計算するメソッド
	 * 
	 * @param itemData  商品のインスタンス
	 * @param itemCount 商品の購入数
	 * @return 商品インスタンス
	 */
	@Override
	public ItemData itemBuyCalc(ItemData itemData, Number itemCount) {
		// ログを流す
		LoggerUtility.info("itemBuyCalcメソッドが利用されました。");

		// 商品の小計を計算
		int sum = itemData.getItemValue().intValue() * itemCount.intValue();

		Number result = (Number) sum;

		// インスタンスに格納
		itemData.setItemCount(itemCount);
		itemData.setItemSum(result);

		return itemData;
	}

	/**
	 * 商品の購入日、購入Noをmysqlに登録するメソッド
	 * 
	 * @param itemData 購入した商品のアイテムインスタンス
	 * @return アイテムインスタンス
	 */
	@Override
	public ItemData setItemBuy(ItemData itemData) {
		// ログを流す
		LoggerUtility.info("setItemBuyメソッドが使われました。");

		Number itemId = itemData.getItemId();

		// 現在時刻を取得
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String nowDate = now.format(fmt);

		// アイテムインスタンスにセットする
		itemData.setBuyTime(nowDate);
		// mysqlに登録
		itemDataMapper.updateBuyTime(nowDate, itemId);

		// ログを流す
		LoggerUtility.debug("データベース[item_data]に商品ID:{}の購入日{}が登録されました。", 
				itemData.getItemId(), nowDate);

		return itemData;
	}

	/**
	 * 購入した商品すべての合計金額を計算するメソッド
	 * 
	 * @param itemList 商品リスト
	 * @return 合計金額
	 */
	@Override
	public Number allSum(List<ItemData> itemList) {
		LoggerUtility.info("allSumメソッドが使われました");

		// それぞれの小計を足す
		int sum = 0;
		for (ItemData item : itemList) {
			int s = item.getItemSum().intValue();
			sum += s;			
		}
		LoggerUtility.debug("商品すべての合計金額は{}円です。", sum);
		
		return (Number) sum;
	}

	/**
	 * カートの中の商品の小計をそれぞれ計算し、リストに格納して返却するメソッド
	 * 
	 * @param list      購入商品リスト
	 * @param itemCount それぞれの商品の購入個数
	 * @return 商品の小計を含んだ商品リスト
	 */
	@Override
	public List<ItemData> cartBuyCalc(List<ItemData> list, List<Number> itemCounts) {
		// TODO 自動生成されたメソッド・スタブ
		LoggerUtility.info("cartBuyCalcメソッドが使われました");

		// イテレーターを利用する
		Iterator<Number> cList = itemCounts.iterator();

		// それぞれの小計を計算してインスタンスに格納・リストに追加
		List<ItemData> newList = new ArrayList<ItemData>();
		for (ItemData item : list) {
			Number count = cList.next();
			int result = item.getItemValue().intValue() * count.intValue();
			item.setItemSum((Number) result);
			item.setItemCount(count);
			newList.add(item);
			
			LoggerUtility.debug("商品ID:{}の小計は{}円です。", item.getItemId(), result);
		}
		return newList;
	}

	/**
	 * 在庫を確認し、購入個数が在庫を上回るかを判定するメソッド
	 * 
	 * @param item      アイテムインスタンス
	 * @param itemCount 購入個数
	 * @return 購入個数が在庫を上回った場合在庫数を返す<br>
	 *         上回らない場合nullを返す
	 */
	@Override
	public Number stockJudge(Number itemId, Number itemCount) {
		// TODO 自動生成されたメソッド・スタブ
		LoggerUtility.info("stockJudgeメソッドが使われました");

		// 在庫数
		int stock = itemRepository.stockCheck(itemId);

		// 購入個数が在庫数より少なかった場合nullを返す
		if (itemCount.intValue() <= stock) {
			return null;
		} else {
			// 在庫が足りなかった場合在庫数を返す
			return stock;
		}
	}

	/**
	 * 在庫を確認し、カートの中の商品の購入個数が在庫を上回るかを判定するメソッド
	 * 
	 * @param list      アイテムカートの中身
	 * @param itemCount 購入個数
	 * @return カートの中の商品のうち一つでも購入個数が在庫を下回った場合、在庫数を返す<br>
	 *         上回らない場合nullを返す
	 */
	@Override
	public Number cartStockJudge(List<ItemData> list, Number itemC) {
		// TODO 自動生成されたメソッド・スタブ
		LoggerUtility.info("cartStockJudgeメソッドが使われました");

		// 商品リスト内の商品の中で1つでも在庫が足りない商品があった場合、在庫数を返す
		for (ItemData item : list) {
			Number result = stockJudge(item.getItemId(), itemC);
			if (result != null) {
				return result;
			}
		}
		// 全ての商品が購入可能だった場合、nullを返す
		return null;
	}
}
