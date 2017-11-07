package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TTag;

public interface TTagDAO extends JpaRepository<TTag, String>, JpaSpecificationExecutor<TTag> {

	@Query("from TTag t where t.ftype = ?1 and t.fisDisplay = 1 order by t.forder")
	List<TTag> findByType(Integer type);

}