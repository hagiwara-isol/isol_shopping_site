package com.isol.shopping.controler;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
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
public class MinusBuyController {

	@Autowired
	private ItemInputService itemInputService;

	@Autowired
	private ItemRepository itemRepository;

		
	//エラーメッセージ
	private String message = null;
	
	@GetMapping(value = "/shop/minusBuy")
	public String display(Model model, HttpSession session, @ModelAttribute ItemInputRequest itemInputRequest) {
		UserData userData = (UserData) session.getAttribute("loginUser");
		model.addAttribute("userData", userData);
		
		LoggerUtility.info("商品マイナスページに飛びました。");
		return "shop/minusBuy"; // HTMLを返す
	}

	/**
	 * 商品の購入個数を格納して購入確認画面に飛ばすメソッド
	 * 
	 * @param model
	 * @param itemInputRequest 購入個数を記録しているクラス
	 * @param session
	 * @return 購入確認画面
	 */
	@PostMapping(value = "/shop/minusBuy")
	public String submit(Model model, @Validated @ModelAttribute ItemInputRequest itemInputRequest, BindingResult bindingResult, 
			RedirectAttributes redirectAttributes) {
		LoggerUtility.info("POST先に飛びました。");
		
		//入力された購入個数・該当する商品IDを取得
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

			ItemData item = itemRepository.getItemData(id);			
			//リダイレクト先に情報を渡す
			redirectAttributes.addFlashAttribute("item", item);
			
			//メッセージを表示
			model.addAttribute("message", errorMessage);
			redirectAttributes.addFlashAttribute("message", errorMessage);

			return "redirect:/shop/minusBuy";
		}

		// 在庫チェック
		Number stock = itemInputService.stockJudge(id, count);

		// nullだった場合購入可能
		if (stock == null) {
			List<ItemData> list = new ArrayList<ItemData>();
			//リストに格納
			ItemData item = itemRepository.getItemData(id);
			ItemData itemSum = itemInputService.itemBuyCalc(item, count);
			itemRepository.setItemBuy(itemSum, count, itemSum.getItemSum());
			list.add(itemSum);
			//ログを流す
			LoggerUtility.info("商品ID:{}が{}個購入可能", id, count);
			//次のページに情報を渡す
			redirectAttributes.addFlashAttribute("itemList", list);
			//商品確認画面に飛ばす
			return "redirect:/shop/itemConfirm";
		} else {
			// 在庫が足りなかった場合
			ItemData item = itemRepository.getItemData(id);
			item.setItemStock(stock);
			item.setItemCount(count);
			
			//ログを流す
			LoggerUtility.warn("商品ID:{}が在庫不足のため購入不可", id);
			
			//メッセージを表示
			message = stock.toString() + "個までの購入でお願いします。";
			model.addAttribute("message", message);
			redirectAttributes.addFlashAttribute("message", message);
			redirectAttributes.addFlashAttribute("item", item);
			//再度商品品切れ画面を表示
			return "redirect:/shop/minusBuy";
		}
	}
}
