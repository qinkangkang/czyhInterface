package com.innee.czyhInterface.dto.m.category;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class SecondaryCategoryDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int secondaryCategoryId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String categoryName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String image;

	private int parentId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String updateTime;

	public int getSecondaryCategoryId() {
		return secondaryCategoryId;
	}

	public void setSecondaryCategoryId(int secondaryCategoryId) {
		this.secondaryCategoryId = secondaryCategoryId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}