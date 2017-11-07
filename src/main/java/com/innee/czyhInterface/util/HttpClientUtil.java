package com.innee.czyhInterface.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.dto.m.ResponseDTO;

public class HttpClientUtil {

	private static final int TIMEOUT_SECONDS = 20;

	private static final int POOL_SIZE = 10;

	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	private static CloseableHttpClient httpClient;

	// 创建包含connection pool与超时设置的client
	public static void init() {
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_SECONDS * 1000)
				.setConnectTimeout(TIMEOUT_SECONDS * 1000).build();

		httpClient = HttpClientBuilder.create().setMaxConnTotal(POOL_SIZE).setMaxConnPerRoute(POOL_SIZE)
				.setDefaultRequestConfig(requestConfig).build();
	}

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

	public static String callUrlGet(String url, Map<String, Object> paramsMap) throws Exception {
		String params = toQueryString(paramsMap);

		StringBuilder allUrl = new StringBuilder();
		allUrl.append(url).append("?").append(params);

		// 获取内容
		HttpGet httpGet = new HttpGet(allUrl.toString());

		CloseableHttpResponse remoteResponse = httpClient.execute(httpGet);
		String st = null;
		try {
			// 判断返回值
			int statusCode = remoteResponse.getStatusLine().getStatusCode();
			if (statusCode >= 400) {
				throw new HttpException("Http return error code is :" + statusCode);
			}
			HttpEntity entity = remoteResponse.getEntity();

			// 输出内容
			InputStream input = entity.getContent();
			st = IOUtils.toString(input, "UTF-8");
		} finally {
			remoteResponse.close();
		}
		return st;
	}

	public static String callUrlPost(String url, Map<String, Object> paramsMap) throws Exception {
		// 获取内容
		HttpPost httpPost = new HttpPost(url);

		// 添加参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Map.Entry<String, Object> e : paramsMap.entrySet()) {
			params.add(new BasicNameValuePair(e.getKey(), e.getValue().toString()));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

		CloseableHttpResponse remoteResponse = httpClient.execute(httpPost);
		String st = null;
		try {
			// 判断返回值
			int statusCode = remoteResponse.getStatusLine().getStatusCode();
			if (statusCode >= 400) {
				throw new HttpException("Http return error code is :" + statusCode);
			}
			HttpEntity entity = remoteResponse.getEntity();
			// 输出内容
			InputStream input = entity.getContent();
			st = IOUtils.toString(input, "UTF-8");
		} finally {
			remoteResponse.close();
		}
		return st;
	}

	public static Map<String, Object> callUrlPostReturnMap(String url, Map<String, Object> paramsMap) throws Exception {
		Map<String, Object> returnMap = Maps.newHashMap();
		// 获取内容
		HttpPost httpPost = new HttpPost(url);

		// 添加参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Map.Entry<String, Object> e : paramsMap.entrySet()) {
			params.add(new BasicNameValuePair(e.getKey(), e.getValue().toString()));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		CloseableHttpResponse remoteResponse = httpClient.execute(httpPost);
		try {
			// 判断返回值
			int statusCode = remoteResponse.getStatusLine().getStatusCode();
			returnMap.put("statusCode", statusCode);
			if (statusCode >= 400) {
				throw new HttpException("Http return error code is :" + statusCode);
			}
			HttpEntity entity = remoteResponse.getEntity();
			// 输出内容
			InputStream input = entity.getContent();
			String st = IOUtils.toString(input, "UTF-8");
			returnMap.put("result", st);
		} finally {
			remoteResponse.close();
		}
		return returnMap;
	}

	public static String callUrlPost(String url, List<NameValuePair> params) throws Exception {
		// 获取内容
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

		CloseableHttpResponse remoteResponse = httpClient.execute(httpPost);
		String st = null;
		try {
			// 判断返回值
			int statusCode = remoteResponse.getStatusLine().getStatusCode();
			if (statusCode >= 400) {
				throw new HttpException("Http return error code is :" + statusCode);
			}
			HttpEntity entity = remoteResponse.getEntity();
			// 输出内容
			InputStream input = entity.getContent();
			st = IOUtils.toString(input, "UTF-8");
		} finally {
			remoteResponse.close();
		}
		return st;
	}
	
	public static String callUrlPostByJson(String url, String json) throws Exception {
		// 获取内容
		HttpPost httpPost = new HttpPost(url);

		httpPost.setEntity(new StringEntity(json, "UTF-8"));

		CloseableHttpResponse remoteResponse = httpClient.execute(httpPost);
		String st = null;
		try {
			// 判断返回值
			int statusCode = remoteResponse.getStatusLine().getStatusCode();
			if (statusCode >= 400) {
				throw new HttpException("Http return error code is :" + statusCode);
			}
			HttpEntity entity = remoteResponse.getEntity();
			// 输出内容
			InputStream input = entity.getContent();
			st = IOUtils.toString(input, "UTF-8");
		} finally {
			remoteResponse.close();
		}
		return st;
	}

	public static void destroy() {
		try {
			httpClient.close();
		} catch (IOException e) {
			logger.error("httpclient close fail", e);
		}
	}

	public static void main(String[] args) {
		String url = "http://api.map.baidu.com/geosearch/v3/nearby";

		HttpClientUtil.init();
		try {
			System.out.println(HttpClientUtil.callUrlGet(url, Maps.newHashMap()));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
	}
	
	public static ResponseDTO getRequest(String url, Map<String, Object> map) throws Exception {
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			String str = null;
			str = HttpClientUtil.callUrlPost(url.toString(), map);
			responseDTO = mapper.fromJson(str, ResponseDTO.class);
		} catch (Exception e) {
			responseDTO.setMsg("您的网络不稳定，请稍后重试");
			e.printStackTrace();
		}
		return responseDTO;
	}
	
	
}