package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TCustomerLog;

public interface CustomerLogDAO extends JpaRepository<TCustomerLog, Long>, JpaSpecificationExecutor<TCustomerLog> {

}