package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TPayInfo;

public interface PayInfoDAO extends JpaRepository<TPayInfo, String>, JpaSpecificationExecutor<TPayInfo> {

	@Query("select t from TPayInfo t where t.fpayId = ?1 and t.fstatus = 20 ")
	TPayInfo findByPayId(String payId);
}