package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TEventDistanceTemp;

public interface EventDistanceTempDAO
		extends JpaRepository<TEventDistanceTemp, String>, JpaSpecificationExecutor<TEventDistanceTemp> {

	@Modifying
	@Query("delete TEventDistanceTemp t where t.fhsid = ?1")
	void deleteDistanceTempByHsid(String hsid);

	@Modifying
	@Query("delete TEventDistanceTemp t where t.fcreateTime < ?1")
	int deleteDistanceTempByOverdue(long overdue);

}