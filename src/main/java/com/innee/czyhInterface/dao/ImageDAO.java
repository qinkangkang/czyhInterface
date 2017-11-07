package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TImage;

public interface ImageDAO extends JpaRepository<TImage, Long>, JpaSpecificationExecutor<TImage> {

	List<TImage> findByIdIn(List<Long> ids);

	@Modifying
	@Query("delete TImage t where t.status = ?1")
	int clearTempImage(Integer status);

	@Modifying
	@Query("update TImage t set t.status = ?2, t.entityId = ?3, t.entityType = ?4 where t.id = ?1")
	void saveStatusAndEntityIdAndEntityType(Long id, Integer status, String entityId, Integer entityType);
}