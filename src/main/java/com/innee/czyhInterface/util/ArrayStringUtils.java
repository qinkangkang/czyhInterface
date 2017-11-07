package com.innee.czyhInterface.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArrayStringUtils {

	private static final Logger logger = LoggerFactory.getLogger(ArrayStringUtils.class);

	public static final String Separator = ";";

	public static String arrayToString(String[] ids, String separator) {
		if (ArrayUtils.isEmpty(ids)) {
			return StringUtils.EMPTY;
		}
		StringBuilder ret = new StringBuilder();
		for (String id : ids) {
			ret.append(id).append(separator);
		}
		return ret.deleteCharAt(ret.length() - 1).toString();
	}

	public static String[] stringToArray(String ids, String separator) {
		if (StringUtils.isBlank(ids)) {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		String[] ret = StringUtils.split(ids, separator);
		return ret;
	}

	public static String addAsterisk(String info) {
		if (StringUtils.isBlank(info)) {
			return StringUtils.EMPTY;
		} else if (info.length() == 1) {
			return info;
		} else if (info.length() == 2) {
			return info.substring(0, 1) + "*";
		} else {
			StringBuilder ret = new StringBuilder();
			ret.append(info.substring(0, 1)).append("***").append(info.substring(info.length()-1,info.length()));
			return ret.toString();
		}
		// } else {
		// StringBuilder ret = new StringBuilder();
		// ret.append(info.substring(0, 1));
		// for (int i = 0; i < info.length() - 2; i++) {
		// ret.append("*");
		// }
		// ret.append(info.substring(info.length() - 1));
		// return ret.toString();
		// }
	}

	public static void main(String[] args) {
		System.out.println(ArrayStringUtils.addAsterisk("jinshengzhi"));
	}
}