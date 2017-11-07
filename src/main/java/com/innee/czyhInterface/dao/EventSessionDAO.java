package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TEventSession;

public interface EventSessionDAO extends JpaRepository<TEventSession, String>, JpaSpecificationExecutor<TEventSession> {

	@Query("from TEventSession t where t.TEvent.id = ?1 and t.fstatus < 999")
	List<TEventSession> findByEventId(String eventId);

	@Modifying
	@Query("update TEventSession t set t.fstatus = ?1 where t.id = ?2")
	void saveStatus(Integer status, String id);

}