package com.innee.czyhInterface.util.asynchronoustasks;

import java.io.Serializable;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.innee.czyhInterface.exception.ServiceException;
import com.innee.czyhInterface.service.v1.asynchronousTasks.AsynchronousTasksService;
import com.innee.czyhInterface.util.SpringContextHolder;

public class AsynchronousTasksExecutor implements Callable<String>, Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(AsynchronousTasksExecutor.class);

	private ITaskBean iTaskBean;

	public AsynchronousTasksExecutor(ITaskBean iTaskBean) {
		this.iTaskBean = iTaskBean;
	}

	@Override
	public String call() throws Exception {
		// 处理五次都有异常，就不再重复执行了
		if (iTaskBean.getCounter() < 5) {
			try {
				AsynchronousTasksService asynchronousTasksService = SpringContextHolder
						.getBean(AsynchronousTasksService.class);
				asynchronousTasksService.performTasks(iTaskBean);
			} catch (ServiceException e) {
				logger.error("异步线程任务出错", e);
				iTaskBean.addCounterOne();
				AsynchronousTasksManager.put(iTaskBean);
			}
		}
		return null;
	}
}