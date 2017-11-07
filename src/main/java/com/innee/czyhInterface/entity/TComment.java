package com.innee.czyhInterface.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_comment")
public class TComment extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fcustomerId;
	private String fcustomerName;
	private String fcustomerLogoUrl;
	private String fobjectId;
	private String forderId;
	private String fcontent;
	private String fphoto;
	private Date fcreateTime;
	private Integer fscore;
	private Integer ftype;
	private Long frecommend;
	private Integer forder;
	private Long fauditor;
	private Long fuserId;
	private String fuserName;
	private String freply;
	private Date freplyTime;
	private Integer fstatus;

	// Constructors

	/** default constructor */
	public TComment() {
	}

	/** minimal constructor */
	public TComment(String id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "fCustomerID", length = 36)
	public String getFcustomerId() {
		return this.fcustomerId;
	}

	public void setFcustomerId(String fcustomerId) {
		this.fcustomerId = fcustomerId;
	}

	@Column(name = "fCustomerName")
	public String getFcustomerName() {
		return this.fcustomerName;
	}

	public void setFcustomerName(String fcustomerName) {
		this.fcustomerName = fcustomerName;
	}

	@Column(name = "fCustomerLogoUrl", length = 2048)
	public String getFcustomerLogoUrl() {
		return this.fcustomerLogoUrl;
	}

	public void setFcustomerLogoUrl(String fcustomerLogoUrl) {
		this.fcustomerLogoUrl = fcustomerLogoUrl;
	}

	@Column(name = "fObjectID", length = 36)
	public String getFobjectId() {
		return this.fobjectId;
	}

	public void setFobjectId(String fobjectId) {
		this.fobjectId = fobjectId;
	}

	@Column(name = "fContent", length = 2048)
	public String getFcontent() {
		return this.fcontent;
	}

	public void setFcontent(String fcontent) {
		this.fcontent = fcontent;
	}

	@Column(name = "fPhoto", length = 255)
	public String getFphoto() {
		return this.fphoto;
	}

	public void setFphoto(String fphoto) {
		this.fphoto = fphoto;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fScore")
	public Integer getFscore() {
		return this.fscore;
	}

	public void setFscore(Integer fscore) {
		this.fscore = fscore;
	}

	@Column(name = "fType")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fRecommend")
	public Long getFrecommend() {
		return this.frecommend;
	}

	public void setFrecommend(Long frecommend) {
		this.frecommend = frecommend;
	}

	@Column(name = "fOrder")
	public Integer getForder() {
		return this.forder;
	}

	public void setForder(Integer forder) {
		this.forder = forder;
	}

	@Column(name = "fAuditor")
	public Long getFauditor() {
		return this.fauditor;
	}

	public void setFauditor(Long fauditor) {
		this.fauditor = fauditor;
	}

	@Column(name = "fUserId")
	public Long getFuserId() {
		return this.fuserId;
	}

	public void setFuserId(Long fuserId) {
		this.fuserId = fuserId;
	}

	@Column(name = "fUserName")
	public String getFuserName() {
		return this.fuserName;
	}

	public void setFuserName(String fuserName) {
		this.fuserName = fuserName;
	}

	@Column(name = "fReply")
	public String getFreply() {
		return this.freply;
	}

	public void setFreply(String freply) {
		this.freply = freply;
	}

	@Column(name = "fReplyTime", length = 19)
	public Date getFreplyTime() {
		return this.freplyTime;
	}

	public void setFreplyTime(Date freplyTime) {
		this.freplyTime = freplyTime;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fOrderID")
	public String getForderId() {
		return forderId;
	}

	public void setForderId(String forderId) {
		this.forderId = forderId;
	}

}