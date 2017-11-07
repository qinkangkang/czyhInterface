package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TDictionary;

public interface DictionaryDao extends JpaRepository<TDictionary, Long>, JpaSpecificationExecutor<TDictionary> {

	@Query("from TDictionary a where a.status = 1 order by a.TDictionaryClass.id asc ,a.value asc")
	List<TDictionary> findAllOrderByClassId();

	@Query("from TDictionary a where a.TDictionaryClass.id = ?1 and a.status = 1")
	List<TDictionary> findOrderByClassId(Long classId);

	@Query("from TDictionary a where a.value = ?1 and a.TDictionaryClass.id = ?2 and a.status = 1")
	TDictionary findNameAndValue(Integer value, Long classId);

	@Query("select count(a.id) from TDictionary a where a.name = ?1 and a.TDictionaryClass.id = ?2")
	Long countByNameAndClassId(String name, Long classId);

	@Query("select count(a.id) from TDictionary a where a.name = ?1 and a.TDictionaryClass.id = ?2 and a.id != ?3")
	Long countByNameAndClassIdNotId(String name, Long classId, Long id);

	@Query("select count(a.id) from TDictionary a where a.code = ?1 and a.TDictionaryClass.id = ?2")
	Long countByCodeAndClassId(String code, Long classId);

	@Query("select count(a.id) from TDictionary a where a.code = ?1 and a.TDictionaryClass.id = ?2 and a.id != ?3")
	Long countByCodeAndClassIdNotId(String code, Long classId, Long id);

	@Query("select count(a.id) from TDictionary a where a.value = ?1 and a.TDictionaryClass.id = ?2")
	Long countByValueAndClassId(Integer value, Long classId);

	@Query("select count(a.id) from TDictionary a where a.value = ?1 and a.TDictionaryClass.id = ?2 and a.id != ?3")
	Long countByValueAndClassIdNotId(Integer value, Long classId, Long id);

}