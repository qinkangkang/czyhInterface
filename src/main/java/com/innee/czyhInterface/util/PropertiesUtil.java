package com.innee.czyhInterface.util;

import org.springside.modules.utils.PropertiesLoader;

/**
 * @name PropertiesUtil
 * @title 操作属性文件工具类
 * @desc
 * @author
 * @version
 */
public class PropertiesUtil {

	private static PropertiesLoader propertiesLoader;

	static {
		propertiesLoader = new PropertiesLoader(Constant.SYSTEM_SETTING_FILE);
	}

	// 获取key所对应的值
	public static String getProperty(String key) {
		return propertiesLoader.getProperty(key);
	}
}