package com.isol.shopping.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isol.shopping.entitiy.UserData;
import com.isol.shopping.repository.UserMapper;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private UserMapper userMapper;
	
	//ログを作る
	private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	/**ログインIDとパスワードから、すでに登録されたユーザーに該当するか判定するメソッド
	 * @param loginId ログインID
	 * @param password パスワード
	 *@return ユーザーインスタンス 該当しなかった場合null
	 */
	@Override
	public UserData loginJudge(String loginId, String password) {
		
		//使用ログ
		logger.info("loginJudgeを使用しました");

		// SQLの実行
		// 結果をMap<Sring, object>型で返却
		Map<String, Object> result = userMapper.selectUserData(loginId, password);

		// なかった場合
		if (result == null) {
			return null;
		}

		// 検索の一行目を取り出してUserDataインスタンスに格納する

		UserData user = new UserData();

		user.setUserId((Number) result.get("user_id"));
		user.setLoginId((String) result.get("login_id"));
		user.setPassword((String) result.get("password"));
		user.setUserName((String) result.get("user_name"));
		user.setAddress((String) result.get("address"));
		user.setPhoneNumber((String) result.get("phone_number"));
		user.setUpdateDate((LocalDateTime) result.get("update_date"));
		user.setCreateDate((LocalDateTime) result.get("create_date"));
		
		return user;
	}


}
