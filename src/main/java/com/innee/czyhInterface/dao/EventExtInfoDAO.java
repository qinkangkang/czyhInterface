package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TEventExtInfo;

public interface EventExtInfoDAO extends JpaRepository<TEventExtInfo, String>, JpaSpecificationExecutor<TEventExtInfo> {

	@Query("select t from TEventExtInfo t where t.feventId = ?1  and t.fstatus < 999 order by t.forder")
	List<TEventExtInfo> findByEventId(String eventId);

}