package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TDictionaryClass;

public interface DictionaryClassDao extends JpaRepository<TDictionaryClass, Long>,
		JpaSpecificationExecutor<TDictionaryClass> {

}