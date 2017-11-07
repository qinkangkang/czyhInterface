package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCouponDeliveryHistory;

public interface CouponDeliveryHistoryDAO
		extends JpaRepository<TCouponDeliveryHistory, String>, JpaSpecificationExecutor<TCouponDeliveryHistory> {

	@Query("from TCouponDeliveryHistory t where t.TCouponInformation.id = ?1 and t.TCustomer.id = ?2")
	TCouponDeliveryHistory getCouponDelivery(String couponId, String customerId);
	
	@Query("from TCouponDeliveryHistory t where t.TOrder.id = ?1 and t.fstatus = 20")
	TCouponDeliveryHistory getCouponbyOrder(String orderId);
	
	@Modifying
	@Query("update TCouponDeliveryHistory t set t.TCustomer.id = ?1 where t.TCustomer.id = ?2")
	void updateHistoryCouponByCustomer(String id2, String id);
}