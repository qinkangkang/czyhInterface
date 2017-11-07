package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TConfiguration;

public interface ConfigurationDAO
		extends JpaRepository<TConfiguration, String>, JpaSpecificationExecutor<TConfiguration> {

	@Modifying
	@Query("update TCoupon t set t.fstatus = ?1, t.fupdateTime = now() where t.id = ?2")
	void saveStatus(Integer status, String id);

	@Query("select t from TConfiguration t")
	List<TConfiguration> findAll();

}