package com.innee.czyhInterface.dto.m;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class CustomerBabyDTO implements Serializable {

	private static final long serialVersionUID = 1L;

//	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
//	private String customerId;
	
//	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
//	private String name;
	
	private int age;
	
	private int sex;
	
//	private int order;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String flikeType;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getFlikeType() {
		return flikeType;
	}

	public void setFlikeType(String flikeType) {
		this.flikeType = flikeType;
	}
	
}