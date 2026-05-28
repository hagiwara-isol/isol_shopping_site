package com.isol.shopping.entitiy;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserData {
	
	/** ユーザーID　*/
	private Number userId;

	/** ログインID */
	private String loginId;
	
	/** パスワード */
	private String password;

	/** ユーザ名 */
	private String userName;
	
	/** 住所 */
	private String address;
	
	/** 電話番号　*/
	private String phoneNumber;
	
	/** 更新日時 */
	private LocalDateTime updateDate;
	
	/** 登録日時 */
	private LocalDateTime createDate;

	public UserData() {
	}
}