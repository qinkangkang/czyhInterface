package com.innee.czyhInterface.dto.m;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class StatementDetailDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String statementId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String detailId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String statementNum;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String startTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String endTime;

	private int group = 0;

	private int inOut = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String typeString;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String title;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String createTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String remark;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String amount;

	private int type = 0;

	public String getStatementId() {
		return statementId;
	}

	public void setStatementId(String statementId) {
		this.statementId = statementId;
	}

	public String getDetailId() {
		return detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public String getTypeString() {
		return typeString;
	}

	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public int getInOut() {
		return inOut;
	}

	public void setInOut(int inOut) {
		this.inOut = inOut;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatementNum() {
		return statementNum;
	}

	public void setStatementNum(String statementNum) {
		this.statementNum = statementNum;
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

}