package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TSubobjectTimeStamp;

public interface SubobjectTimeStampDAO
		extends JpaRepository<TSubobjectTimeStamp, String>, JpaSpecificationExecutor<TSubobjectTimeStamp> {

}