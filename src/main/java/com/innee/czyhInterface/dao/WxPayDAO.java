package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TWxPay;

public interface WxPayDAO extends JpaRepository<TWxPay, String>, JpaSpecificationExecutor<TWxPay> {

	@Query("from TWxPay t where t.forderId = ?1 and t.fstatus = ?2")
	TWxPay getByOrderIdAndStatus(String orderId, Integer status);

	@Query("from TWxPay t where t.forderId = ?1 and t.finOut = ?2 and t.fstatus = ?3")
	TWxPay getByOrderIdAndInOutAndStatus(String orderId, Integer inOut, Integer status);

}