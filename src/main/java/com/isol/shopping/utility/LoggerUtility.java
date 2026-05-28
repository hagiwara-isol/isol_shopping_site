package com.isol.shopping.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.isol.shopping.service.LoginServiceImpl;

public class LoggerUtility {
	// ログを作る
	private static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	public static void info (String format, Object ...obj ) {
		logger.info(format, obj);
	}
	
	public static void debug(String format, Object ...obj) {
		logger.debug(format, obj);
	}
	
	public static void error(String format, Object ...obj) {
		logger.error(format, obj);
	}
	
	public static void warn(String format, Object ...obj) {
		logger.warn(format, obj);
	}
}
