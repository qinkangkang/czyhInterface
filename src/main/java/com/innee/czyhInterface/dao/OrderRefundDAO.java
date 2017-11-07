package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TOrder;
import com.innee.czyhInterface.entity.TOrderRefund;

public interface OrderRefundDAO extends JpaRepository<TOrderRefund, String>, JpaSpecificationExecutor<TOrderRefund> {

	@Query("select t from TOrderRefund t where t.forderId = ?1 and t.frefundStatus <= 30 ")
	TOrderRefund findByOrder(String orderId);
}