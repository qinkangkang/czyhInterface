package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TEventCategory;

public interface EventCategoryDAO
		extends JpaRepository<TEventCategory, Integer>, JpaSpecificationExecutor<TEventCategory> {

	TEventCategory getByLevelAndValue(int level, int value);

	List<TEventCategory> findByLevel(int level);

	List<TEventCategory> findByLevelAndParentId(int level, long parentId);
}