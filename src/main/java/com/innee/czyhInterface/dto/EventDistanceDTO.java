package com.innee.czyhInterface.dto;

import java.io.Serializable;

public class EventDistanceDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// 活动ID
	private String eventId;

	// 活动百度gps坐标
	private String gps;

	// 活动百度gps经度坐标
	private String gpsLng;

	// 活动百度gps纬度坐标
	private String gpsLat;

	// 活动的距离米数
	private int distance = 0;

	// 活动的距离信息（3.2km、343m）
	private String distanceString;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getDistanceString() {
		return distanceString;
	}

	public void setDistanceString(String distanceString) {
		this.distanceString = distanceString;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	public String getGpsLng() {
		return gpsLng;
	}

	public void setGpsLng(String gpsLng) {
		this.gpsLng = gpsLng;
	}

	public String getGpsLat() {
		return gpsLat;
	}

	public void setGpsLat(String gpsLat) {
		this.gpsLat = gpsLat;
	}

}