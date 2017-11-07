package com.innee.czyhInterface.dto.m.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.dto.m.express.ExpressDTO;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class ViewOrderDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String orderId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String orderNum;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String name;
	
	private int source = 1;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String address;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String phone;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorName;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorPhone;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorAddress;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorGps;

	private int status = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String statusString;

	private BigDecimal postage = BigDecimal.ZERO;

	private int orderType = 0;

	private BigDecimal total = BigDecimal.ZERO;

	private int payType = 0;

	private int sellModel = 0;

	private int promotionModel = 0;
	
	private int sponsorModel = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String spec;

	private int specModel;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String addRemark;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String remark;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String orderTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String payTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String exname;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String exnum;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String exmessage;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String verificationCode;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String verificationCodeAuto;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String confirmationTime;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String changeAmount = "0";
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String cancelTime = "0";

	private List<OrderGoodsDetailDTO> orderGoodsList;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSponsorName() {
		return sponsorName;
	}

	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public int getSellModel() {
		return sellModel;
	}

	public void setSellModel(int sellModel) {
		this.sellModel = sellModel;
	}

	public int getPromotionModel() {
		return promotionModel;
	}

	public void setPromotionModel(int promotionModel) {
		this.promotionModel = promotionModel;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public int getSpecModel() {
		return specModel;
	}

	public void setSpecModel(int specModel) {
		this.specModel = specModel;
	}

	public String getAddRemark() {
		return addRemark;
	}

	public void setAddRemark(String addRemark) {
		this.addRemark = addRemark;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getPostage() {
		return postage;
	}

	public void setPostage(BigDecimal postage) {
		this.postage = postage;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getExname() {
		return exname;
	}

	public void setExname(String exname) {
		this.exname = exname;
	}

	public String getExnum() {
		return exnum;
	}

	public void setExnum(String exnum) {
		this.exnum = exnum;
	}

	public String getExmessage() {
		return exmessage;
	}

	public void setExmessage(String exmessage) {
		this.exmessage = exmessage;
	}

	public List<OrderGoodsDetailDTO> getOrderGoodsList() {
		return orderGoodsList;
	}

	public void setOrderGoodsList(List<OrderGoodsDetailDTO> orderGoodsList) {
		this.orderGoodsList = orderGoodsList;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSponsorPhone() {
		return sponsorPhone;
	}

	public void setSponsorPhone(String sponsorPhone) {
		this.sponsorPhone = sponsorPhone;
	}

	public String getSponsorAddress() {
		return sponsorAddress;
	}

	public void setSponsorAddress(String sponsorAddress) {
		this.sponsorAddress = sponsorAddress;
	}

	public String getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(String changeAmount) {
		this.changeAmount = changeAmount;
	}

	public String getSponsorGps() {
		return sponsorGps;
	}

	public void setSponsorGps(String sponsorGps) {
		this.sponsorGps = sponsorGps;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public String getConfirmationTime() {
		return confirmationTime;
	}

	public void setConfirmationTime(String confirmationTime) {
		this.confirmationTime = confirmationTime;
	}

	public int getSponsorModel() {
		return sponsorModel;
	}

	public void setSponsorModel(int sponsorModel) {
		this.sponsorModel = sponsorModel;
	}

	public String getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}

	public String getVerificationCodeAuto() {
		return verificationCodeAuto;
	}

	public void setVerificationCodeAuto(String verificationCodeAuto) {
		this.verificationCodeAuto = verificationCodeAuto;
	}
	
}