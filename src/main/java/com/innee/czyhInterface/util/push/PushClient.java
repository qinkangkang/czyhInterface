package com.innee.czyhInterface.util.push;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.alibaba.fastjson.JSONObject;
import com.innee.czyhInterface.dto.m.ResponseDTO;

public class PushClient {

	// The user agent
	protected final String USER_AGENT = "Mozilla/5.0";

	// This object is used for sending the post request to Umeng
	protected HttpClient client = new DefaultHttpClient();

	// The host
	protected static final String host = "http://msg.umeng.com";

	// The upload path
	protected static final String uploadPath = "/upload";

	// The post path
	protected static final String postPath = "/api/send";

	// The post add tag method path
	protected static final String tagAddPath = "/api/tag/add";

	// The post del tag method path
	protected static final String tagDelPath = "/api/tag/delete";

	// The post del tag method path
	protected static final String taglistPath = "/api/tag/list";

	public ResponseDTO send(UmengNotification msg) throws Exception {
		// System.out.println(msg + "测试来了");
		ResponseDTO responseDTO = new ResponseDTO();

		String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000));
		msg.setPredefinedKeyValue("timestamp", timestamp);
		String url = host + postPath;
		String postBody = msg.getPostBody();
		String sign = DigestUtils.md5Hex(("POST" + url + postBody + msg.getAppMasterSecret()).getBytes("utf8"));
		url = url + "?sign=" + sign;
		HttpPost post = new HttpPost(url);
		post.setHeader("User-Agent", USER_AGENT);
		StringEntity se = new StringEntity(postBody, "UTF-8");
		post.setEntity(se);
		// Send the post request and get the response
		HttpResponse response = client.execute(post);
		int status = response.getStatusLine().getStatusCode();
		// System.out.println("Response Code : " + status);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		// System.out.println(result.toString());
		if (status == 200) {
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("推送成功");
			return responseDTO;
		} else {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("推送失败");
			return responseDTO;
		}

	}

	// Upload file with device_tokens to Umeng
	public String uploadContents(String appkey, String appMasterSecret, String contents) throws Exception {
		// Construct the json string
		JSONObject uploadJson = new JSONObject();
		uploadJson.put("appkey", appkey);
		String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000));
		uploadJson.put("timestamp", timestamp);
		uploadJson.put("content", contents);
		// Construct the request
		String url = host + uploadPath;
		String postBody = uploadJson.toString();
		String sign = DigestUtils.md5Hex(("POST" + url + postBody + appMasterSecret).getBytes("utf8"));
		url = url + "?sign=" + sign;
		HttpPost post = new HttpPost(url);
		post.setHeader("User-Agent", USER_AGENT);
		StringEntity se = new StringEntity(postBody, "UTF-8");
		post.setEntity(se);
		// Send the post request and get the response
		HttpResponse response = client.execute(post);
		// System.out.println("Response Code : " +
		// response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		// System.out.println(result.toString());
		// Decode response string and get file_id from it
		JSONObject respJson = JSONObject.parseObject(result.toString());
		String ret = respJson.getString("ret");
		if (!ret.equals("SUCCESS")) {
			throw new Exception("Failed to upload file");
		}
		JSONObject data = respJson.getJSONObject("data");
		String fileId = data.getString("file_id");
		// Set file_id into rootJson using setPredefinedKeyValue

		return fileId;
	}

	public ResponseDTO AddTags(String appkey, String appMasterSecret, String tag, String device_tokens)
			throws Exception {

		ResponseDTO responseDTO = new ResponseDTO();
		JSONObject TagJson = new JSONObject();
		TagJson.put("appkey", appkey);
		String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000));
		TagJson.put("timestamp", timestamp);
		TagJson.put("tag", tag);
		TagJson.put("device_tokens", device_tokens);

		String url = host + tagAddPath;
		String postBody = TagJson.toString();
		// System.out.println(postBody + "postBody内容");
		String sign = DigestUtils.md5Hex(("POST" + url + postBody + appMasterSecret).getBytes("utf8"));
		url = url + "?sign=" + sign;
		HttpPost post = new HttpPost(url);

		post.setHeader("User-Agent", USER_AGENT);
		StringEntity se = new StringEntity(postBody, "UTF-8");
		post.setEntity(se);
		// Send the post request and get the response
		HttpResponse response = client.execute(post);

		int status = response.getStatusLine().getStatusCode();
		// System.out.println("Response Code : " + status);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		System.out.println(result.toString());
		if (status == 200) {
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("添加Tag标签成功");
			return responseDTO;
		} else {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("添加Tag标签失败");
			return responseDTO;
		}

	}

	public ResponseDTO DelTags(String appkey, String appMasterSecret, String tag, String device_tokens)
			throws Exception {

		ResponseDTO responseDTO = new ResponseDTO();
		JSONObject TagJson = new JSONObject();
		TagJson.put("appkey", appkey);
		String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000));
		TagJson.put("timestamp", timestamp);
		TagJson.put("tag", tag);
		TagJson.put("device_tokens", device_tokens);

		String url = host + tagDelPath;
		String postBody = TagJson.toString();
		System.out.println(postBody + "postBody内容");
		String sign = DigestUtils.md5Hex(("POST" + url + postBody + appMasterSecret).getBytes("utf8"));
		url = url + "?sign=" + sign;
		HttpPost post = new HttpPost(url);

		post.setHeader("User-Agent", USER_AGENT);
		StringEntity se = new StringEntity(postBody, "UTF-8");
		post.setEntity(se);
		// Send the post request and get the response
		HttpResponse response = client.execute(post);

		int status = response.getStatusLine().getStatusCode();
		System.out.println("Response Code : " + status);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		System.out.println(result.toString());
		if (status == 200) {
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("移除Tag标签成功");
			return responseDTO;
		} else {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("移除Tag标签失败");
			return responseDTO;
		}

	}

}