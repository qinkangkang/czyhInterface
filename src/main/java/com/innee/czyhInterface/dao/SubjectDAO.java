package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TSubject;

public interface SubjectDAO extends JpaRepository<TSubject, String>, JpaSpecificationExecutor<TSubject> {
	
	@Query("select t from TSubject t where t.id = ?1 and fstatus = ?2")
	TSubject findOne(String subjectId, int i);

}