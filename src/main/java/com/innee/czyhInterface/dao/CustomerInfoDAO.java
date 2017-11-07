package com.innee.czyhInterface.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCustomerInfo;

public interface CustomerInfoDAO extends JpaRepository<TCustomerInfo, String>, JpaSpecificationExecutor<TCustomerInfo> {

	@Query("select t from TCustomerInfo t where t.fcustomerId = ?1")
	TCustomerInfo getByCustomerId(String customerId);

	@Modifying
	@Query("update TCustomerInfo t set t.forderTotal = ?2 where t.fcustomerId = ?1")
	void updateOrderTotal(String customerId, BigDecimal orderTotal);

	@Query("select t.fcustomerId from TCustomerInfo t")
	List<String> findByCustomerIdList();

	@Modifying
	@Query("update TCustomerInfo t set t.ftipNumber = ?2 where t.fcustomerId = ?1")
	void updateTipNumber(String customerId, Integer tipNumber);

	@Modifying
	@Query("update TCustomerInfo t set t.fpoint = (t.fpoint + ?2) where t.fcustomerId = ?1")
	void updatePoint(String customerId, int point);

	@Modifying
	@Query("update TCustomerInfo t set t.fposterImage = ?2 , t.fqRcodeImage = ?3 where t.fcustomerId = ?1")
	void updatePosterAndQr(String customerId, String posterImage, String qRcodeImage);

	@Modifying
	@Query("update TCustomerInfo t set t.fpoint = (t.fpoint + ?2),t.fusedPoint = (t.fusedPoint + ?3) where t.fcustomerId = ?1")
	void updatePointAndUsePoint(String customerId, int point, int usePoint);

	@Modifying
	@Query("update TCustomerInfo t set t.fpointCode = ?2 , t.finvalidTime = ?3 where t.fcustomerId = ?1")
	void updatePosterInfo(String customerId, String fpointCode, Date finvalidTime);

	@Query("select max(t.fpointCode) from TCustomerInfo t where 1=1")
	String getMaxPointCode();

	@Query("select t from TCustomerInfo t where t.fpointCode = ?1 ")
	TCustomerInfo findOneByfpointCode(String fpointCode);

	@Modifying
	@Query("update TCustomerInfo t set t.fkykFlag = ?2 where t.fcustomerId = ?1")
	void updateKykFlag(String customerId, Integer fkykFlag);

	@Query("select t from TCustomerInfo t where t.followCustomerId = ?1 ")
	List<TCustomerInfo> findfollowCustomerIdList(String followCustomerId);

	@Query("select t from TCustomerInfo t where t.fcustomerId = ?1 ")
	TCustomerInfo getPointAndCouponNum(String customerId);

	@Modifying
	@Query("update TCustomerInfo t set t.fregisterDeviceTokens = ?2 where t.fcustomerId = ?1")
	void updateDeviceTokens(String customerId, String deviceTokens);
	
	@Modifying
	@Query("update TCustomerInfo t set t.fans = (t.fans + ?2) where t.fcustomerId = ?1")
	void updateFfans(String customerId, int fans);

}