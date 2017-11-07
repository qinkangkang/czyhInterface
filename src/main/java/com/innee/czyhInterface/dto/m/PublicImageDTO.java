package com.innee.czyhInterface.dto.m;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class PublicImageDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String original;// 原图

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String bigPicture;// 大图

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String mediumPicture;// 中图

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String smallPicture;// 小图

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getBigPicture() {
		return bigPicture;
	}

	public void setBigPicture(String bigPicture) {
		this.bigPicture = bigPicture;
	}

	public String getMediumPicture() {
		return mediumPicture;
	}

	public void setMediumPicture(String mediumPicture) {
		this.mediumPicture = mediumPicture;
	}

	public String getSmallPicture() {
		return smallPicture;
	}

	public void setSmallPicture(String smallPicture) {
		this.smallPicture = smallPicture;
	}

}