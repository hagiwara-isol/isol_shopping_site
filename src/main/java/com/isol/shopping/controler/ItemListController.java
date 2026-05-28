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
import com.isol.shopping.utility.LoggerUtility;

@Controller
public class ItemListController {

	@Autowired
	ItemRepository itemRepository;

	/**
	 * 画面表示
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/shop/itemList")
	public String display(HttpSession session, Model model) {

		LoggerUtility.info("商品リストページに飛びました。");

		// セッションからユーザー情報を取得・反映させる
		UserData userData = (UserData) session.getAttribute("loginUser");
		model.addAttribute("userData", userData);

		List<ItemData> itemList = itemRepository.getItemList();
		
		

		model.addAttribute("itemList", itemList);
		return "shop/itemList"; // HTMLを返す
	}

	/**
	 * チェックボックスのチェック項目を取得して商品カートページに飛ばすメソッド
	 * 
	 * @param model
	 * @param itemInputRequest   商品IDを格納する
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping(value = "/shop/itemList")
	public String checkbox(Model model, @ModelAttribute ItemInputRequest itemInputRequest,
			RedirectAttributes redirectAttributes) {
		LoggerUtility.info("POST先の処理を開始します。");

		// POSTされた商品IDを取得
		List<Number> itemIds = itemInputRequest.getItemIds();

		// チェックボックスにチェックが1つもつけられていない場合nullになる
		if (itemIds == null) {
			redirectAttributes.addFlashAttribute("message", "購入したい商品をチェックしてください");
			// ログを流す
			LoggerUtility.warn("チェックボックスにチェックがつけられていなかったため、redirect処理がなされました。");
			// 商品リストのページに再度飛ばす
			return "redirect:/shop/itemList";
		} else {
			// チェックされていた商品をリストに格納する
			List<ItemData> itemList = new ArrayList<ItemData>();
			for (Number id : itemIds) {
				ItemData itemData = itemRepository.getItemData(id);
				itemList.add(itemData);
				// ログを流す
				LoggerUtility.debug("商品ID:{}を追加しました。", id);
			}
			// 次のページに情報を渡す
			redirectAttributes.addFlashAttribute("itemList", itemList);
			// 商品カートのページに飛ばす
			return "redirect:/shop/itemInputCart";
		}
	}
}
