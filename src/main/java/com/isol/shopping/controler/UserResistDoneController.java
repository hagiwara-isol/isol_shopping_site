package com.isol.shopping.controler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.isol.shopping.entitiy.UserData;
import com.isol.shopping.utility.LoggerUtility;

@Controller
public class UserResistDoneController {

	/**
	 * 画面を表示するメソッド
	 * 
	 * @param model
	 * @param userData
	 * @return
	 */
	@GetMapping(value = "/shop/userResistDone")
	public String display(Model model, @ModelAttribute("userData") UserData userData) {
		LoggerUtility.info("ユーザー登録完了ページに飛びました。");
		
		model.addAttribute("userData", userData);

		return "shop/userResistDone"; // HTMLを返す
	}

}
