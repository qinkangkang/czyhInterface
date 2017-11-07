package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TAutoShow;

public interface AutoShowDAO extends JpaRepository<TAutoShow, String>, JpaSpecificationExecutor<TAutoShow> {

}