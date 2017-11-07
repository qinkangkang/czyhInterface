package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TSceneScanInfo;

public interface SceneScanInfoDAO
		extends JpaRepository<TSceneScanInfo, String>, JpaSpecificationExecutor<TSceneScanInfo> {

}