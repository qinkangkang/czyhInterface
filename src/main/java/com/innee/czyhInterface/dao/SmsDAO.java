package com.innee.czyhInterface.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TSms;

public interface SmsDAO extends JpaRepository<TSms, Long>, JpaSpecificationExecutor<TSms> {

	@Query("select t.sendTime from TSms t where t.sendPhone = ?1 ")
	List<Date> findByPhone(String phone);
}