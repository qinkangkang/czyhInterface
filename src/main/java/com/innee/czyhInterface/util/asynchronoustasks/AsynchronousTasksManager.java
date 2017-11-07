package com.innee.czyhInterface.util.asynchronoustasks;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.utils.Exceptions;

public class AsynchronousTasksManager extends Thread {

	private static Logger logger = LoggerFactory.getLogger(AsynchronousTasksManager.class);

	private static ArrayBlockingQueue<ITaskBean> abq = new ArrayBlockingQueue<ITaskBean>(1000, true);

	private static ThreadPoolExecutor producerPool = null;

	public static void put(ITaskBean iTaskBean) {
		try {
			abq.put(iTaskBean);
		} catch (InterruptedException e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
	}

	public AsynchronousTasksManager() {
		if (producerPool == null) {
			producerPool = new ThreadPoolExecutor(5, 20, 3600, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000),
					new ThreadPoolExecutor.DiscardOldestPolicy());
		}
	}

	@Override
	public void run() {
		ITaskBean iTaskBean = null;
		try {
			while (true) {
				if (abq.isEmpty()) {
					Thread.sleep(10000);
				} else {
					iTaskBean = abq.poll();
					if (iTaskBean != null) {
						producerPool.submit(new AsynchronousTasksExecutor(iTaskBean));
						// logger.info("开始执行生成静态化页面！TemplateName：" +
						// staticHtmlBean.getTemplateName() + "；ID："
						// + staticHtmlBean.getObjectId());
					}
				}
			}
		} catch (InterruptedException e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		} finally {
			interrupt();
		}
	}

	@Override
	public void interrupt() {
		producerPool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!producerPool.awaitTermination(10, TimeUnit.SECONDS)) {
				producerPool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!producerPool.awaitTermination(10, TimeUnit.SECONDS)) {
					logger.error("Pool did not terminated");
				}
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			producerPool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

}