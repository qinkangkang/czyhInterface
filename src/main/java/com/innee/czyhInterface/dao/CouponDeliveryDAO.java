package com.innee.czyhInterface.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCouponDelivery;

public interface CouponDeliveryDAO
		extends JpaRepository<TCouponDelivery, String>, JpaSpecificationExecutor<TCouponDelivery> {

	/*@Query("from TCouponDelivery t where t.TCouponInformation.id = ?1 and t.TCustomer.id = ?2 and t.fstatus = ?3")
	TCouponDelivery getCouponDelivery(String couponId, String customerId, Integer status);

	@Modifying
	@Query("update TCouponDelivery t set t.TOrder.id = ?2, t.fstatus = ?3, t.fuseTime = now() where t.id = ?1")
	void updateOrderIdAndStatus(String couponDeliveryId, String orderId, Integer newStatus);

	@Modifying
	@Query("update TCouponDelivery t set t.TOrder.id = null, t.fuseTime = null, t.fstatus = 10 where t.TOrder.id = ?1 and t.fstatus in(?2 ,?3)")
	void clearOrderIdAndUseTime(String orderId, Integer statusA, Integer statusB);

	@Modifying
	@Query("update TCouponDelivery t set t.fstatus = ?3 where t.TOrder.id = ?1 and t.fstatus = ?2")
	void updateStatusByOrderId(String orderId, Integer status, Integer newStatus);

	@Modifying
	@Query("update TCouponDelivery t set t.fstatus = ?3 where t.TCouponInformation.id = ?1 and t.fstatus < ?2")
	void updateStatusByCouponId(String couponId, Integer status, Integer newStatus);
	
	@Modifying
	@Query("update TCouponDelivery t set t.fstatus = ?3 where t.TCouponInformation.id = ?1 and t.fstatus = ?2")
	void updateStatusByCouponId2(String couponId, Integer status, Integer newStatus);

	@Query("select count(t.id) from TCouponDelivery t where t.TCustomer.id = ?2 and t.TCouponInformation.id = ?1")
	int isReceiveCoupon(String couponId, String customerId);*/
	
	@Query("from TCouponDelivery t where t.id = ?1 and (t.TCustomer.id = ?2 or t.TCustomer.id is null) and t.fdeliverTime >= ?3")
	TCouponDelivery getCouponbycustomer(String couponDeliveryId, String customerId,Date date);
	
	@Query("from TCouponDelivery t where (t.TCustomer.id = ?1 or t.TCustomer.id is null ) and t.TCouponInformation.id = ?2")
	TCouponDelivery getCouponbyCustomer(String customerId, String couponId);
	
	@Modifying
	@Query("update TCouponDelivery t set t.TCustomer.id = ?1 where t.TCustomer.id = ?2")
	void updateCouponByCustomer(String id2, String id);


	@Query("select count(id) from TCouponDelivery t where (t.TCustomer.id = ?1 or t.TCustomer.id is null) ")
	long getSizebycustomer(String customerId);

}