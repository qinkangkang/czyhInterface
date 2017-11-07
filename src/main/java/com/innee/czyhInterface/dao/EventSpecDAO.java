package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TEventSpec;

public interface EventSpecDAO extends JpaRepository<TEventSpec, String>, JpaSpecificationExecutor<TEventSpec> {

	@Modifying
	@Query("update TEventSpec t set t.fstock = t.fstock + ?1 where t.id = ?2")
	void addStock(Integer count, String id);

	@Modifying
	@Query("update TEventSpec t set t.fstock = t.fstock - ?1 where t.id = ?2")
	void subtractStock(Integer count, String id);

	@Query("select sum(s.fstock) from TEventSpec s where s.TEvent.id = ?1 and s.fstatus < 999")
	Integer getSumStock(String eventId);

}