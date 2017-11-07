package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TSysRegion;

public interface SysRegionDAO extends JpaRepository<TSysRegion, Integer>, JpaSpecificationExecutor<TSysRegion> {

	@Query("select t from TSysRegion t where t.isDisabled = 0")
	List<TSysRegion> getAllRegion();
}