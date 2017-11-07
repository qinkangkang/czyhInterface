package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCustomerRecommend;

public interface CustomerRecommendDAO
		extends JpaRepository<TCustomerRecommend, String>, JpaSpecificationExecutor<TCustomerRecommend> {
	

	@Query("from TCustomerRecommend t where t.fcustomerId = ?1 and t.feventId = ?2")
	TCustomerRecommend getByCustomerIdAndEventId(String customerId, String eventId);
}