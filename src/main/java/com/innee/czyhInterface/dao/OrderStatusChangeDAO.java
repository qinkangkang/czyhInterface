package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TOrderStatusChange;

public interface OrderStatusChangeDAO
		extends JpaRepository<TOrderStatusChange, String>, JpaSpecificationExecutor<TOrderStatusChange> {

}