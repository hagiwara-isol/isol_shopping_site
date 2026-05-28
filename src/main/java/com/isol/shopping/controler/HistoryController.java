package com.isol.shopping.controler;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.isol.shopping.entitiy.ItemData;
import com.isol.shopping.entitiy.UserData;
import com.isol.shopping.repository.HistoryRepository;
import com.isol.shopping.utility.LoggerUtility;

@Controller
public class HistoryController {
	
	@Autowired
	private HistoryRepository historyRepository;
	
	/** 画面表示
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/shop/history")
	public String display(HttpSession session, Model model) {
		LoggerUtility.info("商品履歴ページに飛びました。");
		
		//セッションからユーザー情報取得・反映
		UserData userData = (UserData) session.getAttribute("loginUser");
		model.addAttribute("userData", userData);
		
		//ユーザーによって表示する商品リストを変える
		List<ItemData> itemList = historyRepository.getItemHistory(userData.getUserId());
		LoggerUtility.info("データベースにアクセスしてユーザーID:{}の過去の購入履歴を検索しました。", userData.getUserId());
		
		//購入履歴が無い場合、メッセージを表示する
		model.addAttribute("itemList", itemList);
		if(itemList == null) {
			LoggerUtility.info("ユーザーID:{}の過去の購入履歴はありませんでした。", userData.getUserId());
			model.addAttribute("message", "過去に購入された商品はありません");
		} else {
			model.addAttribute("message", null);
		}
		return "shop/history";
	}
}
