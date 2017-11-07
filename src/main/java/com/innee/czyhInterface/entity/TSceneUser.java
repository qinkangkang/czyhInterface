package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "t_scene_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TSceneUser extends UuidEntity {

	private static final long serialVersionUID = 1L;

	private Integer fsceneId;
	private String fsceneStr;
	private String fsceneGps;
	private String fcity;
	private String flocation;
	private String fopenId;
	private Integer fsubscribe;
	private Date fsubscribeTime;
	private Integer funSubscribe;
	private Date funSubscribeTime;
	private Integer fregister;
	private Date fregisterTime;
	private String fphone;
	private Date fphoneTime;
	private Integer fdelivery;
	private Date fdeliveryTime;
	private Integer fbounsCustomer;
	private Date fcreateTime;
	private Date fupdateTime;

	// Constructors

	/** default constructor */
	public TSceneUser() {
	}

	/** full constructor */
	public TSceneUser(String id) {
		this.id = id;
	}

	@Column(name = "fSceneID")
	public Integer getFsceneId() {
		return this.fsceneId;
	}

	public void setFsceneId(Integer fsceneId) {
		this.fsceneId = fsceneId;
	}

	@Column(name = "fSceneStr")
	public String getFsceneStr() {
		return this.fsceneStr;
	}

	public void setFsceneStr(String fsceneStr) {
		this.fsceneStr = fsceneStr;
	}

	@Column(name = "fSceneGPS")
	public String getFsceneGps() {
		return this.fsceneGps;
	}

	public void setFsceneGps(String fsceneGps) {
		this.fsceneGps = fsceneGps;
	}

	@Column(name = "fCity")
	public String getFcity() {
		return this.fcity;
	}

	public void setFcity(String fcity) {
		this.fcity = fcity;
	}

	@Column(name = "fLocation")
	public String getFlocation() {
		return this.flocation;
	}

	public void setFlocation(String flocation) {
		this.flocation = flocation;
	}

	@Column(name = "fOpenID")
	public String getFopenId() {
		return this.fopenId;
	}

	public void setFopenId(String fopenId) {
		this.fopenId = fopenId;
	}

	@Column(name = "fSubscribe")
	public Integer getFsubscribe() {
		return this.fsubscribe;
	}

	public void setFsubscribe(Integer fsubscribe) {
		this.fsubscribe = fsubscribe;
	}

	@Column(name = "fSubscribeTime", length = 19)
	public Date getFsubscribeTime() {
		return this.fsubscribeTime;
	}

	public void setFsubscribeTime(Date fsubscribeTime) {
		this.fsubscribeTime = fsubscribeTime;
	}

	@Column(name = "fUnSubscribe")
	public Integer getFunSubscribe() {
		return this.funSubscribe;
	}

	public void setFunSubscribe(Integer funSubscribe) {
		this.funSubscribe = funSubscribe;
	}

	@Column(name = "fUnSubscribeTime", length = 19)
	public Date getFunSubscribeTime() {
		return this.funSubscribeTime;
	}

	public void setFunSubscribeTime(Date funSubscribeTime) {
		this.funSubscribeTime = funSubscribeTime;
	}

	@Column(name = "fRegister")
	public Integer getFregister() {
		return this.fregister;
	}

	public void setFregister(Integer fregister) {
		this.fregister = fregister;
	}

	@Column(name = "fRegisterTime", length = 19)
	public Date getFregisterTime() {
		return this.fregisterTime;
	}

	public void setFregisterTime(Date fregisterTime) {
		this.fregisterTime = fregisterTime;
	}

	@Column(name = "fPhone")
	public String getFphone() {
		return this.fphone;
	}

	public void setFphone(String fphone) {
		this.fphone = fphone;
	}

	@Column(name = "fPhoneTime", length = 19)
	public Date getFphoneTime() {
		return this.fphoneTime;
	}

	public void setFphoneTime(Date fphoneTime) {
		this.fphoneTime = fphoneTime;
	}

	@Column(name = "fDelivery")
	public Integer getFdelivery() {
		return this.fdelivery;
	}

	public void setFdelivery(Integer fdelivery) {
		this.fdelivery = fdelivery;
	}

	@Column(name = "fDeliveryTime", length = 19)
	public Date getFdeliveryTime() {
		return this.fdeliveryTime;
	}

	public void setFdeliveryTime(Date fdeliveryTime) {
		this.fdeliveryTime = fdeliveryTime;
	}

	@Column(name = "fBounsCustomer")
	public Integer getFbounsCustomer() {
		return this.fbounsCustomer;
	}

	public void setFbounsCustomer(Integer fbounsCustomer) {
		this.fbounsCustomer = fbounsCustomer;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fUpdateTime", length = 19)
	public Date getFupdateTime() {
		return this.fupdateTime;
	}

	public void setFupdateTime(Date fupdateTime) {
		this.fupdateTime = fupdateTime;
	}

}