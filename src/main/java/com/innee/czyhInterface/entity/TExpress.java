package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_express")
public class TExpress extends UuidEntity {

	private static final long serialVersionUID = 1L;

	private String forderId;
	private String fexpressNum;
	private Integer fexYpressType;
	private String fstarting;
	private String freach;
	private Date fcreateTime;
	private Date fupdateTime;

	public TExpress() {
	}

	public TExpress(String id) {
		this.id = id;
	}

	@Column(name = "fOrderId", nullable = false, length = 36)
	public String getForderId() {
		return this.forderId;
	}

	public void setForderId(String forderId) {
		this.forderId = forderId;
	}

	@Column(name = "fExpressNum", nullable = false, length = 36)
	public String getFexpressNum() {
		return this.fexpressNum;
	}

	public void setFexpressNum(String fexpressNum) {
		this.fexpressNum = fexpressNum;
	}

	@Column(name = "fExYpressType", nullable = false, length = 36)
	public Integer getFexYpressType() {
		return this.fexYpressType;
	}

	public void setFexYpressType(Integer fexYpressType) {
		this.fexYpressType = fexYpressType;
	}

	@Column(name = "fCreateTime", nullable = false, length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}
	
	@Column(name = "fStarting", nullable = false)
	public String getFstarting() {
		return fstarting;
	}

	public void setFstarting(String fstarting) {
		this.fstarting = fstarting;
	}

	@Column(name = "fReach", nullable = false)
	public String getFreach() {
		return freach;
	}

	public void setFreach(String freach) {
		this.freach = freach;
	}


	@Column(name = "fUpdateTime", nullable = false, length = 19)
	public Date getFupdateTime() {
		return this.fupdateTime;
	}

	public void setFupdateTime(Date fupdateTime) {
		this.fupdateTime = fupdateTime;
	}

}