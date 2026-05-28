package com.isol.shopping.controler;

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

import com.isol.shopping.dto.LoginRequest;
import com.isol.shopping.dto.UserLogin;
import com.isol.shopping.entitiy.UserData;
import com.isol.shopping.service.LoginService;
import com.isol.shopping.utility.LoggerUtility;

@Controller
public class LoginController {

	@Autowired
	private LoginService loginService;

	final String error = "ログインIDまたはパスワードが間違っています";

	/**
	 * 画面表示
	 * 
	 * @param model 処理の内容
	 * @return 表示する画面
	 */
	@GetMapping(value = "/shop/login")
	public String display(Model model, @ModelAttribute LoginRequest loginRequest) {
		LoggerUtility.info("ログインページに飛びました。");
		return "shop/login"; // HTMLを返す
	}

	/**
	 * ログイン情報があっているか判定して次のページに飛ばす
	 * 
	 * @param model
	 * @param loginRequest
	 * @return メニュー画面
	 */
	@PostMapping(value = "/shop/login")
	public String login(Model model, @Validated(UserLogin.class) @ModelAttribute LoginRequest loginRequest, BindingResult bindingResult,
			HttpSession session) {
		LoggerUtility.info("POST先の処理を開始します。");

		if (bindingResult.hasErrors()) {
			//入力値が不正だった理由
			String error = "";
			for(ObjectError oe : bindingResult.getAllErrors()) {
				error += oe.getDefaultMessage();
			}
			LoggerUtility.warn(error);
			
			LoggerUtility.warn("入力値が不正でした。ログイン画面に飛ばします。");
			return "shop/login";
		}

		// ログイン情報をmysqlに照らし合わせて判定
		UserData user = loginService.loginJudge(loginRequest.getLoginId(), loginRequest.getPassword());

		// nullの場合合っている
		if (user != null) {
			LoggerUtility.info("ログイン処理がなされました");
			// sessionにセットする
			session.setAttribute("loginUser", user);
			// メニュー画面に飛ばす
			return "redirect:/shop/menu";
		} else {
			LoggerUtility.warn("ログイン情報が間違っていました。");
			// 間違っていた場合メッセージを表示
			model.addAttribute("message", error);
			// 再度ログイン画面へ
			return "shop/login";
		}
	}
}
