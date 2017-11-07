package com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl;

import com.innee.czyhInterface.util.asynchronoustasks.ITaskBean;

public class BargainCountBean implements ITaskBean {

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

	private String eventBargainId;
	
	private String eventTitle;

	private String eventCode;

	private Integer remainingStock;


	public String getEventBargainId() {
		return eventBargainId;
	}

	public void setEventBargainId(String eventBargainId) {
		this.eventBargainId = eventBargainId;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public Integer getRemainingStock() {
		return remainingStock;
	}

	public void setRemainingStock(Integer remainingStock) {
		this.remainingStock = remainingStock;
	}
}