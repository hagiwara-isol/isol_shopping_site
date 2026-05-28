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
import com.isol.shopping.service.ItemInputService;
import com.isol.shopping.utility.LoggerUtility;

@Controller
public class DoneController {

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private ItemInputService itemInputService;
	/**
	 * 画面表示
	 * 
	 * @param itemData
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/shop/done")
	public String display(HttpSession session, Model model) {
		//セッションからユーザー情報を取得・反映
		UserData userData = (UserData) session.getAttribute("loginUser");
		model.addAttribute("userData", userData);
		
		LoggerUtility.info("商品購入完了ページに飛びました。");
		
		@SuppressWarnings("unchecked")
		List<ItemData> list = (List<ItemData>) model.getAttribute("itemList");
		
		//mysqlに商品の購入情報を登録
		historyRepository.setItemHistory(list, userData.getUserId());
		
		LoggerUtility.info("データベース[item_history]にアクセスしました。");

		// 複数購入時の合計を計算する
		Number sum = itemInputService.allSum(list);
		//反映
		model.addAttribute("itemSum", sum);

		model.addAttribute("itemList", list);

		return "shop/done";
	}

}
