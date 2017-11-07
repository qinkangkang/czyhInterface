package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TSysCity;
import com.innee.czyhInterface.entity.TSysProvince;

public interface SysCityDAO extends JpaRepository<TSysCity, Integer>, JpaSpecificationExecutor<TSysCity> {

	@Query("select t from TSysCity t where t.isDisabled = 0")
	List<TSysCity> getAllCity();
}