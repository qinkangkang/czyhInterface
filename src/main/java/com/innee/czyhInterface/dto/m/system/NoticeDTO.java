package com.innee.czyhInterface.dto.m.system;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.dto.m.EventSpecDTO;
import com.innee.czyhInterface.entity.TDictionaryRegion;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class NoticeDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String noticeName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String noticeUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String noticeTitle;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String noticeTime;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String noticeId;
	
	private Integer noticeType;
	
	public String getNoticeName() {
		return noticeName;
	}

	public void setNoticeName(String noticeName) {
		this.noticeName = noticeName;
	}

	public String getNoticeUrl() {
		return noticeUrl;
	}

	public void setNoticeUrl(String noticeUrl) {
		this.noticeUrl = noticeUrl;
	}

	public String getNoticeTitle() {
		return noticeTitle;
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public Integer getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(Integer noticeType) {
		this.noticeType = noticeType;
	}

	public String getNoticeTime() {
		return noticeTime;
	}

	public void setNoticeTime(String noticeTime) {
		this.noticeTime = noticeTime;
	}

}