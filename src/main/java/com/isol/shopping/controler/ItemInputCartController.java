package com.isol.shopping.controler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
public class ItemInputCartController {

	@Autowired
	private ItemInputService itemInputService;

	@Autowired
	private ItemRepository itemRepository;

	final String error = "購入する個数を入力してください";

	/**
	 * 画面表示 複数で購入時
	 * 
	 * @param itemId
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/shop/itemInputCart")
	public String display(HttpSession session, Model model, @ModelAttribute ItemInputRequest itemInputRequest) {
		LoggerUtility.info("商品カートページに飛びました。");

		UserData userData = (UserData) session.getAttribute("loginUser");
		model.addAttribute("userData", userData);

		return "shop/itemInputCart";
	}

	/**
	 * 商品の購入個数を格納して購入確認画面に飛ばすメソッド
	 * 
	 * @param model
	 * @param itemInputRequest 購入個数を記録しているクラス
	 * @param session
	 * @return 購入確認画面
	 */
	@PostMapping(value = "/shop/itemInputCart")
	public String submit(Model model, @Validated @ModelAttribute ItemInputRequest itemInputRequest,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		LoggerUtility.info("POST先の処理を開始します。");

		// POSTされた商品の情報を受け取る
		List<ItemData> itemlist = itemInputRequest.getItemList();
		List<Number> itemCounts = itemInputRequest.getItemCounts();

		// Validatedの処理 購入個数がゼロまたはマイナスになった場合
		if (bindingResult.hasErrors()) {
			// ログを流す
			LoggerUtility.warn("個数がマイナスまたはゼロでした。再度商品カートページに飛ばします。");

			redirectAttributes.addFlashAttribute("message", "個数は1個からでお願いします。");
			redirectAttributes.addFlashAttribute("itemList", itemlist);

			return "redirect:/shop/itemInputCart";
		}

		// イテレーター作成
		Iterator<Number> cList = itemCounts.iterator();

		// 足りなかった場合ここに格納
		List<Number> minusIdList = new ArrayList<Number>();

		// 商品購入数が在庫より多いかを調べる
		for (ItemData item : itemlist) {
			Number count = cList.next();
			Number s = itemInputService.stockJudge(item.getItemId(), count);
			// null=在庫が足りないとき
			if (s != null) {
				minusIdList.add(item.getItemId());

				// ログを流す
				LoggerUtility.warn("商品ID{}の在庫が足りないため購入できません", count);
			}
		}
		// 在庫が全て足りた場合
		if (minusIdList.isEmpty()) {
			// 小計の計算、データの上書き・格納
			List<ItemData> list = itemInputService.cartBuyCalc(itemlist, itemCounts);
			itemRepository.setCartBuy(list, itemCounts);

			// ログを流す
			LoggerUtility.info("商品の在庫が全て足りました");

			// 次のページに情報を渡す
			redirectAttributes.addFlashAttribute("itemList", list);
			// 確認ページに飛ばす
			return "redirect:/shop/itemConfirm";

			// どれか1つでも足りない場合
		} else {
			// メッセージを作成
			StringBuilder sb = new StringBuilder();
			for (Number id : minusIdList) {
				ItemData item = itemRepository.getItemData(id);
				sb.append("申し訳ありません。[");
				sb.append(item.getItemName());
				sb.append("]は在庫の都合上、");
				sb.append(item.getItemStock().toString());
				sb.append("個までの購入でお願いします。");
				sb.append("\r\n");
				// メッセージを反映させる
				redirectAttributes.addFlashAttribute("message", sb.toString());
				model.addAttribute("message", sb.toString());
				redirectAttributes.addFlashAttribute("itemList", itemlist);
			}
			LoggerUtility.warn("商品の在庫が足りないため再度商品カートページに飛ばします。");
			
			// 再度商品カートを表示
			return "redirect:/shop/itemInputCart";
		}
	}

}
