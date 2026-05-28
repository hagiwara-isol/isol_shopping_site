package com.isol.shopping.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.isol.shopping.entitiy.UserData;

@Repository
public class UserRepository {
	
	@Autowired
	private UserMapper userMapper;
	
	/**新規ユーザー登録を行うメソッド
	 *@param loginId ログインID
	 *@param userName ユーザー名
	 *@param password パスワード
	 *@return ユーザーインスタンス
	 */
	public UserData resistUser(String loginId, String userName, String password) {
		// TODO 自動生成されたメソッド・スタブ
		userMapper.insertUserData(loginId, userName, password);

		UserData user = new UserData();
		user.setLoginId(loginId);
		user.setUserName(userName);
		user.setPassword(password);

		return user;
	}
}
