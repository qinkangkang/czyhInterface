package com.innee.czyhInterface.util.push;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.innee.czyhInterface.util.PropertiesUtil;
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


public class Demo {
	private String appkey = "56f0b604e0f55ac2fe0015f7";
	private String appMasterSecret = "jkfrt7b1usedshhkm6lwj8j2ugholhkd";
	private String timestamp = null;
	private PushClient client = new PushClient();

	public Demo(String key, String secret) {
		try {
			appkey = key;
			appMasterSecret = secret;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void sendAndroidBroadcast() throws Exception {
		AndroidBroadcast broadcast = new AndroidBroadcast(PropertiesUtil.getProperty("umappkey"),
				PropertiesUtil.getProperty("appMasterSecret"));
		broadcast.setTicker("我是消息描述");
		broadcast.setTitle("我是测试消息测试消息");
		broadcast.setText("富文本测试消息");
		broadcast.setDescription("全局广播消息");
		broadcast.goAppAfterOpen();
		broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		broadcast.setProductionMode();
		// Set customized fields
		broadcast.setExtraField("test", "helloworld");
		client.send(broadcast);
		System.out.println(broadcast + "我是小王八");
	}

	public void sendAndroidUnicast() throws Exception {
		AndroidUnicast unicast = new AndroidUnicast(PropertiesUtil.getProperty("umappkey"),
				PropertiesUtil.getProperty("appMasterSecret"));
		// TODO Set your device token
		unicast.setDeviceToken("AvdoA3MOpiUfD1JG5Z0DI3wIBe3rTb_dikK_nQ83vq5t");
		unicast.setTicker("安卓客户端测试单播");
		unicast.setTitle("安卓客户端测试单播title");
		unicast.setText("安卓客户端测试单播test");
		unicast.setDescription("unicast");
		unicast.goAppAfterOpen();
		unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		unicast.setProductionMode();
		// Set customized fields
		unicast.setExtraField("test", "helloworld");
		client.send(unicast);
	}

	public void sendAndroidGroupcast() throws Exception {
		AndroidGroupcast groupcast = new AndroidGroupcast(PropertiesUtil.getProperty("umappkey"),
				PropertiesUtil.getProperty("appMasterSecret"));
		JSONObject filterJson = new JSONObject();
		JSONObject whereJson = new JSONObject();
		JSONArray tagArray = new JSONArray();
		JSONObject testTag = new JSONObject();
		JSONObject TestTag = new JSONObject();

		testTag.put("app_version", "1.0.3");

		TestTag.put("channel", "www");

		tagArray.add(testTag);
		tagArray.add(TestTag);

		whereJson.put("and", tagArray);
		filterJson.put("where", whereJson);
		System.out.println(filterJson.toString() + "这是测试");

		groupcast.setFilter(filterJson);
		groupcast.setTicker("Android groupcast ticker");
		groupcast.setTitle("中文的title");
		groupcast.setText("Android groupcast text");
		groupcast.setDescription("测试组播消息");
		groupcast.goAppAfterOpen();
		groupcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		groupcast.setProductionMode();
		client.send(groupcast);
	}

	public void sendAndroidCustomizedcast() throws Exception {

		AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(PropertiesUtil.getProperty("umappkey"),
				PropertiesUtil.getProperty("appMasterSecret"));

		customizedcast.setAlias("alias", "alias_type");
		customizedcast.setTicker("Android customizedcast ticker");
		customizedcast.setTitle("中文的title");
		customizedcast.setText("Android customizedcast text");
		customizedcast.setDescription("测试标题");
		customizedcast.goAppAfterOpen();
		customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);

		customizedcast.setProductionMode();
		client.send(customizedcast);
	}

	public void sendAndroidCustomizedcastFile() throws Exception {
		AndroidCustomizedcast customizedcast = new AndroidCustomizedcast(PropertiesUtil.getProperty("umappkey"),
				PropertiesUtil.getProperty("appMasterSecret"));
		// TODO Set your alias here, and use comma to split them if there are
		// multiple alias.
		// And if you have many alias, you can also upload a file containing
		// these alias, then
		// use file_id to send customized notification.
		String fileId = client.uploadContents(PropertiesUtil.getProperty("umappkey"),
				PropertiesUtil.getProperty("appMasterSecret"), "aa" + "\n" + "bb" + "\n" + "alias");
		customizedcast.setFileId(fileId, "alias_type");
		customizedcast.setTicker("Android customizedcast ticker");
		customizedcast.setTitle("中文的title");
		customizedcast.setText("Android customizedcast text");
		customizedcast.goAppAfterOpen();
		customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device.
		// For how to register a test device, please see the developer doc.
		customizedcast.setProductionMode();
		client.send(customizedcast);
	}

	public void sendAndroidFilecast() throws Exception {
		AndroidFilecast filecast = new AndroidFilecast(appkey, appMasterSecret);
		// TODO upload your device tokens, and use '\n' to split them if there
		// are multiple tokens
		String fileId = client.uploadContents(appkey, appMasterSecret, "aa" + "\n" + "bb");
		filecast.setFileId(fileId);
		filecast.setTicker("Android filecast ticker");
		filecast.setTitle("中文的title");
		filecast.setText("Android filecast text");
		filecast.goAppAfterOpen();
		filecast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		client.send(filecast);
	}

	public void sendIOSBroadcast() throws Exception {
		IOSBroadcast broadcast = new IOSBroadcast(PropertiesUtil.getProperty("IOSUMAppKey"),
				PropertiesUtil.getProperty("IOSappMasterSecret"));

		broadcast.setAlert("IOS 广播测试");
		broadcast.setBadge(0);
		broadcast.setSound("default");
		// TODO set 'production_mode' to 'true' if your app is under production
		// mode
		broadcast.setTestMode();
		// Set customized fields
		broadcast.setCustomizedField("test", "helloworld");
		client.send(broadcast);
	}

	public void sendIOSUnicast() throws Exception {
		IOSUnicast unicast = new IOSUnicast(appkey, appMasterSecret);
		// TODO Set your device token
		unicast.setDeviceToken("xx");
		unicast.setAlert("IOS 单播测试");
		unicast.setBadge(0);
		unicast.setSound("default");
		// TODO set 'production_mode' to 'true' if your app is under production
		// mode
		unicast.setTestMode();
		// Set customized fields
		unicast.setCustomizedField("test", "helloworld");
		client.send(unicast);
	}

	public void sendIOSGroupcast() throws Exception {
		IOSGroupcast groupcast = new IOSGroupcast(appkey, appMasterSecret);
		/*
		 * TODO Construct the filter condition: "where": { "and": [
		 * {"tag":"iostest"} ] }
		 */
		JSONObject filterJson = new JSONObject();
		JSONObject whereJson = new JSONObject();
		JSONArray tagArray = new JSONArray();
		JSONObject testTag = new JSONObject();
		testTag.put("tag", "iostest");
		tagArray.add(testTag);
		whereJson.put("and", tagArray);
		filterJson.put("where", whereJson);
		// System.out.println(filterJson.toString());

		// Set filter condition into rootJson
		groupcast.setFilter(filterJson);
		groupcast.setAlert("IOS 组播测试");
		groupcast.setBadge(0);
		groupcast.setSound("default");
		// TODO set 'production_mode' to 'true' if your app is under production
		// mode
		groupcast.setTestMode();
		client.send(groupcast);
	}

	public void sendIOSCustomizedcast() throws Exception {
		IOSCustomizedcast customizedcast = new IOSCustomizedcast(appkey, appMasterSecret);
		// TODO Set your alias and alias_type here, and use comma to split them
		// if there are multiple alias.
		// And if you have many alias, you can also upload a file containing
		// these alias, then
		// use file_id to send customized notification.
		customizedcast.setAlias("alias", "alias_type");
		customizedcast.setAlert("IOS 个性化测试");
		customizedcast.setBadge(0);
		customizedcast.setSound("default");
		// TODO set 'production_mode' to 'true' if your app is under production
		// mode
		customizedcast.setTestMode();
		client.send(customizedcast);
	}

	public void sendIOSFilecast() throws Exception {
		IOSFilecast filecast = new IOSFilecast(appkey, appMasterSecret);
		// TODO upload your device tokens, and use '\n' to split them if there
		// are multiple tokens
		String fileId = client.uploadContents(appkey, appMasterSecret, "aa" + "\n" + "bb");
		filecast.setFileId(fileId);
		filecast.setAlert("IOS 文件播测试");
		filecast.setBadge(0);
		filecast.setSound("default");
		// TODO set 'production_mode' to 'true' if your app is under production
		// mode
		filecast.setTestMode();
		client.send(filecast);
	}

	public static void main(String[] args) {
		// TODO set your appkey and master secret here
		Demo demo = new Demo("your appkey", "the app master secret");
		try {
			demo.sendAndroidBroadcast();
			/*
			 * TODO these methods are all available, just fill in some fields
			 * and do the test demo.sendAndroidCustomizedcastFile();
			 * demo.sendAndroidBroadcast(); demo.sendAndroidGroupcast();
			 * demo.sendAndroidCustomizedcast(); demo.sendAndroidFilecast();
			 * 
			 * demo.sendIOSBroadcast(); demo.sendIOSUnicast();
			 * demo.sendIOSGroupcast(); demo.sendIOSCustomizedcast();
			 * demo.sendIOSFilecast();
			 */
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
