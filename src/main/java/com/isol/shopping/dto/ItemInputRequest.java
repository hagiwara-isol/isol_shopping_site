package com.isol.shopping.dto;

import java.util.List;

import jakarta.validation.constraints.Positive;

import com.isol.shopping.entitiy.ItemData;

import lombok.Data;

@Data
public class ItemInputRequest {
	
	/** 商品のID */
	private Number itemId;
	
	/** 商品複数購入時のID */
	private List<Number> itemIds;
	
	/** 商品リスト */
	private List<ItemData> itemList;
	
	/** 商品の購入個数 個数がマイナスもしくは0個にならないよう　Validated */
	@Positive(message = "個数は1個からでお願いします")
	private Number itemCount;
	
	/** 商品複数購入時の個数 個数がマイナスもしくは0個にならないようValidated */
	private List<@Positive(message = "個数は1個からでお願いします") Number> itemCounts;
	
	/** 商品の名前 */
	private String itemName;
	
	/** 商品の価格 */
	private Number itemValue;

}
