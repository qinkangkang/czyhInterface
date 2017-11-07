package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TAppIndexItem;

public interface AppIndexItemDAO extends JpaRepository<TAppIndexItem, String>, JpaSpecificationExecutor<TAppIndexItem> {

}