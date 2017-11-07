package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TCouponInformation;

public interface CouponInformationDAO
		extends JpaRepository<TCouponInformation, String>, JpaSpecificationExecutor<TCouponInformation> {

}