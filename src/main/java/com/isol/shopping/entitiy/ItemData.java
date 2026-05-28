package com.isol.shopping.entitiy;

import lombok.Data;

@Data
public class ItemData {
	//アイテムID
	private Number itemId;
	
	//アイテム名
	private String itemName;
	
	//アイテムの値段
	private Number itemValue;
	
	//アイテムの購入個数
	private Number itemCount;
	
	//アイテムのジャンル
	private String itemCategory;
	
	//アイテムの説明
	private String itemExplain;
	
	//アイテムの合計購入金額
	private Number itemSum;
	
	//アイテムの購入時間
	private String buyTime;
	
	//アイテムNo
	private Number itemNo;
	
	//アイテムの在庫
	private Number itemStock;
}
