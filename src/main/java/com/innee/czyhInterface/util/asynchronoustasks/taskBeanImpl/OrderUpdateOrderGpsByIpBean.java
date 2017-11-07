package com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl;

import com.innee.czyhInterface.util.asynchronoustasks.ITaskBean;

public class OrderUpdateOrderGpsByIpBean implements ITaskBean {

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

	private String orderId;

	private String ip;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}