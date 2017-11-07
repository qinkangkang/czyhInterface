package com.innee.czyhInterface.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCustomerEventBrowse;

public interface CustomerEventBrowseDAO extends JpaRepository<TCustomerEventBrowse, String>, JpaSpecificationExecutor<TCustomerEventBrowse> {

	@Query("from TCustomerEventBrowse t where t.fcustomerId = ?1 and t.feventId = ?2")
	List<TCustomerEventBrowse> findByCustomerIdAndEventId(String customerId, String eventId);
	
	@Query("select count(t.id) from TCustomerEventBrowse t where t.fcustomerId = ?1")
	Integer getCountCustomerEventBrowse(String customerId);
	
	@Modifying
	@Query("update TCustomerEventBrowse t set t.feventId = ?2,t.fcreateTime = ?3 where t.id = ?1")
	void setEventId(String id, String eventId, Date now);
}