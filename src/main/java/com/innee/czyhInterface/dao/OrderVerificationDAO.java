package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TOrderVerification;

public interface OrderVerificationDAO
		extends JpaRepository<TOrderVerification, String>, JpaSpecificationExecutor<TOrderVerification> {

}