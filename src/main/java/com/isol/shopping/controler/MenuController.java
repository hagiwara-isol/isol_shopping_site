package com.isol.shopping.controler;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.isol.shopping.dto.LoginRequest;
import com.isol.shopping.entitiy.UserData;
import com.isol.shopping.utility.LoggerUtility;

@Controller
public class MenuController {
		
	@GetMapping(value = "/shop/menu")
	public String display(@ModelAttribute LoginRequest loginRequest, HttpSession session, Model model) {
		LoggerUtility.info("メニュー画面に飛びました。");

		UserData userData = (UserData) session.getAttribute("loginUser");
		model.addAttribute("userData", userData);
	
		return "shop/menu";
	}

}