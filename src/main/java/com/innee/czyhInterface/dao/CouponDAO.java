package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCoupon;

public interface CouponDAO extends JpaRepository<TCoupon, String>, JpaSpecificationExecutor<TCoupon> {

	TCoupon getByIdAndFstatus(String couponId, int i);

	@Modifying
	@Query("update TCoupon t set t.fstatus = ?2 where t.id = ?1")
	void updateStatus(String couponId, Integer status);

}