package com.innee.czyhInterface.util;

import java.util.Map;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.innee.czyhInterface.exception.ServiceException;

/**
 * 提示文案工具类，将项目中所有的提示文案统一放置属性文件中
 * 
 * @author zgzhou
 *
 */
public class PromptInfoUtil {

	private static final Logger logger = LoggerFactory.getLogger(PromptInfoUtil.class);

	private static Map<String, Configuration> promptInfoMap = Maps.newHashMap();

	public static final String czyhInterface_BONUS = "bonus";

	public static final String czyhInterface_COUPON = "coupon";

	public static final String czyhInterface_EVENT = "event";

	public static final String czyhInterface_INDEX = "index";

	public static final String czyhInterface_ORDER = "order";

	public static final String czyhInterface_PERSONALIZED = "personalized";

	public static final String czyhInterface_SCENE = "scene";

	public static final String czyhInterface_USER = "user";

	public static final String czyhInterface_B_ORDER = "b_order";

	public static final String czyhInterface_B_USER = "b_user";

	public static void init() {

		Configurations configs = new Configurations();
		// FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new
		// FileBasedConfigurationBuilder<FileBasedConfiguration>(
		// PropertiesConfiguration.class);
		// Parameters params = new Parameters();
		try {
			// Read data from this file
			// File propertiesFile = new
			// File("promptInfo/czyhInterface_bonus.properties");
			// builder.configure(params.fileBased().setFile(propertiesFile));
			// Configuration config = builder.getConfiguration();
			// promptInfoMap.put(czyhInterface_BONUS, config);

			Configuration config = configs.properties("classpath:/promptInfo/m/czyhInterface_bonus.properties");
			promptInfoMap.put(czyhInterface_BONUS, config);

			config = configs.properties("classpath:/promptInfo/m/czyhInterface_coupon.properties");
			promptInfoMap.put(czyhInterface_COUPON, config);

			config = configs.properties("classpath:/promptInfo/m/czyhInterface_event.properties");
			promptInfoMap.put(czyhInterface_EVENT, config);

			config = configs.properties("classpath:/promptInfo/m/czyhInterface_index.properties");
			promptInfoMap.put(czyhInterface_INDEX, config);

			config = configs.properties("classpath:/promptInfo/m/czyhInterface_order.properties");
			promptInfoMap.put(czyhInterface_ORDER, config);

			config = configs.properties("classpath:/promptInfo/m/czyhInterface_personalized.properties");
			promptInfoMap.put(czyhInterface_PERSONALIZED, config);

			config = configs.properties("classpath:/promptInfo/m/czyhInterface_scene.properties");
			promptInfoMap.put(czyhInterface_SCENE, config);

			config = configs.properties("classpath:/promptInfo/m/czyhInterface_user.properties");
			promptInfoMap.put(czyhInterface_USER, config);

			config = configs.properties("classpath:/promptInfo/b/czyhInterface_order.properties");
			promptInfoMap.put(czyhInterface_B_ORDER, config);

			config = configs.properties("classpath:/promptInfo/b/czyhInterface_user.properties");
			promptInfoMap.put(czyhInterface_B_USER, config);

		} catch (ConfigurationException cex) {
			throw new ServiceException("初始化提示信息工具类出错：" + cex.getMessage(), cex);
		}
	}

	public static String getPrompt(String prop, String key) {
		Configuration config = promptInfoMap.get(prop);
		return config.getString(key, StringUtils.EMPTY);
	}

}