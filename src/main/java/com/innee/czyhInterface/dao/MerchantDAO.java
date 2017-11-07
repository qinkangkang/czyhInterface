package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TMerchant;

public interface MerchantDAO extends JpaRepository<TMerchant, String>, JpaSpecificationExecutor<TMerchant> {

}