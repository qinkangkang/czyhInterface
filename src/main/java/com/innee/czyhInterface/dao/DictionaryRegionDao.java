package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TDictionaryRegion;

public interface DictionaryRegionDao
		extends JpaRepository<TDictionaryRegion, Long>, JpaSpecificationExecutor<TDictionaryRegion> {

	@Query("select t from TDictionaryRegion t where 1=1")
	List<TDictionaryRegion> findAllRegion();

	@Query("select t from TDictionaryRegion t where t.cityValue = ?1")
	List<TDictionaryRegion> findOneCityRegion(Integer cityId);
}