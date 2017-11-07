package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TCustomerEventDistribution;

public interface CustomerEventDistributionDAO extends JpaRepository<TCustomerEventDistribution, String>,
		JpaSpecificationExecutor<TCustomerEventDistribution> {

}