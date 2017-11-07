package com.github.cuter44.wxmp.reqs;

import java.io.IOException;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.cuter44.wxmp.resps.MessageCustomSendResponse;

/**
 * 客服接口-发消息(文本) <br />
 * <a href=
 * "http://mp.weixin.qq.com/wiki/1/70a29afed17f56d537c833f89be979c9.html#.E5.AE.A2.E6.9C.8D.E6.8E.A5.E5.8F.A3-.E5.8F.91.E6.B6.88.E6.81.AF">
 * ref ↗</a> <br />
 * 支持 &lt;a&gt; 标签 <br />
 * 
 * <pre style="font-size:12px">
    参数说明
    access_token    是  调用接口凭证
    touser          是  普通用户openid
    msgtype         是  消息类型，文本为text<del>，图片为image，语音为voice，视频消息为video，音乐消息为music，图文消息为news，卡券为wxcard</del>
    content         是  文本消息内容
    <del>media_id        是  发送的图片/语音/视频的媒体ID</del>
    <del>thumb_media_id  是  缩略图的媒体ID</del>
    <del>title           否  图文消息/视频消息/音乐消息的标题</del>
    <del>description     否  图文消息/视频消息/音乐消息的描述</del>
    <del>musicurl        是  音乐链接</del>
    <del>hqmusicurl      是  高品质音乐链接，wifi环境优先使用该链接播放音乐</del>
    <del>url             否  图文消息被点击后跳转的链接</del>
    <del>picurl          否  图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80</del>
 * </pre>
 * 
 * This is a general super class for sending message, use corresponding
 * sub-class for specific message type
 */
public class MessageCustomSendText extends MessageCustomSend {
	// KEYS
	public static final String KEY_CONTENT = "content";

	public static final JSONObject BODY_SCHEMA = JSON.parseObject("{" + "'properties':{" + "'touser':{'type':'string'},"
			+ "'msgtype':{'type':'string'}," + "'text':{" + "'type':'object'," + "'schema':{" + "'properties':{"
			+ "'content':{'type':'string'}" + "} } } } }");

	// CONSTRUCT
	public MessageCustomSendText(Properties prop) {
		super(prop);

		super.setProperty(KEY_MSGTYPE, "text");

		return;
	}

	// BUILD
	@Override
	public MessageCustomSendText build() {
		this.jsonBody = super.buildJSONBody(BODY_SCHEMA, this.conf);

		return (this);
	}

	// EXECUTE
	@Override
	public MessageCustomSendResponse execute() throws IOException {
		String url = URL_API_BASE + "?" + super.toQueryString(KEYS_PARAM);
		String body = this.jsonBody.toString();

		String respJson = super.executePostJSON(url, body);

		return (new MessageCustomSendResponse(respJson));
	}

	// MISC
	public MessageCustomSendText setContent(String content) {
		super.setProperty(KEY_CONTENT, content);

		return (this);
	}

}
