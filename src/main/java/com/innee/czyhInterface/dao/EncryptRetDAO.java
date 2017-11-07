package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TEncryptRet;

public interface EncryptRetDAO extends JpaRepository<TEncryptRet, String>, JpaSpecificationExecutor<TEncryptRet> {

	@Query("select t from TEncryptRet t where t.id = ?1 and t.fstatus = 1")
	TEncryptRet findToken(Long id);
}