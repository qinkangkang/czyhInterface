package com.innee.czyhInterface.dto;

import java.io.Serializable;

public class ArticleRecommendDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// 活动ID
	private String articleId;

	// 活动推荐总数
	private long recommend = 0L;

	// 数值是否变化标志位
	private boolean change = false;

	public long getRecommend() {
		return recommend;
	}

	public void setRecommend(long recommend) {
		this.recommend = recommend;
	}

	public boolean isChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}

	public void addOne() {
		recommend += 1;
		this.change = true;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
}