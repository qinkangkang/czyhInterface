package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TCustomerBalanceDetail;

public interface CustomerBalanceDetailDAO
		extends JpaRepository<TCustomerBalanceDetail, String>, JpaSpecificationExecutor<TCustomerBalanceDetail> {

}