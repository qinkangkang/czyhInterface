package com.innee.czyhInterface.dto.m.category;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class CategoryDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int categoryId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String imageA;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String imageB;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String categoryAName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String updateTime;

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getImageA() {
		return imageA;
	}

	public void setImageA(String imageA) {
		this.imageA = imageA;
	}

	public String getImageB() {
		return imageB;
	}

	public void setImageB(String imageB) {
		this.imageB = imageB;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCategoryAName() {
		return categoryAName;
	}

	public void setCategoryAName(String categoryAName) {
		this.categoryAName = categoryAName;
	}

}