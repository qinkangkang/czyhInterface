package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCustomerBaby;

public interface CustomerBabyDAO extends JpaRepository<TCustomerBaby, String>, JpaSpecificationExecutor<TCustomerBaby> {

	@Query("select t from TCustomerBaby t where t.fcustomerId = ?1")
	TCustomerBaby getByCustomerId(String customerId);
	
}