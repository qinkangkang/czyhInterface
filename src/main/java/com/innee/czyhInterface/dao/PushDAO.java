package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TPush;

public interface PushDAO extends JpaRepository<TPush, String>, JpaSpecificationExecutor<TPush> {

	@Query("select t from TPush t where t.id = ?1 and t.fstatus < 999")
	TPush findPushId(String pushId);

	@Modifying
	@Query("update TPush t set t.fstatus = ?1 where t.id = ?2")
	void saveUpdatePush(Integer status, String id);

}