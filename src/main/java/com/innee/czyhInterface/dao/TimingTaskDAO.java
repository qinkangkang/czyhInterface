package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TTimingTask;

public interface TimingTaskDAO extends JpaRepository<TTimingTask, Long>, JpaSpecificationExecutor<TTimingTask> {

	List<TTimingTask> findByTaskTimeLessThan(long time);

	TTimingTask getByEntityIdAndTaskType(String entityId, Integer taskType);

	@Modifying
	@Query("delete TTimingTask t where t.entityId = ?1")
	void clearTimeTaskByEntityId(String entityId);

	@Modifying
	@Query("delete TTimingTask t where t.entityId = ?1 and t.taskType = ?2")
	void clearTimeTaskByEntityIdAndTaskType(String entityId, Integer taskType);

	@Modifying
	@Query("update TTimingTask t set t.taskTime = ?1 where t.id = ?2")
	void saveTaskTime(Long taskTime, Long id);

	@Modifying
	@Query("update TTimingTask t set t.taskTime = ?1 where t.entityId = ?2 and t.taskType = ?3")
	void saveTaskTime(Long taskTime, String entityId, Integer taskType);

}