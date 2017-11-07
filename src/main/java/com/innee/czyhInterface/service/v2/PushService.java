package com.innee.czyhInterface.service.v2;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.innee.czyhInterface.dao.CustomerTagDAO;
import com.innee.czyhInterface.dao.PushDAO;
import com.innee.czyhInterface.dao.PushTokenDAO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.entity.TCustomerTag;
import com.innee.czyhInterface.entity.TPush;
import com.innee.czyhInterface.entity.TPushToken;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.push.AndroidNotification;
import com.innee.czyhInterface.util.push.PushClient;
import com.innee.czyhInterface.util.push.android.AndroidBroadcast;
import com.innee.czyhInterface.util.push.android.AndroidCustomizedcast;
import com.innee.czyhInterface.util.push.android.AndroidFilecast;
import com.innee.czyhInterface.util.push.android.AndroidGroupcast;
import com.innee.czyhInterface.util.push.android.AndroidUnicast;
import com.innee.czyhInterface.util.push.ios.IOSBroadcast;
import com.innee.czyhInterface.util.push.ios.IOSCustomizedcast;
import com.innee.czyhInterface.util.push.ios.IOSFilecast;
import com.innee.czyhInterface.util.push.ios.IOSGroupcast;
import com.innee.czyhInterface.util.push.ios.IOSUnicast;

/**
 * 友盟推送
 * 
 * @author jinshenzhi
 *
 */
@Component
@Transactional
public class PushService {

	private static final Logger logger = LoggerFactory.getLogger(PushService.class);

	private PushClient client = new PushClient();

	protected final JSONObject rootJson = new JSONObject();

	protected final String USER_AGENT = "Mozilla/5.0";

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private PushTokenDAO pushtokenDAO;

	@Autowired
	private PushDAO pushDAO;

	@Autowired
	private CustomerTagDAO customerTagDAO;

	@Autowired
	private FxlService fxlService;

