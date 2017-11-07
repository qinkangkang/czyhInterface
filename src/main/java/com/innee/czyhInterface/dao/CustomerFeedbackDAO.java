package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TCustomerFeedback;

public interface CustomerFeedbackDAO extends JpaRepository<TCustomerFeedback, String>, JpaSpecificationExecutor<TCustomerFeedback> {

}