package com.innee.czyhInterface.dto.m;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class WxMpUserDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String openid;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String nickname;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String country;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String province;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String city;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String headimgurl;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String unionid;
	
	private Integer clientType;
	
	private Integer sex;
	
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public Integer getClientType() {
		return clientType;
	}
	public void setClientType(Integer clientType) {
		this.clientType = clientType;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	
}
