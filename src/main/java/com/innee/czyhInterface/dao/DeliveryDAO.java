package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TDelivery;

public interface DeliveryDAO extends JpaRepository<TDelivery, String>, JpaSpecificationExecutor<TDelivery> {

	@Modifying
	@Query("update TDelivery t set t.fstatus = ?2 where t.id = ?1")
	void updateStatus(String deliveryId, Integer status);
}