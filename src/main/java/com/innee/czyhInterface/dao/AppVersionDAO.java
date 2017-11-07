package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TAppVersion;

public interface AppVersionDAO extends JpaRepository<TAppVersion, String>, JpaSpecificationExecutor<TAppVersion> {

}