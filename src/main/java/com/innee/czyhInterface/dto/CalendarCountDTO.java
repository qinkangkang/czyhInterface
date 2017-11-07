package com.innee.czyhInterface.dto;

import java.io.Serializable;

public class CalendarCountDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	public CalendarCountDTO() {
		min = Integer.MAX_VALUE;
		max = Integer.MIN_VALUE;
	}

	// 定义场次起止日期的最小值和最大值，为了去日历表中获取日历记录为条件
	private int min = 0;

	private int max = 0;

	private int cityId = 0;

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

}
