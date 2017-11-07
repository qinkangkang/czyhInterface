package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCouponChannel;

public interface CouponChannelDAO
		extends JpaRepository<TCouponChannel, String>, JpaSpecificationExecutor<TCouponChannel> {

	@Modifying
	@Query("update TCouponChannel t set t.fstatus = ?1, t.fupdateTime = now() where t.id = ?2")
	void updateStatus(Integer status, String id);
}