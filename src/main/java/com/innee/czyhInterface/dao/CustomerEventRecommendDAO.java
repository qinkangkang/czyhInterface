package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCustomerEventRecommend;

public interface CustomerEventRecommendDAO extends JpaRepository<TCustomerEventRecommend, String>, JpaSpecificationExecutor<TCustomerEventRecommend> {

	@Modifying
	@Query("delete from TCustomerEventRecommend t where t.fcustomerId = ?1 and t.feventId=?2")
	void deleteByRecommendId(String customerId,String eventId);
}