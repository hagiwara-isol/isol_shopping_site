package com.isol.shopping.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class LoginRequest {

	/** ログインID 文字数確認は登録画面のみ*/
	@NotBlank(message = "ログインIDは必須です", groups = {UserLogin.class, UserResist.class})
	@Size(min = 6, max = 20, message = "ログインIDは6～20文字を入力してください", groups = {UserResist.class})
	private String loginId;
	
	/** パスワード 文字数確認は登録画面のみ*/
	@NotBlank(message = "パスワードは必須です", groups = {UserLogin.class, UserResist.class})
	@Size(min = 6, max = 20, message = "パスワードは6～20文字を入力してください", groups = {UserResist.class})
	private String password;
	
	/** ユーザー名　登録画面のみ確認*/
	@NotBlank(message = "ユーザー名は必須です", groups = {UserResist.class})
	private String userName;
	
	public LoginRequest() {
	}
}