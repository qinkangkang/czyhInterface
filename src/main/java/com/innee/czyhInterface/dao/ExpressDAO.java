package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TExpress;

public interface ExpressDAO extends JpaRepository<TExpress, String>, JpaSpecificationExecutor<TExpress> {
	
	@Query("select t from TExpress t where t.forderId = ?1")
	TExpress findByOrderId(String orderId);

}