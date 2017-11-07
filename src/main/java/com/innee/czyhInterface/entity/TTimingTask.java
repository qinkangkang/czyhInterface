package com.innee.czyhInterface.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_timing_task")
public class TTimingTask extends IdEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String entityId;
	private Long taskTime;
	private Integer taskType;

	// Constructors

	/** default constructor */
	public TTimingTask() {
	}

	/** minimal constructor */
	public TTimingTask(Long id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "entityId", length = 36)
	public String getEntityId() {
		return this.entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	@Column(name = "taskTime")
	public Long getTaskTime() {
		return this.taskTime;
	}

	public void setTaskTime(Long taskTime) {
		this.taskTime = taskTime;
	}

	@Column(name = "taskType")
	public Integer getTaskType() {
		return this.taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

}