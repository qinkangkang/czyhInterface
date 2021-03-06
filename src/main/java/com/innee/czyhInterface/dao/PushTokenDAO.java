package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TPushToken;

public interface PushTokenDAO extends JpaRepository<TPushToken, String>, JpaSpecificationExecutor<TPushToken> {

	TPushToken getByFcustomerId(String fcustomerId);
}