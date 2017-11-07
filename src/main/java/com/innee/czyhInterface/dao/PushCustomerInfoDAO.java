package com.innee.czyhInterface.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TPushCustomerInfo;
import com.innee.czyhInterface.entity.TPushToken;

public interface PushCustomerInfoDAO
		extends JpaRepository<TPushCustomerInfo, String>, JpaSpecificationExecutor<TPushCustomerInfo> {

	TPushCustomerInfo getByFcustomerId(String fcustomerId);

	@Modifying
	@Query("update TPushCustomerInfo t set t.funread = ?3 where t.fcustomerId = ?1 and t.id=?2")
	void updatePushstatus(String customerId, String id, Integer unread);
}