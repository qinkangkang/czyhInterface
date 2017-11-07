package com.innee.czyhInterface.dto.m;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class ArticleDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String articleId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String title;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String type;

	private int typeColor = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String imageUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String brief;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String detailHtmlUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String createTime;

	private long recommend = 0L;

	private long comment = 0L;
	
	private int favoriteCount = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String address;

	private boolean favorite = false;

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getDetailHtmlUrl() {
		return detailHtmlUrl;
	}

	public void setDetailHtmlUrl(String detailHtmlUrl) {
		this.detailHtmlUrl = detailHtmlUrl;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public long getRecommend() {
		return recommend;
	}

	public void setRecommend(long recommend) {
		this.recommend = recommend;
	}

	public long getComment() {
		return comment;
	}

	public void setComment(long comment) {
		this.comment = comment;
	}

	public int getTypeColor() {
		return typeColor;
	}

	public void setTypeColor(int typeColor) {
		this.typeColor = typeColor;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

}