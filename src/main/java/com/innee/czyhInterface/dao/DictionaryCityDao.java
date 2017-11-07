package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TDictionaryCity;

public interface DictionaryCityDao
		extends JpaRepository<TDictionaryCity, Long>, JpaSpecificationExecutor<TDictionaryCity> {

	@Query("select t from TDictionaryCity t where 1=1")
	List<TDictionaryCity> findAll();

	@Query("select t from TDictionaryCity t where t.value = ?1")
	TDictionaryCity findOneCity(Integer cityId);
}