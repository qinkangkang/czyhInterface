package com.innee.czyhInterface.util.bmap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.innee.czyhInterface.util.PropertiesUtil;

/**
 * 百度云检索类
 * 
 * @author Robert Tsing
 */
public class LbsCloud {

	// 百度云URL
	public static final String LBS_URL = "http://api.map.baidu.com";

	// 百度云IP地址定位API URI
	public static final String LOCATION_URI_IP = "/location/ip";
	
	public static final String LOCATION_URI_GPS = "/geocoder/v2/";

	// 百度云检索API URI
	public static final String SEARCH_URI_NEARBY = "/geosearch/v3/nearby";
	public static final String SEARCH_URI_LOCAL = "/geosearch/v3/local";
	public static final String SEARCH_URI_BOUND = "/geosearch/v3/bound";
	public static final String SEARCH_URI_DETAIL = "/geosearch/v3/detail/";

	// 百度云存储API URI
	public static final String POST_URI_CREATE = "/geodata/v3/poi/create";
	public static final String POST_URI_UPDATE = "/geodata/v3/poi/update";
	public static final String POST_URI_DELETE = "/geodata/v3/poi/delete";
	public static final String GET_URI_LIST = "/geodata/v3/poi/list";
	public static final String GET_URI_DETAIL = "/geodata/v3/poi/detail";

	// LBS云公钥
	public static String AK = PropertiesUtil.getProperty("baiduMapAk");
	public static String SK = PropertiesUtil.getProperty("baiduMapSk");
	// 百度地图坐标系
	public static String coord_type = "3";
	// 百度地图坐标系名称
	public static String coor = "bd09ll";

	public static String getSn(String uri, LinkedHashMap<String, Object> paramsMap)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String paramsStr = toQueryString(paramsMap);
		String wholeStr = new StringBuilder().append(uri).append("?").append(paramsStr).append(SK).toString();
		// 对上面wholeStr再作utf8编码
		String tempStr = URLEncoder.encode(wholeStr, "UTF-8");
		return MD5(tempStr);
	}

	// 对Map内所有value作utf8编码，拼接返回结果
	private static String toQueryString(Map<String, Object> data) throws UnsupportedEncodingException {
		StringBuilder queryString = new StringBuilder();
		for (Entry<String, Object> pair : data.entrySet()) {
			queryString.append(pair.getKey() + "=");
			queryString.append(URLEncoder.encode((String) pair.getValue(), "UTF-8") + "&");
		}
		if (queryString.length() > 0) {
			queryString.deleteCharAt(queryString.length() - 1);
		}
		return queryString.toString();
	}

	// 来自stackoverflow的MD5计算方法，调用了MessageDigest库函数，并把byte数组结果转换成16进制
	private static String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}

}