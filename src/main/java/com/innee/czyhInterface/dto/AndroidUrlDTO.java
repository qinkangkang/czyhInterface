package com.innee.czyhInterface.dto;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class AndroidUrlDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String www;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String ch01;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String ch02;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String ch03;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String ch04;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String prev;

	public String getWww() {
		return www;
	}

	public void setWww(String www) {
		this.www = www;
	}

	public String getCh01() {
		return ch01;
	}

	public void setCh01(String ch01) {
		this.ch01 = ch01;
	}

	public String getCh02() {
		return ch02;
	}

	public void setCh02(String ch02) {
		this.ch02 = ch02;
	}

	public String getCh03() {
		return ch03;
	}

	public void setCh03(String ch03) {
		this.ch03 = ch03;
	}

	public String getCh04() {
		return ch04;
	}

	public void setCh04(String ch04) {
		this.ch04 = ch04;
	}

	public String getPrev() {
		return prev;
	}

	public void setPrev(String prev) {
		this.prev = prev;
	}

}