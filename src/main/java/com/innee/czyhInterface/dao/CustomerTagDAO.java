package com.innee.czyhInterface.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCustomerTag;

public interface CustomerTagDAO extends JpaRepository<TCustomerTag, String>, JpaSpecificationExecutor<TCustomerTag> {

	@Query("from TCustomerTag t where t.fcustomerId=?1 and t.ftag=?2")
	TCustomerTag findTCustomerTag(String fcustomerId,Integer ftag);
	
	@Query("from TCustomerTag t where t.fcustomerId=?1")
	TCustomerTag getByCustomer(String fcustomerId);
}