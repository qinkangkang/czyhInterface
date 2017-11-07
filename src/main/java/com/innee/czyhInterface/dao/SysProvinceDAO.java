package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TSysProvince;

public interface SysProvinceDAO extends JpaRepository<TSysProvince, Integer>, JpaSpecificationExecutor<TSysProvince> {

	@Query("select t from TSysProvince t where t.fstatus = 0 order by t.proId")
	List<TSysProvince> getAllProvince();
}