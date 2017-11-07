package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TCouponObject;

public interface CouponObjectDAO extends JpaRepository<TCouponObject, String>, JpaSpecificationExecutor<TCouponObject> {

}