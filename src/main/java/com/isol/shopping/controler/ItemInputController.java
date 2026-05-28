package com.isol.shopping.controler;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.isol.shopping.dto.ItemInputRequest;
import com.isol.shopping.entitiy.ItemData;
import com.isol.shopping.entitiy.UserData;
import com.isol.shopping.repository.ItemRepository;
import com.isol.shopping.service.ItemInputService;
import com.isol.shopping.utility.LoggerUtility;

@Controller
public class ItemInputController {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	private ItemInputService itemInputService;

	@Autowired
	private ItemRepository itemRepository;

	/**
	 * 画面表示 単品で購入時
	 * 
	 * @param itemId
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/shop/itemInput")
	public String display(@RequestParam(value = "itemid") String itemid, HttpSession session, Model model,
			@ModelAttribute ItemInputRequest itemInputRequest) {
		LoggerUtility.info("商品購入ページに飛びました。");

		UserData userData = (UserData) session.getAttribute("loginUser");
		model.addAttribute("userData", userData);

		Number itemId = (Number) Integer.parseInt(itemid);

		ItemData itemData = itemRepository.getItemData(itemId);

		model.addAttribute("item", itemData);
		return "shop/itemInput";
	}

	/**
	 * 商品の購入個数を格納して購入確認画面に飛ばすメソッド
	 * 
	 * @param model
	 * @param itemInputRequest 購入個数を記録しているクラス
	 * @param session
	 * @return 購入確認画面
	 */
	@PostMapping(value = "/shop/itemInput")
	public String submit(Model model, @Validated @ModelAttribute ItemInputRequest itemInputRequest,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		LoggerUtility.info("POST先の処理を開始します。");

		// POSTから情報を取得
		Number count = itemInputRequest.getItemCount();
		Number id = itemInputRequest.getItemId();
		
		//Validatedの処理 購入個数がゼロまたはマイナスになった場合
		if (bindingResult.hasErrors()) {
			// 入力値が不正だった理由を取得
			String errorMessage = "";
			for (ObjectError oe : bindingResult.getAllErrors()) {
				errorMessage += oe.getDefaultMessage();
			}
			//ログに流す
			LoggerUtility.warn(errorMessage);
			LoggerUtility.warn("入力値が不正でした。商品マイナスページに飛ばします。");

			// 商品IDから商品の情報を取得する
			ItemData item = itemRepository.getItemData(id);

			
			//リダイレクト先に情報を渡す
			redirectAttributes.addFlashAttribute("item", item);

			return "redirect:/shop/minusBuy";
		}

		// 在庫チェック
		Number stock = itemInputService.stockJudge(id, count);

		// nullだった場合購入可能
		if (stock == null) {
			// 商品IDから商品の情報を取得し、リストに格納
			List<ItemData> list = new ArrayList<ItemData>();

			ItemData item = itemRepository.getItemData(id);
			ItemData itemSum = itemInputService.itemBuyCalc(item, count);
			itemRepository.setItemBuy(itemSum, count, itemSum.getItemSum());
			list.add(itemSum);

			// ログを流す
			LoggerUtility.debug("商品ID:{}が{}個購入可能", id, count);

			// 次のページに情報を渡す
			redirectAttributes.addFlashAttribute("itemList", list);
			// 確認画面に飛ばす
			return "redirect:/shop/itemConfirm";
		} else {
			// 在庫が足りなかった場合
			// 商品インスタンスに在庫数を格納
			ItemData item = itemRepository.getItemData(id);
			item.setItemStock(stock);

			// ログを流す
			LoggerUtility.debug("商品ID:{}が在庫不足", id);

			// 次のページに情報を渡す
			redirectAttributes.addFlashAttribute("item", item);
			// 品切れページに飛ばす
			return "redirect:/shop/outStock";
		}
	}

}
