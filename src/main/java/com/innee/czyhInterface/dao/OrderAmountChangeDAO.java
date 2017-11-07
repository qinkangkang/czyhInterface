package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TOrderAmountChange;

public interface OrderAmountChangeDAO
		extends JpaRepository<TOrderAmountChange, String>, JpaSpecificationExecutor<TOrderAmountChange> {

}