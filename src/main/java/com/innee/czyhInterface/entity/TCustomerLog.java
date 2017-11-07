package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_customer_log")
public class TCustomerLog extends IdEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String customerId;
	private Integer customerType;
	private String url;
	private String param;
	private Integer clientType;
	private String clientIp;
	private Date createTime;

	// Constructors

	/** default constructor */
	public TCustomerLog() {
	}

	// Property accessors
	@Column(name = "customerId", length = 36)
	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Column(name = "customerType")
	public Integer getCustomerType() {
		return this.customerType;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}

	@Column(name = "url")
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "param", length = 2048)
	public String getParam() {
		return this.param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	@Column(name = "clientType")
	public Integer getClientType() {
		return this.clientType;
	}

	public void setClientType(Integer clientType) {
		this.clientType = clientType;
	}

	@Column(name = "clientIp")
	public String getClientIp() {
		return this.clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	@Column(name = "createTime", length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}