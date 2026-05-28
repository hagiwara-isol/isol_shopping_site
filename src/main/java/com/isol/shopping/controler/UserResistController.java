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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.isol.shopping.dto.LoginRequest;
import com.isol.shopping.dto.UserResist;
import com.isol.shopping.entitiy.UserData;
import com.isol.shopping.repository.UserRepository;
import com.isol.shopping.service.LoginService;
import com.isol.shopping.utility.LoggerUtility;

@Controller
public class UserResistController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private UserRepository userRepository;

	String error = "すでに登録されています";

	/**
	 * 画面表示
	 * 
	 * @param model 処理の内容
	 * @return 表示する画面
	 */
	@GetMapping(value = "/shop/userResist")
	public String display(Model model, @ModelAttribute LoginRequest loginRequest) {
		LoggerUtility.info("ユーザー登録画面に飛びました。");
		return "shop/userResist"; // HTMLを返す
	}

	/**
	 * ログイン情報があっているか判定して次のページに飛ばす
	 * 
	 * @param model
	 * @param loginRequest
	 * @return メニュー画面
	 */
	@PostMapping(value = "/shop/userResist")
	public String login(Model model, @Validated(UserResist.class) @ModelAttribute LoginRequest loginRequest, BindingResult bindingResult, HttpSession session,
			RedirectAttributes redirectAttributes) {
		LoggerUtility.info("POST先の処理を開始します。");
		
		//入力値のチェック　loginId,passwordがそれぞれ6文字以上15文字以下
		if(bindingResult.hasErrors()) {
			
			//入力値が不正だった理由
			String errorMessage = "";
			for(ObjectError oe : bindingResult.getAllErrors()) {
				errorMessage += oe.getDefaultMessage();
			}
			LoggerUtility.warn(errorMessage);
			
			LoggerUtility.warn("入力値が不正でした。ユーザー登録ページに飛ばします。");
			return "shop/userResist";
		}	
		
		//すでに登録されているか確かめる
		UserData userJudge = loginService.loginJudge(loginRequest.getLoginId(), loginRequest.getPassword());

		// すでに登録されていた場合登録しない
		if (userJudge != null) {
			//メッセージを表示
			LoggerUtility.warn("loginId,passwordが両方登録済みでした。");
			model.addAttribute("message", error);
			return "shop/userResist";
		} else {
			//mysqlにユーザーの情報を登録する
			UserData user = userRepository.resistUser(loginRequest.getLoginId(), loginRequest.getUserName(),
					loginRequest.getPassword());
			LoggerUtility.info("ユーザー登録が完了しました。ユーザー登録完了ページに飛ばします。");
			//次のページに情報を渡す
			redirectAttributes.addFlashAttribute("userData", user);
			//セッションに保存する
			session.setAttribute("loginUser", user);
			//ユーザー登録完了ページに飛ぶ
			return "redirect:/shop/userResistDone";
		}
	}
}
