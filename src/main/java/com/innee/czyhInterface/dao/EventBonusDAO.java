package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TEventBonus;


public interface EventBonusDAO extends JpaRepository<TEventBonus, String>, JpaSpecificationExecutor<TEventBonus> {
	
}