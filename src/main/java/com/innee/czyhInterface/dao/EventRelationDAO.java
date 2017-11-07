package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TEventRelation;

public interface EventRelationDAO
		extends JpaRepository<TEventRelation, String>, JpaSpecificationExecutor<TEventRelation> {

}