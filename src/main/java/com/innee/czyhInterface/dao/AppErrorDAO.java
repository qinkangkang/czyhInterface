package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TAppError;

public interface AppErrorDAO extends JpaRepository<TAppError, String>, JpaSpecificationExecutor<TAppError> {

}