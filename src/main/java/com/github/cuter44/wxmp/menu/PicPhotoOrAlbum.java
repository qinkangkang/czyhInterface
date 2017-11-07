package com.github.cuter44.wxmp.menu;

import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSONObject;

public class PicPhotoOrAlbum extends JSONObject implements SubMenuElement {
	public static final int KEY_BYTE_SIZE_CONSTRAINT = 128;

	public static final String KEY_NAME = "name";
	public static final String KEY_KEY = "key";

	public PicPhotoOrAlbum() {
		this.put("type", "pic_photo_or_album");

		return;
	}

	public PicPhotoOrAlbum(String name, String key) {
		this.put(KEY_NAME, name);
		this.setKey(key);

		return;
	}

	public PicPhotoOrAlbum setName(String name) {
		this.put(KEY_NAME, name);

		return (this);
	}

	public PicPhotoOrAlbum setKey(String key) {
		try {
			if (key.getBytes("UTF-8").length > KEY_BYTE_SIZE_CONSTRAINT)
				throw (new IllegalArgumentException(
						"Key length limit " + KEY_BYTE_SIZE_CONSTRAINT + " bytes excessed."));
		} catch (UnsupportedEncodingException ex) {
			// never occur
			ex.printStackTrace();
		}

		this.put(KEY_KEY, key);

		return (this);
	}

	@Override
	public String getName() {
		return (this.getString(KEY_NAME));
	}
}
