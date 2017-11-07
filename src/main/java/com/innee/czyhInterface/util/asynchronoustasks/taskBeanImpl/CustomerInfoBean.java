package com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl;

import java.util.Date;

import com.innee.czyhInterface.util.asynchronoustasks.ITaskBean;

public class CustomerInfoBean implements ITaskBean {

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

	private String customerId;

	private String eventId;

	private Integer clientType;

	private Date createTime;

	private Integer type;

	private String gps;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Integer getClientType() {
		return clientType;
	}

	public void setClientType(Integer clientType) {
		this.clientType = clientType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}
}