package com.innee.czyhInterface.util;

import org.apache.commons.lang3.StringUtils;

import com.innee.czyhInterface.exception.ServiceException;



public class HeadImageUtil {

	public static String getHeadImage(String headimgurl, int size) {
		if (StringUtils.isBlank(headimgurl)) {
			return Constant.defaultHeadImgUrl;
		}
		if (headimgurl.startsWith("http://wx.qlogo.cn/mmopen")) {
			if (size != 0 && size != 46 && size != 64 && size != 96 && size != 132) {
				throw new ServiceException("获取微信头像图片出错，获取头像图片的尺寸必须是0、46、64、96、132数值可选，0代表640*640正方形头像图片");
			}
			return new StringBuilder().append(headimgurl.substring(0, headimgurl.length() - 1)).append(size).toString();
		} else {
			return headimgurl;
		}
	}

	public static void main(String[] args) {
		System.out.println(HeadImageUtil.getHeadImage(
				"http://wx.qlogo.cn/mmopen/pibnjufDKrn8rXWWQrAl6viaKictlMhZFicghiakqH1cAhslJriaYH18U4W4r4tPE3M8E61iaXVV6kbIa0iboV5smP8sdw/0",
				46));
	}
}