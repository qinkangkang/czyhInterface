package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class CommentDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String commentId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String objectId;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String objectName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String commenterLevel;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String content;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String commentImageUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String commentDate;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String commenterId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String commenterName;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String commenterLogoUrl;

	private List<PublicImageDTO> imageUrlPathList;

	private int type = 0;

	private int mark = 0;

	private long recommend = 0L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String typeString;
	
	private boolean replied = false;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String userName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String userLogoUrl;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String replyTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String reply;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String orderDate;

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCommenterId() {
		return commenterId;
	}

	public void setCommenterId(String commenterId) {
		this.commenterId = commenterId;
	}

	public String getCommenterName() {
		return commenterName;
	}

	public void setCommenterName(String commenterName) {
		this.commenterName = commenterName;
	}

	public String getCommenterLogoUrl() {
		return commenterLogoUrl;
	}

	public void setCommenterLogoUrl(String commenterLogoUrl) {
		this.commenterLogoUrl = commenterLogoUrl;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public String getTypeString() {
		return typeString;
	}

	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}

	public String getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}

	public String getCommentImageUrl() {
		return commentImageUrl;
	}

	public void setCommentImageUrl(String commentImageUrl) {
		this.commentImageUrl = commentImageUrl;
	}

	public long getRecommend() {
		return recommend;
	}

	public void setRecommend(long recommend) {
		this.recommend = recommend;
	}

	public List<PublicImageDTO> getImageUrlPathList() {
		return imageUrlPathList;
	}

	public void setImageUrlPathList(List<PublicImageDTO> imageUrlPathList) {
		this.imageUrlPathList = imageUrlPathList;
	}

	public boolean isReplied() {
		return replied;
	}

	public void setReplied(boolean replied) {
		this.replied = replied;
	}

	public String getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserLogoUrl() {
		return userLogoUrl;
	}

	public void setUserLogoUrl(String userLogoUrl) {
		this.userLogoUrl = userLogoUrl;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getCommenterLevel() {
		return commenterLevel;
	}

	public void setCommenterLevel(String commenterLevel) {
		this.commenterLevel = commenterLevel;
	}

}