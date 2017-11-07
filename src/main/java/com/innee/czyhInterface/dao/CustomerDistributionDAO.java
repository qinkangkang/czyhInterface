package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TCustomerDistribution;

public interface CustomerDistributionDAO
		extends JpaRepository<TCustomerDistribution, String>, JpaSpecificationExecutor<TCustomerDistribution> {

}