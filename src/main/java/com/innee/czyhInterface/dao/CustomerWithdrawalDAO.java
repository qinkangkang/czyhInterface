package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TCustomerWithdrawal;

public interface CustomerWithdrawalDAO
		extends JpaRepository<TCustomerWithdrawal, String>, JpaSpecificationExecutor<TCustomerWithdrawal> {

}