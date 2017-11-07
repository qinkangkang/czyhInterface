package com.innee.czyhInterface.dao;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCouponShareOrder;

public interface CouponShareOrderDAO
		extends JpaRepository<TCouponShareOrder, String>, JpaSpecificationExecutor<TCouponShareOrder> {

	@Query("select t from TCouponShareOrder t where t.fstartMoney <= ?1 and t.fendMoney > ?1")
	TCouponShareOrder getByOrderPrice(BigDecimal orderprice);
}