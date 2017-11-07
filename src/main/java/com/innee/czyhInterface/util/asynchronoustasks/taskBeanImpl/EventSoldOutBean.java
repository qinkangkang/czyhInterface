package com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl;

import com.innee.czyhInterface.util.asynchronoustasks.ITaskBean;

public class EventSoldOutBean implements ITaskBean {

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

	private String eventTitle;

	private String sessionTitle;

	private String specTitle;

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getSessionTitle() {
		return sessionTitle;
	}

	public void setSessionTitle(String sessionTitle) {
		this.sessionTitle = sessionTitle;
	}

	public String getSpecTitle() {
		return specTitle;
	}

	public void setSpecTitle(String specTitle) {
		this.specTitle = specTitle;
	}

}