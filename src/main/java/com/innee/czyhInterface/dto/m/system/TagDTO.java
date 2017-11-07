package com.innee.czyhInterface.dto.m.system;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.dto.m.EventSpecDTO;
import com.innee.czyhInterface.entity.TDictionaryRegion;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class TagDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int tagId = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String tagName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String stagImageUrl;

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getStagImageUrl() {
		return stagImageUrl;
	}

	public void setStagImageUrl(String stagImageUrl) {
		this.stagImageUrl = stagImageUrl;
	}

	
}