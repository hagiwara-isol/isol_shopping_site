package com.isol.shopping.service;

import com.isol.shopping.entitiy.UserData;

public interface LoginService {
	/**ログインIDとパスワードから、すでに登録されたユーザーに該当するか判定するメソッド
	 * @param loginId ログインID
	 * @param password パスワード
	 *@return ユーザーインスタンス 該当しなかった場合null
	 */
	UserData loginJudge(String loginId, String password);
	}