package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class StatementSimpleDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String statementId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String statementNum;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String startTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String endTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String createTime;

	private BigDecimal total = BigDecimal.ZERO;

	public String getStatementId() {
		return statementId;
	}

	public void setStatementId(String statementId) {
		this.statementId = statementId;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStatementNum() {
		return statementNum;
	}

	public void setStatementNum(String statementNum) {
		this.statementNum = statementNum;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}