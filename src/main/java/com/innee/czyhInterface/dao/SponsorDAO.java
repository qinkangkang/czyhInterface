package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TSponsor;

public interface SponsorDAO extends JpaRepository<TSponsor, String>, JpaSpecificationExecutor<TSponsor> {

	@Query("select count(t.id) from TSponsor t where t.fname = ?1 and t.fstatus < 999")
	Long checkName(String name);

	@Query("select count(t.id) from TSponsor t where t.id != ?1 and t.fname = ?2 and t.fstatus < 999")
	Long checkEditName(String id, String name);

	@Query("select t from TSponsor t where t.fdetailHtmlUrl is null and t.fstatus = 1")
	List<TSponsor> findByFdetailHtmlUrlIsnull();

	@Modifying
	@Query("update TSponsor t set t.fdetailHtmlUrl = ?2 where t.id = ?1")
	void updateFdetailHtmlUrl(String id, String fdetailHtmlUrl);

	@Modifying
	@Query("update TSponsor t set t.fstatus = ?2 where t.id = ?1")
	void updateStatus(String id, Integer status);

	@Query("select max(t.fnumber) from TSponsor t")
	String getMaxNumber();

	@Query("select t from TSponsor t where (t.fnumber is null or t.fnumber = '') and t.fstatus < 999")
	List<TSponsor> findByFnumberIsNull();
}