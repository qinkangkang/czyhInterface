package com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl;

import com.innee.czyhInterface.entity.TOrder;
import com.innee.czyhInterface.util.asynchronoustasks.ITaskBean;

public class TicketOperateBean  implements ITaskBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	private TOrder order;

	public TOrder getOrder() {
		return order;
	}

	public void setOrder(TOrder order) {
		this.order = order;
	}

}
