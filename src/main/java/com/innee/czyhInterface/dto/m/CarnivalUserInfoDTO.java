package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class CarnivalUserInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String userId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String name;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String headimgUrl;
	
	private Integer age;
	
	private Integer sex;

	private int ranking = 0;// 排名

	private int sweepCount = 0;// 扫码数量

	private int medalCount = 0;// 勋章数量

	private int credit = 0;// 用户积分

	private Date createTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String ticket;

	private boolean change = false;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHeadimgUrl() {
		return headimgUrl;
	}

	public void setHeadimgUrl(String headimgUrl) {
		this.headimgUrl = headimgUrl;
	}

	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public int getSweepCount() {
		return sweepCount;
	}

	public void setSweepCount(int sweepCount) {
		this.sweepCount = sweepCount;
	}

	public int getMedalCount() {
		return medalCount;
	}

	public void setMedalCount(int medalCount) {
		this.medalCount = medalCount;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public boolean isChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

}