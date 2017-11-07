package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_goods_spec_value")
public class TGoodsSpaceValue extends UuidEntity {

	private static final long serialVersionUID = 1L;

	private String fgoodsId;
	private String fspaceName;
	private String fvalueName;
//	private Date fupdateDate;
//	private Date fcreateTime;

	// Constructors

	/** default constructor */
	public TGoodsSpaceValue() {
	}

	public void setFid(String id) {
		this.id = id;
	}

	@Column(name = "fGoodsId", nullable = false, length = 36)
	public String getFgoodsId() {
		return this.fgoodsId;
	}

	public void setFgoodsId(String fgoodsId) {
		this.fgoodsId = fgoodsId;
	}

	@Column(name = "fSpaceName")
	public String getFspaceName() {
		return this.fspaceName;
	}

	public void setFspaceName(String fspaceName) {
		this.fspaceName = fspaceName;
	}

	@Column(name = "fValueName")
	public String getFvalueName() {
		return this.fvalueName;
	}

	public void setFvalueName(String fvalueName) {
		this.fvalueName = fvalueName;
	}

//	@Column(name = "fUpdateDate", length = 19)
//	public Date getFupdateDate() {
//		return this.fupdateDate;
//	}
//
//	public void setFupdateDate(Date fupdateDate) {
//		this.fupdateDate = fupdateDate;
//	}
//
//	@Column(name = "fCreateTime", length = 19)
//	public Date getFcreateTime() {
//		return this.fcreateTime;
//	}
//
//	public void setFcreateTime(Date fcreateTime) {
//		this.fcreateTime = fcreateTime;
//	}

}