package com.innee.czyhInterface.dto.m;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.dto.AndroidUrlDTO;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class AppVersionDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int appType = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String releaseDate;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String versionNum;

	private long versionValue = 0L;

	private boolean ifUpdate = false;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String description;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private AndroidUrlDTO appUrls;

	public int getAppType() {
		return appType;
	}

	public void setAppType(int appType) {
		this.appType = appType;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}

	public long getVersionValue() {
		return versionValue;
	}

	public void setVersionValue(long versionValue) {
		this.versionValue = versionValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AndroidUrlDTO getAppUrls() {
		return appUrls;
	}

	public void setAppUrls(AndroidUrlDTO appUrls) {
		this.appUrls = appUrls;
	}

	public boolean isIfUpdate() {
		return ifUpdate;
	}

	public void setIfUpdate(boolean ifUpdate) {
		this.ifUpdate = ifUpdate;
	}

}