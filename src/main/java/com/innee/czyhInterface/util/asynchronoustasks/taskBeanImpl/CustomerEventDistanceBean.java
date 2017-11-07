package com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl;

import java.util.List;

import com.innee.czyhInterface.entity.TEventDistanceTemp;
import com.innee.czyhInterface.util.asynchronoustasks.ITaskBean;

public class CustomerEventDistanceBean implements ITaskBean {

	private static final long serialVersionUID = 1L;

	/* 任务Bean的固定属性和方法 */

	private int counter = 0;

	private int taskType;

	public void addCounterOne() {
		counter++;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	/* Bean的实际属性 */

	private String sessionId;

	private List<TEventDistanceTemp> eventDistanceTempList;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public List<TEventDistanceTemp> getEventDistanceTempList() {
		return eventDistanceTempList;
	}

	public void setEventDistanceTempList(List<TEventDistanceTemp> eventDistanceTempList) {
		this.eventDistanceTempList = eventDistanceTempList;
	}

}