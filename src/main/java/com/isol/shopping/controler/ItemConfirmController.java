package com.isol.shopping.controler;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.isol.shopping.dto.ItemInputRequest;
import com.isol.shopping.entitiy.ItemData;
import com.isol.shopping.entitiy.UserData;
import com.isol.shopping.repository.ItemRepository;
import com.isol.shopping.service.ItemInputService;
import com.isol.shopping.utility.LoggerUtility;

@Controller
public class ItemConfirmController {
	@Autowired
	private ItemInputService itemInputService;

	@Autowired
	private ItemRepository itemRepository;

	/**
	 * 画面表示
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/shop/itemConfirm")
	public String display(@ModelAttribute("itemList") List<ItemData> itemList, HttpSession session, Model model) {
		
		LoggerUtility.info("商品確認画面に飛びました。");

		// セッションからユーザー情報取得・反映
		UserData userData = (UserData) session.getAttribute("loginUser");
		model.addAttribute("userData", userData);

		// 合計の金額を取得
		Number sum = itemInputService.allSum(itemList);
		// ログを流す
		LoggerUtility.info("合計金額:{}円", sum);
		// 反映
		model.addAttribute("itemSum", sum);

		return "shop/itemConfirm";
	}

	/**
	 * 次のページに値を渡すメソッド
	 * 
	 * @param model
	 * @param itemInputRequest
	 * @param session
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping(value = "/shop/itemConfirm")
	public String submit(Model model, @ModelAttribute ItemInputRequest itemInputRequest,
			RedirectAttributes redirectAttributes) {
		LoggerUtility.info("POST先の処理を開始します。");
		
		// POSTされた情報から購入商品リストを取得
		List<ItemData> itemList = itemInputRequest.getItemList();

		List<ItemData> newList = new ArrayList<ItemData>();

		// mysqlに購入個数、購入日、購入金額を書く
		for (ItemData item : itemList) {
			ItemData itemData = itemInputService.setItemBuy(item);
			// 購入個数、購入日、購入金額をセットされた商品インスタンスをリストに格納
			newList.add(itemData);
			
			// 在庫から購入個数を引く
			itemRepository.minusStock(item.getItemId(), item.getItemCount());
			// ログを流す
			LoggerUtility.info("データベース[item_data]の商品ID:{}の在庫が{}個引かれました", item.getItemId(), item.getItemCount());
			
		}

		// 次のページに情報を渡す
		redirectAttributes.addFlashAttribute("itemList", newList);
		// 購入完了ページに飛ばす
		return "redirect:/shop/done";
	}
}
