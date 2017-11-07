package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCouponDeliveryHistory;
import com.innee.czyhInterface.entity.TPushCustomer;

public interface PushCustomerDAO extends JpaRepository<TPushCustomer, String>, JpaSpecificationExecutor<TPushCustomer> {

	@Modifying
	@Query("update TPushCustomer t set t.fstatus = ?1 where t.id = ?2")
	void saveStatus(Integer status, String id);

	@Query("from TPushCustomer t where t.TPush.id = ?2 and t.fcustomerId = ?1")
	TPushCustomer getUnread(String customerId, String pushId);
}