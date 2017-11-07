package com.innee.czyhInterface.dto.m.system;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.dto.m.EventSpecDTO;
import com.innee.czyhInterface.entity.TDictionaryRegion;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class DictionaryDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String systemModel;

	private int systemId = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String systemName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String systemImageUrl;

	public String getSystemModel() {
		return systemModel;
	}

	public void setSystemModel(String systemModel) {
		this.systemModel = systemModel;
	}

	public int getSystemId() {
		return systemId;
	}

	public void setSystemId(int systemId) {
		this.systemId = systemId;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemImageUrl() {
		return systemImageUrl;
	}

	public void setSystemImageUrl(String systemImageUrl) {
		this.systemImageUrl = systemImageUrl;
	}

}