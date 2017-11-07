package com.innee.czyhInterface.util.IM;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.mapper.JsonMapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Userinfos;
import com.taobao.api.request.OpenimCustmsgPushRequest;
import com.taobao.api.request.OpenimCustmsgPushRequest.CustMsg;
import com.taobao.api.request.OpenimImmsgPushRequest;
import com.taobao.api.request.OpenimImmsgPushRequest.ImMsg;
import com.taobao.api.request.OpenimUsersAddRequest;
import com.taobao.api.request.OpenimUsersDeleteRequest;
import com.taobao.api.request.OpenimUsersGetRequest;
import com.taobao.api.request.OpenimUsersUpdateRequest;
import com.taobao.api.response.OpenimCustmsgPushResponse;
import com.taobao.api.response.OpenimImmsgPushResponse;
import com.taobao.api.response.OpenimUsersAddResponse;
import com.taobao.api.response.OpenimUsersDeleteResponse;
import com.taobao.api.response.OpenimUsersGetResponse;
import com.taobao.api.response.OpenimUsersUpdateResponse;

public class IMUtil {

	private static final Logger logger = LoggerFactory.getLogger(IMUtil.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	private static String IMServerUrl = null;

	private static String IMAppkey = null;

	private static String IMAppsecret = null;

	private static TaobaoClient client = null;

	public static void init() {
		IMAppkey = PropertiesUtil.getProperty("Appkey");
		IMAppsecret = PropertiesUtil.getProperty("Appsecret");
		IMServerUrl = PropertiesUtil.getProperty("tabaoServiceUrl");
		client = new DefaultTaobaoClient(IMServerUrl, IMAppkey, IMAppsecret);
	}

	public static IMResult addIMUser(String nickname, String headUrl, String userid, String password, long age,
			String wecaht, String sex, String phone) {
		IMResult imResult = new IMResult();

		OpenimUsersAddRequest req = new OpenimUsersAddRequest();
		List<Userinfos> userInfoList = new ArrayList<Userinfos>();
		Userinfos userInfo = new Userinfos();
		userInfoList.add(userInfo);
		userInfo.setNick(nickname);
		userInfo.setIconUrl(headUrl);
		userInfo.setEmail("");
		userInfo.setMobile(phone);
		userInfo.setTaobaoid("");
		userInfo.setUserid(userid);
		userInfo.setPassword(password);
		userInfo.setRemark("demo");
		userInfo.setExtra("{}");
		userInfo.setCareer("demo");
		userInfo.setVip("{}");
		userInfo.setAddress("demo");
		userInfo.setName("czyh");
		userInfo.setAge(age);
		userInfo.setGender(sex);
		userInfo.setWechat(wecaht);
		userInfo.setQq("demo");
		userInfo.setWeibo("demo");
		req.setUserinfos(userInfoList);
		OpenimUsersAddResponse rsp = null;
		try {
			rsp = client.execute(req);
			imResult.setSuccess(true);
			imResult.setContent(rsp.getMsg());
			imResult.setResponse(rsp.getBody());
		} catch (ApiException e) {
			e.printStackTrace();
			logger.error("IM添加用户时出错");
		}

		return imResult;
	}

	public static IMResult deleteIMUser(String usersId) {
		IMResult imResult = new IMResult();
		OpenimUsersDeleteRequest req = new OpenimUsersDeleteRequest();
		req.setUserids(usersId);

		try {
			OpenimUsersDeleteResponse rsp = client.execute(req);
			imResult.setSuccess(true);
			imResult.setContent(rsp.getMsg());
			imResult.setResponse(rsp.getBody());
		} catch (ApiException e) {
			e.printStackTrace();
			logger.error("IM删除用户时出错");
		}

		return imResult;
	}

	public static IMResult updateIMUser(String usersId, String nickname, String headUrl, String userid, String password,
			long age, String wecaht, String sex, String phone) {
		IMResult imResult = new IMResult();

		OpenimUsersUpdateRequest req = new OpenimUsersUpdateRequest();
		List<Userinfos> list2 = new ArrayList<Userinfos>();
		Userinfos obj3 = new Userinfos();
		list2.add(obj3);
		obj3.setNick(nickname);
		obj3.setIconUrl(headUrl);
		obj3.setEmail("");
		obj3.setMobile(phone);
		obj3.setTaobaoid("");
		obj3.setUserid(usersId);
		obj3.setPassword(password);
		obj3.setRemark("demo");
		obj3.setExtra("{}");
		obj3.setCareer("demo");
		obj3.setVip("{}");
		obj3.setAddress("demo");
		obj3.setName("demo");
		obj3.setAge(age);
		obj3.setGender(sex);
		obj3.setWechat(wecaht);
		obj3.setQq("demo");
		obj3.setWeibo("demo");
		req.setUserinfos(list2);
		try {
			OpenimUsersUpdateResponse rsp = client.execute(req);
			imResult.setSuccess(true);
			imResult.setContent(rsp.getMsg());
			imResult.setResponse(rsp.getBody());
		} catch (ApiException e) {
			e.printStackTrace();
			logger.error("IM修改用户时出错");
		}

		return imResult;
	}

	public static IMResult getIMUser(String usersId) {
		IMResult imResult = new IMResult();
		OpenimUsersGetRequest req = new OpenimUsersGetRequest();
		req.setUserids(usersId);
		try {
			OpenimUsersGetResponse rsp = client.execute(req);
			imResult.setSuccess(true);
			imResult.setContent(rsp.getMsg());
			imResult.setResponse(rsp.getBody());
		} catch (ApiException e) {
			e.printStackTrace();
			logger.error("IM获取用户时出错");
		}

		return imResult;
	}

	public static IMResult pushIMAuto(String fromUserId, List<String> usersId, long msgType, String context) {
		IMResult imResult = new IMResult();

		OpenimImmsgPushRequest req = new OpenimImmsgPushRequest();
		ImMsg obj1 = new ImMsg();
		obj1.setFromUser(fromUserId);
		obj1.setToUsers(usersId);
		obj1.setMsgType(msgType);
		obj1.setContext(context);
		obj1.setToAppkey("0");
		obj1.setMediaAttr("{\"type\":\"amr\",\"playtime\":6}");
		obj1.setFromTaobao(0L);
		req.setImmsg(obj1);
		OpenimImmsgPushResponse rsp;
		try {
			rsp = client.execute(req);
			imResult.setSuccess(true);
			imResult.setContent(rsp.getMsg());
			imResult.setResponse(rsp.getBody());
		} catch (ApiException e) {
			e.printStackTrace();
			logger.error("IM推送标准消息时出错");
		}

		return imResult;
	}

	/**
	 * 推送自定义消息
	 * 
	 * @param fromUserId
	 * @param usersId
	 * @param summary
	 * @param data
	 * @return
	 */
	public static IMResult pushIMCust(String fromUserId, List<String> usersId, String summary, String data) {
		IMResult imResult = new IMResult();

		OpenimCustmsgPushRequest req = new OpenimCustmsgPushRequest();
		CustMsg obj1 = new CustMsg();
		obj1.setFromUser(fromUserId);
		obj1.setToAppkey("0");
		obj1.setToUsers(usersId);
		obj1.setSummary(summary);
		obj1.setData(data);
		obj1.setAps("{\"alert\":\"ios apns push\"}");
		obj1.setApnsParam("apns推送的附带数据");
		obj1.setInvisible(0L);
		obj1.setFromNick("sender_nick");
		obj1.setFromTaobao(0L);
		req.setCustmsg(obj1);
		OpenimCustmsgPushResponse rsp = null;
		try {
			rsp = client.execute(req);
			imResult.setSuccess(true);
			imResult.setContent(rsp.getMsg());
			imResult.setResponse(rsp.getBody());
		} catch (ApiException e) {
			e.printStackTrace();
			logger.error("IM推送自定义消息时出错");
		}
		return imResult;
	}
}
