package com.innee.czyhInterface.dto.m.system;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.dto.m.EventSpecDTO;
import com.innee.czyhInterface.entity.TDictionaryRegion;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class ColumnDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String columnUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String columnName;

	public String getColumnUrl() {
		return columnUrl;
	}

	public void setColumnUrl(String columnUrl) {
		this.columnUrl = columnUrl;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

}