package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TAppFlash;

public interface AppFlashDAO extends JpaRepository<TAppFlash, String>, JpaSpecificationExecutor<TAppFlash> {

	TAppFlash findByFcityAndFisVisible(Integer cityId, Integer fisVisible);

}