package com.innee.czyhInterface.util.asynchronoustasks;

import java.io.Serializable;

public interface ITaskBean extends Serializable {

	public void addCounterOne();

	public int getCounter();

	public void setCounter(int counter);

	public int getTaskType();

	public void setTaskType(int taskType);

}