package com.innee.czyhInterface.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TAppStartup;

public interface AppStartupDAO extends JpaRepository<TAppStartup, String>, JpaSpecificationExecutor<TAppStartup> {

	@Query("select count(t.id) from TAppStartup t where t.fcustomerId = ?1")
	Integer getCounttAppStartup(String customerId);
	
	@Modifying
	@Query("update TAppStartup t set t.fgps = ?2,t.fstartupTime = ?3,t.fclientType = ?4 where t.id = ?1")
	void setUpdateAppStartup(String id, String fgps, Date now,Integer clientType);
	
}