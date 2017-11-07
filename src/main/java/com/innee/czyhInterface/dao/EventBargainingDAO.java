package com.innee.czyhInterface.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TEventBargaining;

public interface EventBargainingDAO
		extends JpaRepository<TEventBargaining, String>, JpaSpecificationExecutor<TEventBargaining> {

	@Modifying
	@Query("update TEventBargaining t set t.fstatus = ?1 ,t.fupdateTime = ?3 where t.id = ?2")
	void saveStatusBargaining(Integer status, String id, Date updateTime);

	@Query("select t from TEventBargaining t where t.feventId = ?1 and t.fstatus < 999")
	TEventBargaining getByEventId(String eventId);
}