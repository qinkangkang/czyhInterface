package com.innee.czyhInterface.util.sms;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.innee.czyhInterface.util.PropertiesUtil;
import com.taobao.api.DefaultTaobaoClient;

/**
 * 253短信发送接口
 * 
 * @author 金圣智
 *
 */
public class SmsUtil2 {

	private static Logger logger = LoggerFactory.getLogger(SmsUtil2.class);

	public static final int CheckCodeSms = 1;

	public static final int PaySuccessSms = 2;

	public static final String SmsUrl = PropertiesUtil.getProperty("SmsUrl");

	public static final String SmsUn = PropertiesUtil.getProperty("SmsUn");

	public static final String SmsPw = PropertiesUtil.getProperty("SmsPw");

	public static final String SmsRd = "1";

	public static final String SmsEx = "";

	public static boolean LoginPwdSwitch = true;

	/**
	 * 短信发送主题类
	 * 
	 * @param url
	 * @param un
	 * @param pw
	 * @param phone
	 * @param msg
	 * @param rd
	 * @param ex
	 * @return
	 * @throws Exception
	 */
	public static SmsResult SendSms(String phone, int smsType, String str) {
		SmsResult smsResult = new SmsResult();
		HttpClient client = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
		GetMethod method = new GetMethod();

		try {
			String msg = null;
			if (smsType == CheckCodeSms) {
				msg = SmsUtil2.MsgSms(CheckCodeSms, str);
			} else if (smsType == PaySuccessSms) {
				msg = SmsUtil2.MsgSms(PaySuccessSms, str);
			}
			try {
				URI base = new URI(SmsUrl, false);
				method.setURI(new URI(base, "send", false));
				method.setQueryString(
						new NameValuePair[] { new NameValuePair("un", SmsUn), new NameValuePair("pw", SmsPw),
								new NameValuePair("phone", phone), new NameValuePair("rd", SmsRd),
								new NameValuePair("msg", msg), new NameValuePair("ex", SmsEx), });
				int result = client.executeMethod(method);
				if (result == HttpStatus.SC_OK) {
					InputStream in = method.getResponseBodyAsStream();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = in.read(buffer)) != -1) {
						baos.write(buffer, 0, len);
					}
					smsResult.setContent(URLDecoder.decode(baos.toString(), "UTF-8"));
//					smsResult.setContent(msg);
					smsResult.setSuccess(true);
					return smsResult;

				} else {
					throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		} finally {
			method.releaseConnection();
		}
		return smsResult;
	}

	public static String MsgSms(int smsType, String str) {
		StringBuilder msg = new StringBuilder();
		if (smsType == CheckCodeSms) {
			msg.append("【查找优惠】您的验证码为:").append(str).append(",请在有效时间内输入。为保障您的账号安全，请勿将验证码短信转发给他人。").toString();
		} else if (smsType == PaySuccessSms) {
			msg.append("【查找优惠】您的订单号为:").append(str).append(",请在有效时间内输入。为保障您的账号安全，请勿将验证码短信转发给他人。").toString();
		}
		return msg.toString();
	}

	public static void main(String[] args) {

		String url = "http://sms.253.com/msg/send";// 应用地址
		String un = "N9446605";// 账号
		String pw = "009728c0";// 密码
		String phone = "15940336981";// 手机号码，多个号码使用","分割
		String msg = "【查找优惠】您的验证码为，请在有效时间内输入。为保障您的账号安全，请勿将验证码短信转发给他人。";// 短信内容
		String rd = "1";// 是否需要状态报告，需要1，不需要0
		String ex = "2826";// 扩展码

		try {
			SmsUtil2 smsUtil2 = new SmsUtil2();
			SmsResult smsResult = new SmsResult();
			smsResult = smsUtil2.SendSms(phone, 1, "s");
			System.out.println(smsResult.getContent());
			// TODO 处理返回值,参见HTTP协议文档
		} catch (Exception e) {
			// TODO 处理异常
			e.printStackTrace();
		}
	}
}