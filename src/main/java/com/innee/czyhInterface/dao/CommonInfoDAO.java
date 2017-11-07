package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TCommonInfo;

public interface CommonInfoDAO extends JpaRepository<TCommonInfo, String>, JpaSpecificationExecutor<TCommonInfo> {

	List<TCommonInfo> findByFcustomerIdAndFtype(String fcustomerId, Integer ftype);

}