package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TAppChannelEvent;

public interface AppChannelEventDAO extends JpaRepository<TAppChannelEvent, String>, JpaSpecificationExecutor<TAppChannelEvent> {

}