	/**
	 * 广播推送Android
	 * 
	 * @param Ticker
	 * @param title
	 * @param test
	 * @param des
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public ResponseDTO sendAndroidBroadcast(String Ticker, String title, String text, String des) throws Exception {

		AndroidBroadcast broadcast = new AndroidBroadcast(PropertiesUtil.getProperty("umappkey"),
				PropertiesUtil.getProperty("appMasterSecret"));
		broadcast.setTicker(Ticker);
		broadcast.setTitle(title);
		broadcast.setText(text);
		broadcast.setDescription(des);// 推送消息描述
		broadcast.goAppAfterOpen();
		broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		broadcast.setProductionMode();
		broadcast.setExtraField("test", "helloworld");
		ResponseDTO res = client.send(broadcast);
		// TPush tPush = new TPush();
		// tPush.setId();

		return res;
	}

	/**
	 * 单播推送Android
	 * 
	 * @param DeviceToken
	 * @param Ticker
	 * @param title
	 * @param test
	 * @param des
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public ResponseDTO sendAndroidUnicast(String DeviceToken, String Ticker, String title, String text, String des)
			throws Exception {

		AndroidUnicast unicast = new AndroidUnicast(PropertiesUtil.getProperty("umappkey"),
				PropertiesUtil.getProperty("appMasterSecret"));

		unicast.setDeviceToken(DeviceToken);
		unicast.setTicker(Ticker);
		unicast.setTitle(title);
		unicast.setText(text);
		unicast.setDescription(des);// 推送消息描述
		unicast.goAppAfterOpen();
		unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		unicast.setProductionMode();// 模式选择
		unicast.setExtraField("test", "helloworld");
		ResponseDTO res = client.send(unicast);

		return res;
	}

	/**
	 * 组播推送Android
	 * 
	 * @param DeviceToken
	 * @param Ticker
	 * @param title
	 * @param test
	 * @param des
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public ResponseDTO sendAndroidGroupcast(String tag, String condition, String Ticker, String title, String text,
			String des) throws Exception {

		AndroidGroupcast groupcast = new AndroidGroupcast(PropertiesUtil.getProperty("umappkey"),
				PropertiesUtil.getProperty("appMasterSecret"));

		JSONObject filterJson = new JSONObject();
		JSONObject whereJson = new JSONObject();
		JSONArray tagArray = new JSONArray();
		JSONObject testTag = new JSONObject();
		// JSONObject TestTag = new JSONObject();
		testTag.put(tag, condition);// key：推送标签 value 推送具体 例如 推送标签为:版本
									// 推送内容为:1.0.3
		// TestTag.put("tag", "Test");//可进行多标签推送
		tagArray.add(testTag);
		// tagArray.put(TestTag);
		whereJson.put("and", tagArray);
		filterJson.put("where", whereJson);
		// System.out.println(filterJson.toString());

		groupcast.setFilter(filterJson);
		groupcast.setTicker(Ticker);
		groupcast.setTitle(title);
		groupcast.setText(text);
		groupcast.goAppAfterOpen();
		groupcast.setDescription(des);
		groupcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		groupcast.setProductionMode();
		ResponseDTO res = client.send(groupcast);

		return res;
	}

	/**
	 * 自定义alias推送Android
	 * 
	 * @param DeviceToken
	 * @param Ticker
	 * @param title
	 * @param test
	 * @param des
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public ResponseDTO UmengPushAndroidCustomizedcast(String alias, String aliastype, String Ticker, String title,
			String text, String des) throws Exception {

		AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(PropertiesUtil.getProperty("umappkey"),
				PropertiesUtil.getProperty("appMasterSecret"));

		customizedcast.setAlias(alias, aliastype);
		customizedcast.setTicker(Ticker);
		customizedcast.setTitle(title);
		customizedcast.setText(text);
		customizedcast.setDescription(des);
		customizedcast.goAppAfterOpen();
		customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);

		customizedcast.setProductionMode();
		ResponseDTO res = client.send(customizedcast);
		return res;
	}

	/**
	 * 文件播推送Android
	 * 
	 * @param DeviceToken
	 * @param Ticker
	 * @param title
	 * @param test
	 * @param des
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public ResponseDTO UmengPushAndroidCustomizedcastFile(String contents, String aliastype, String Ticker,
			String title, String text, String des) throws Exception {

		AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(PropertiesUtil.getProperty("umappkey"),
				PropertiesUtil.getProperty("appMasterSecret"));
		// 待办事项设置您的别名，并用逗号分开他们，如果有多个别名。
		// 如果你有很多别名，你还可以上传一个包含这些别名的文件，然后
		// 使用file_id发送定制的通知。

		String fileId = client.uploadContents(PropertiesUtil.getProperty("umappkey"),
				PropertiesUtil.getProperty("appMasterSecret"), contents);
		customizedcast.setFileId(fileId, aliastype);
		customizedcast.setTicker(Ticker);
		customizedcast.setTitle(title);
		customizedcast.setText(text);
		customizedcast.goAppAfterOpen();
		customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		customizedcast.setProductionMode();

		ResponseDTO res = client.send(customizedcast);
		return res;
	}

	/**
	 * 文件播推送Android
	 * 
	 * @param DeviceToken
	 * @param Ticker
	 * @param title
	 * @param test
	 * @param des
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public ResponseDTO sendAndroidFilecast(String contents, String aliastype, String Ticker, String title, String text,
			String des) throws Exception {

		AndroidFilecast filecast = new AndroidFilecast(PropertiesUtil.getProperty("umappkey"),
				PropertiesUtil.getProperty("appMasterSecret"));
		// TODO 上传您的设备令牌，并使用“\”来拆分它们，如果有多个令牌 contents="aa"+"\n"+"bb"
		String fileId = client.uploadContents(PropertiesUtil.getProperty("umappkey"),
				PropertiesUtil.getProperty("appMasterSecret"), contents);
		filecast.setFileId(fileId);
		filecast.setTicker(Ticker);
		filecast.setTitle(title);
		filecast.setText(text);
		filecast.setDescription(des);
		filecast.goAppAfterOpen();
		filecast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		ResponseDTO res = client.send(filecast);

		return res;
	}

	/**
	 * 广播推送IOS
	 * 
	 * @param Ticker
	 * @param title
	 * @param test
	 * @param des
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public ResponseDTO sendIOSroadcast(String text, String des) throws Exception {

		IOSBroadcast broadcast = new IOSBroadcast(PropertiesUtil.getProperty("IOSUMAppKey"),
				PropertiesUtil.getProperty("IOSappMasterSecret"));

		broadcast.setAlert(text);
		broadcast.setBadge(1);
		broadcast.setSound("default");
		if (des == null) {
			broadcast.setDescription("ios广播消息");
		} else {
			broadcast.setDescription("ios广播消息");
		}

		broadcast.setTestMode();// 开发模式
		// broadcast.setProductionMode();//生产模式

		broadcast.setCustomizedField("test", "helloworld");
		ResponseDTO res = client.send(broadcast);

		return res;
	}

	/**
	 * 单播推送IOS
	 * 
	 * @param DeviceToken
	 * @param Ticker
	 * @param title
	 * @param test
	 * @param des
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public ResponseDTO sendIOSUnicast(String DeviceToken, String text, String des) throws Exception {

		IOSUnicast unicast = new IOSUnicast(PropertiesUtil.getProperty("IOSUMAppKey"),
				PropertiesUtil.getProperty("IOSappMasterSecret"));

		unicast.setDeviceToken(DeviceToken);
		unicast.setAlert(text);
		unicast.setBadge(1);
		unicast.setSound("default");
		if (des == null) {
			unicast.setDescription("ios消息单播");
		} else {
			unicast.setDescription(des);
		}

		unicast.setTestMode();
		// broadcast.setProductionMode();//生产模式
		unicast.setCustomizedField("test", "helloworld");
		ResponseDTO res = client.send(unicast);

		return res;
	}

	/**
	 * 组播推送IOS
	 * 
	 * @param DeviceToken
	 * @param Ticker
	 * @param title
	 * @param test
	 * @param des
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public ResponseDTO sendIOSGroupcast(String tag, String condition, String Ticker, String title, String text,
			String des) throws Exception {

		IOSGroupcast groupcast = new IOSGroupcast(PropertiesUtil.getProperty("IOSUMAppKey"),
				PropertiesUtil.getProperty("IOSappMasterSecret"));

		JSONObject filterJson = new JSONObject();
		JSONObject whereJson = new JSONObject();
		JSONArray tagArray = new JSONArray();
		JSONObject testTag = new JSONObject();
		testTag.put(tag, condition);
		tagArray.add(testTag);
		whereJson.put("and", tagArray);
		filterJson.put("where", whereJson);
		// System.out.println(filterJson.toString());

		groupcast.setFilter(filterJson);
		groupcast.setAlert(text);
		groupcast.setBadge(1);
		groupcast.setSound("default");
		if (des == null) {
			groupcast.setDescription("组播推送");
		} else {
			groupcast.setDescription(des);
		}
		groupcast.setTestMode();
		// broadcast.setProductionMode();//生产模式
		ResponseDTO res = client.send(groupcast);

		return res;
	}

	/**
	 * 个性推送IOS
	 * 
	 * @param DeviceToken
	 * @param Ticker
	 * @param title
	 * @param test
	 * @param des
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public ResponseDTO sendIOSCustomizedcast(String alias, String aliastype, String text, String des) throws Exception {

		IOSCustomizedcast customizedcast = new IOSCustomizedcast(PropertiesUtil.getProperty("IOSUMAppKey"),
				PropertiesUtil.getProperty("IOSappMasterSecret"));
		// 设置您的别名和alias_type这里，并用逗号分开他们，如果存在多个别名。
		// 如果你有很多别名，你还可以上传一个包含这些别名的文件，然后
		// 使用file_id发送定制的通知。
		customizedcast.setAlias(alias, aliastype);
		customizedcast.setAlert(text);
		customizedcast.setBadge(1);
		customizedcast.setSound("default");
		if (des == null) {
			customizedcast.setDescription("alias推送");
		} else {
			customizedcast.setDescription(des);
		}
		customizedcast.setTestMode();
		// broadcast.setProductionMode();//生产模式
		ResponseDTO res = client.send(customizedcast);

		return res;
	}

	/**
	 * 个性推送IOS
	 * 
	 * @param DeviceToken
	 * @param Ticker
	 * @param title
	 * @param test
	 * @param des
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public ResponseDTO sendIOSFilecast(String contents, String aliastype, String Ticker, String title, String text,
			String des) throws Exception {

		IOSFilecast filecast = new IOSFilecast(PropertiesUtil.getProperty("IOSUMAppKey"),
				PropertiesUtil.getProperty("IOSappMasterSecret"));
		// TODO 上传您的设备令牌，并使用“\”来拆分它们，如果有多个令牌 contents="aa"+"\n"+"bb"
		String fileId = client.uploadContents(PropertiesUtil.getProperty("IOSUMAppKey"),
				PropertiesUtil.getProperty("IOSappMasterSecret"), contents);
		filecast.setFileId(fileId);
		filecast.setAlert(text);
		filecast.setBadge(1);
		filecast.setSound("default");
		if (des == null) {
			filecast.setDescription("文件推送");
		} else {
			filecast.setDescription(des);
		}
		filecast.setTestMode();
		// broadcast.setProductionMode();//生产模式

		ResponseDTO res = client.send(filecast);

		return res;
	}

	/**
	 * 添加AndroidTag
	 * 
	 * @param tag
	 * @param DeviceToken
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public ResponseDTO tagAndroidAdd(String tag, String device_tokens) throws Exception {
		ResponseDTO res = client.AddTags(PropertiesUtil.getProperty("umappkey"),
				PropertiesUtil.getProperty("appMasterSecret"), tag, device_tokens);
		return res;
	}

	/**
	 * 添加IOSTag
	 * 
	 * @param tag
	 * @param DeviceToken
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public ResponseDTO tagIOSAdd(String tag, String device_tokens) throws Exception {
		ResponseDTO res = client.AddTags(PropertiesUtil.getProperty("IOSUMAppKey"),
				PropertiesUtil.getProperty("IOSappMasterSecret"), tag, device_tokens);
		return res;
	}

	/**
	 * 移除AndroidTag
	 * 
	 * @param tag
	 * @param DeviceToken
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public ResponseDTO tagAndroidDel(String tag, String device_tokens) throws Exception {
		ResponseDTO res = client.DelTags(PropertiesUtil.getProperty("umappkey"),
				PropertiesUtil.getProperty("appMasterSecret"), tag, device_tokens);
		return res;
	}

	/**
	 * 移除IOSTag
	 * 
	 * @param tag
	 * @param DeviceToken
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public ResponseDTO tagIOSDel(String tag, String device_tokens) throws Exception {
		ResponseDTO res = client.DelTags(PropertiesUtil.getProperty("IOSUMAppKey"),
				PropertiesUtil.getProperty("IOSappMasterSecret"), tag, device_tokens);
		return res;
	}

	/**
	 * 保存用户Token跟设备唯一编码对应
	 * 
	 * @param ticket
	 * @param deviceTokens
	 * @param DEVICE_ID
	 * @param clientType
	 * @return
	 */
	public ResponseDTO saveToken(String ticket, String deviceTokens, String DEVICE_ID, Integer clientType) {

		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("请填写ticket！");
			return responseDTO;
		}
		if (StringUtils.isBlank(deviceTokens)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请填写deviceTokens！");
			return responseDTO;
		}
		if (StringUtils.isBlank(DEVICE_ID)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请填写DEVICE_ID！");
			return responseDTO;
		}

