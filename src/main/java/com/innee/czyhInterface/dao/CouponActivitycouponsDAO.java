package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCouponActivitycoupons;

public interface CouponActivitycouponsDAO
		extends JpaRepository<TCouponActivitycoupons, String>, JpaSpecificationExecutor<TCouponActivitycoupons> {

	@Query("from TCouponActivitycoupons t where t.fcouponId = ?1 and t.fdeliveryId = ?2")
	TCouponActivitycoupons getTCouponActivitycoupons(String couponId, String deliveryId);

	@Query("from TCouponActivitycoupons t where t.fcouponId = ?1")
	TCouponActivitycoupons getTActivitycoupons(String couponId);

}