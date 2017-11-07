package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TEventDetail;

public interface EventDetailDAO extends JpaRepository<TEventDetail, String>, JpaSpecificationExecutor<TEventDetail> {

}