		String customerId = fxlService.getCustomerIdByTicket(ticket, clientType);

		TPushToken TPushToken = new TPushToken();
		Date now = new Date();
		TPushToken.setFcustomerId(customerId);
		TPushToken.setFtoken(deviceTokens);
		TPushToken.setFdevice(DEVICE_ID);
		TPushToken.setFcreateTime(now);

		pushtokenDAO.save(TPushToken);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("绑定Token成功");

		return responseDTO;
	}

//	public void pushTask(String pushId) {
//
//		TPush tpush = pushDAO.findAllPush(pushId);
//
//		if (tpush.getFpushuserType() == 1) {
//			pushAll(pushId, tpush.getFtargetType(), tpush.getFtitle(), tpush.getFcontent(), tpush.getFdescription(),
//					tpush.getFurl(), tpush.getFtargetObjectId());
//		} else if (tpush.getFpushuserType() == 2) {
//			pushUnicast(pushId, tpush.getFtargetType(), tpush.getFtitle(), tpush.getFcontent(), tpush.getFdescription(),
//					tpush.getFurl(), tpush.getFtargetObjectId(), tpush.getFdeviceToken());
//		} else if (tpush.getFpushuserType() == 3) {
//			pushGroupcast(pushId, tpush.getFtargetType(), tpush.getFtitle(), tpush.getFcontent(),
//					tpush.getFdescription(), tpush.getFurl(), tpush.getFtargetObjectId(), tpush.getFdimension(),
//					tpush.getFuserTag(), tpush.getFappVersion());
//		} else if (tpush.getFpushuserType() == 4) {
//			System.out.println("Alias特定用户推送,暂时不实现这个功能");
//		}
//
//	}

	/**
	 * 定时任务专用(广播推送IOS Android通用方法)
	 * 
	 * @param pushId
	 */
	public void pushAll(String pushId, Integer ftargetType, String ftitle, String fcontent, String fdescription,
			String furl, String ftargetObjectId) {

		String target_type = DictionaryUtil.getString(DictionaryUtil.PushLinkTargetType, ftargetType);

		try {
			AndroidBroadcast broadcast = new AndroidBroadcast(PropertiesUtil.getProperty("umappkey"),
					PropertiesUtil.getProperty("appMasterSecret"));
			broadcast.setTicker("您有新的通知栏消息");
			broadcast.setTitle(ftitle);// 消息标题
			broadcast.setText(fcontent);// 消息内容
			broadcast.setDescription(fdescription);// 消息描述
			broadcast.goAppAfterOpen();
			broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);

			// broadcast.setProductionMode();//生产环境 “上线后打开此注释切换生产模式”
			broadcast.setTestMode();// 测试环境
			if (furl.equals("")) {
				broadcast.setExtraField("target_type", target_type);
				broadcast.setExtraField("target_id", ftargetObjectId);
			} else if (ftargetObjectId.equals("")) {
				broadcast.setExtraField("target_type", target_type);
				broadcast.setExtraField("target_id", furl);
			}

			client.send(broadcast);

		} catch (Exception e) {

			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}

		try {
			IOSBroadcast broadcast = new IOSBroadcast(PropertiesUtil.getProperty("IOSUMAppKey"),
					PropertiesUtil.getProperty("IOSappMasterSecret"));

			broadcast.setAlert(fcontent);// 推送的消息
			broadcast.setBadge(1);// 设置角标
			broadcast.setSound("default");// 设置声音
			broadcast.setDescription(fdescription);
			// broadcast.setProductionMode();//生产环境 “上线后打开此注释切换生产模式”
			broadcast.setTestMode();// 测试环境

			if (furl.equals("")) {
				// System.out.println("当前字符串为空");
				broadcast.setCustomizedField("target_type", target_type);
				broadcast.setCustomizedField("target_id", ftargetObjectId);
			} else if (ftargetObjectId.equals("")) {
				// System.out.println("当前ObjectId为空");
				broadcast.setCustomizedField("target_type", target_type);
				broadcast.setCustomizedField("target_id", furl);
			}

			client.send(broadcast);

		} catch (Exception e) {

			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}

		pushDAO.saveUpdatePush(20, pushId);
	}

	/**
	 * 定时任务专用(单播推送IOS Android通用方法)
	 * 
	 * @param valueMap
	 */
	public void pushUnicast(String pushId, Integer ftargetType, String ftitle, String fcontent, String fdescription,
			String furl, String ftargetObjectId, String fdeviceToken) {

		String target_type = DictionaryUtil.getString(DictionaryUtil.PushLinkTargetType, ftargetType);

		try {
			AndroidUnicast unicast = new AndroidUnicast(PropertiesUtil.getProperty("umappkey"),
					PropertiesUtil.getProperty("appMasterSecret"));

			unicast.setDeviceToken(fdeviceToken);
			unicast.setTicker("您有新的通知消息");
			unicast.setTitle(ftitle);
			unicast.setText(fcontent);
			unicast.setDescription(fdescription);
			unicast.goAppAfterOpen();
			unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);

			unicast.setProductionMode();//生产环境 “上线后打开此注释切换生产模式”
			//unicast.setTestMode();// 测试环境
			if (furl.equals("")) {
				unicast.setExtraField("target_type", target_type);
				unicast.setExtraField("target_id", ftargetObjectId);
			} else if (ftargetObjectId.equals("")) {
				unicast.setExtraField("target_type", target_type);
				unicast.setExtraField("target_id", furl);
			}

			client.send(unicast);

		} catch (Exception e) {

			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}

		try {

			IOSUnicast unicast = new IOSUnicast(PropertiesUtil.getProperty("IOSUMAppKey"),
					PropertiesUtil.getProperty("IOSappMasterSecret"));

			unicast.setDeviceToken(fdeviceToken);
			unicast.setAlert(fcontent);
			unicast.setBadge(1);
			unicast.setSound("default");
			unicast.setDescription(fdescription);
			// unicast.setProductionMode();//生产环境 “上线后打开此注释切换生产模式”
			unicast.setTestMode();// 测试环境

			if (furl.equals("")) {
				// System.out.println("当前字符串为空");
				unicast.setCustomizedField("target_type", target_type);
				unicast.setCustomizedField("target_id", ftargetObjectId);
			} else if (ftargetObjectId.equals("")) {
				// System.out.println("当前ObjectId为空");
				unicast.setCustomizedField("target_type", target_type);
				unicast.setCustomizedField("target_id", furl);
			}

			client.send(unicast);

		} catch (Exception e) {

			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}

		pushDAO.saveUpdatePush(20, pushId);

	}

	/**
	 * 定时任务专用(组播推送IOS Android通用方法)
	 * 
	 * @param pushId
	 */
	public void pushGroupcast(String pushId, Integer ftargetType, String ftitle, String fcontent, String fdescription,
			String furl, String ftargetObjectId, Integer fdimension, Integer fuserTag, Integer fappVersion) {

		String target_type = DictionaryUtil.getString(DictionaryUtil.PushLinkTargetType, ftargetType);

		String dimension = DictionaryUtil.getString(DictionaryUtil.PushDimension, fdimension);// 基础维度
		if (dimension.equals("标签")) {
			dimension = "tag";
		} else if (dimension.equals("版本号")) {
			dimension = "app_version";
		} else if (dimension.equals("用户活跃度")) {
			dimension = "launch_from";
		} else if (dimension.equals("渠道")) {
			dimension = "channel";
		}

		String usertag = DictionaryUtil.getString(DictionaryUtil.PushUserTag, fuserTag);// 用户标签

		String appVersion = DictionaryUtil.getString(DictionaryUtil.AppVersion, fappVersion);// 用户版本号

		try {

			AndroidGroupcast groupcast = new AndroidGroupcast(PropertiesUtil.getProperty("umappkey"),
					PropertiesUtil.getProperty("appMasterSecret"));

			JSONObject filterJson = new JSONObject();
			JSONObject whereJson = new JSONObject();
			JSONArray tagArray = new JSONArray();
			JSONObject testTag = new JSONObject();
			JSONObject TestTag = new JSONObject();

			if (usertag.equals("")) {
				testTag.put(dimension, appVersion);
			} else {
				testTag.put(dimension, usertag);
			}

			// TestTag.put("channel", "www");
			tagArray.add(testTag);
			// tagArray.put(TestTag);

			whereJson.put("and", tagArray);
			filterJson.put("where", whereJson);

			groupcast.setFilter(filterJson);
			groupcast.setTicker("您有新的通知消息");
			groupcast.setTitle(ftitle);
			groupcast.setText(fcontent);
			groupcast.setDescription(fdescription);
			groupcast.goAppAfterOpen();
			groupcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
			// groupcast.setProductionMode();//生产环境 “上线后打开此注释切换生产模式”
			groupcast.setTestMode();// 测试环境

			if (furl.equals("")) {
				groupcast.setExtraField("target_type", target_type);
				groupcast.setExtraField("target_id", ftargetObjectId);
			} else if (ftargetObjectId.equals("")) {
				groupcast.setExtraField("target_type", target_type);
				groupcast.setExtraField("target_id", furl);
			}

			client.send(groupcast);

		} catch (Exception e) {

			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}

		try {

			IOSGroupcast groupcast = new IOSGroupcast(PropertiesUtil.getProperty("IOSUMAppKey"),
					PropertiesUtil.getProperty("IOSappMasterSecret"));

			JSONObject filterJson = new JSONObject();
			JSONObject whereJson = new JSONObject();
			JSONArray tagArray = new JSONArray();
			JSONObject testTag = new JSONObject();
			testTag.put("tag", "iostest");
			tagArray.add(testTag);
			whereJson.put("and", tagArray);
			filterJson.put("where", whereJson);

			groupcast.setFilter(filterJson);
			groupcast.setAlert(fcontent);
			groupcast.setBadge(1);
			groupcast.setSound("default");
			groupcast.setDescription(fdescription);
			// groupcast.setProductionMode();//生产环境 “上线后打开此注释切换生产模式”
			groupcast.setTestMode();// 测试环境

			if (furl.equals("")) {
				groupcast.setCustomizedField("target_type", target_type);
				groupcast.setCustomizedField("target_id", ftargetObjectId);
			} else if (ftargetObjectId.equals("")) {
				groupcast.setCustomizedField("target_type", target_type);
				groupcast.setCustomizedField("target_id", furl);
			}

			client.send(groupcast);

		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}

		pushDAO.saveUpdatePush(20, pushId);

	}

	/**
	 * 用于给用户打标签 并跟友盟同步上传标签
	 * 
	 * @param ticket
	 *            用户唯一标识
	 * @param ftag
	 *            用户标签为Integer类型需要在字典里面读取 字典值为48
	 * @param fdevice
	 *            设备唯一编码
	 * @param clientType
	 *            终端设备 2.ios 3.安卓
	 * @param device_tokens
	 *            友盟的device_tokens
	 * @param foperator
	 *            为0则为系统操作
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ResponseDTO addCustomerTags(String customerId, Integer ftag, String fdevice, Integer clientType,
			String deviceTokens) throws Exception {

		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(customerId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("请填写customerId！");
			return responseDTO;
		}
		if (ftag == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请填写ftag！");
			return responseDTO;
		}
		if (StringUtils.isBlank(fdevice)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请填写fdevice！");
			return responseDTO;
		}
		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请填写clientType！");
			return responseDTO;
		}
		if (StringUtils.isBlank(deviceTokens)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请填写deviceTokens！");
			return responseDTO;
		}

		// String customerId = czyhInterfaceService.getCustomerIdByTicket(ticket,
		// clientType);//转换ticket为用户id
		Date now = new Date();
		TCustomerTag tcustomerTag = new TCustomerTag();
		tcustomerTag.setFcustomerId(customerId);
		tcustomerTag.setFtag(ftag);
		tcustomerTag.setFoperator(0L);
		tcustomerTag.setFcreateTime(now);
		customerTagDAO.save(tcustomerTag);

		TPushToken tpushToken = new TPushToken();
		tpushToken.setFcustomerId(customerId);
		tpushToken.setFtoken(deviceTokens);
		tpushToken.setFdevice(fdevice);
		tpushToken.setFcreateTime(now);
		pushtokenDAO.save(tpushToken);

		if (clientType == 3) {
			responseDTO = client.AddTags(PropertiesUtil.getProperty("umappkey"),
					PropertiesUtil.getProperty("appMasterSecret"),
					DictionaryUtil.getString(DictionaryUtil.PushUserTag, ftag), deviceTokens);
		} else {
			responseDTO = client.AddTags(PropertiesUtil.getProperty("IOSUMAppKey"),
					PropertiesUtil.getProperty("IOSappMasterSecret"),
					DictionaryUtil.getString(DictionaryUtil.PushUserTag, ftag), deviceTokens);
		}

		return responseDTO;
	}

	/**
	 * 用于修改用户标签 并跟友盟同步修改上传标签
	 * 
	 * @param ticket
	 *            用户唯一标识
	 * @param ftag
	 *            用户标签为Integer类型需要在字典里面读取 字典值为48
	 * @param fdevice
	 *            设备唯一编码
	 * @param clientType
	 *            终端设备 2.ios 3.安卓
	 * @param device_tokens
	 *            友盟的device_tokens
	 * @param foperator
	 *            为0则为系统操作
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ResponseDTO updateCustomerTags(String customerId, Integer ftag, String fdevice, Integer clientType,
			String deviceTokens) throws Exception {

		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(customerId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("请填写customerId！");
			return responseDTO;
		}
		if (ftag == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请填写ftag！");
			return responseDTO;
		}
		if (StringUtils.isBlank(fdevice)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请填写fdevice！");
			return responseDTO;
		}
		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请填写clientType！");
			return responseDTO;
		}
		if (StringUtils.isBlank(deviceTokens)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请填写deviceTokens！");
			return responseDTO;
		}

		// String customerId = czyhInterfaceService.getCustomerIdByTicket(ticket,
		// clientType);//转换ticket为用户id
		Date now = new Date();
		TCustomerTag customerTag = customerTagDAO.findTCustomerTag(customerId, ftag);
		if (customerTag == null) {
			TCustomerTag tcustomerTag = new TCustomerTag();
			tcustomerTag.setFcustomerId(customerId);
			tcustomerTag.setFtag(ftag);
			tcustomerTag.setFoperator(0L);
			tcustomerTag.setFcreateTime(now);
			customerTagDAO.save(tcustomerTag);

		}

		TPushToken tpushToken = pushtokenDAO.getByFcustomerId(customerId);
		if(tpushToken == null){
			tpushToken = new TPushToken();
			tpushToken.setFcustomerId(customerId);
			tpushToken.setFcreateTime(now);
		}
		tpushToken.setFtoken(deviceTokens);
		tpushToken.setFdevice(fdevice);
		tpushToken.setFupdateTime(now);
		pushtokenDAO.save(tpushToken);

		if (clientType == 3) {
			responseDTO = client.AddTags(PropertiesUtil.getProperty("umappkey"),
					PropertiesUtil.getProperty("appMasterSecret"),
					DictionaryUtil.getString(DictionaryUtil.PushUserTag, ftag, Constant.englishLanguage), deviceTokens);
		} else {
			responseDTO = client.AddTags(PropertiesUtil.getProperty("IOSUMAppKey"),
					PropertiesUtil.getProperty("IOSappMasterSecret"),
					DictionaryUtil.getString(DictionaryUtil.PushUserTag, ftag, Constant.englishLanguage), deviceTokens);
		}

		return responseDTO;
	}

}