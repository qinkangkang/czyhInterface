package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TRelation;

public interface RelationDAO extends JpaRepository<TRelation, String>, JpaSpecificationExecutor<TRelation> {

	@Query("select t from TRelation t where t.fcustomerId = ?1 and t.fbyCustomerId = ?2")
	TRelation findByUser(String fcustomerId, String fbyCustomerId);

	@Query("select t from TRelation t where t.fcustomerId = ?1 and  t.frelationType = ?2")
	List<TRelation> findByType(String customerId, Integer type);
}