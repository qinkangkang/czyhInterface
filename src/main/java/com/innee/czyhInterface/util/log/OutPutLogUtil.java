package com.innee.czyhInterface.util.log;

import java.util.Map;

import org.slf4j.Logger;



public class OutPutLogUtil {

	public static void printLoggger(Exception e,Map<String, String> map,Logger logger){
		
		StringBuilder log = new StringBuilder();
		StringBuilder param = new StringBuilder();
	    for (String key : map.keySet()) {
	 	  param.append(key ).append(":").append(map.get(key)).append(",");
	    }
		log.append("报错行数:").append(e.getStackTrace()[1])
		.append(",报错信息:").append(e.fillInStackTrace())
		.append(",参数:{").append(param).append("}");
		logger.error(log.toString());
	}
}
