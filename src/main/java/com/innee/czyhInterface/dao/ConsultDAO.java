package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TConsult;

public interface ConsultDAO extends JpaRepository<TConsult, String>, JpaSpecificationExecutor<TConsult> {

	@Modifying
	@Query("update TConsult t set t.fcustomerId = ?1 where t.fcustomerId = ?2")
	void updateConsultByCustomer(String id2, String id);
	
	@Query("select count(t.id) from TConsult t where t.fobjectId = ?1 and t.ftype = 1 and t.fstatus < 999")
	Long findByEvent(String eventId);
}