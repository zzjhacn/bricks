package com.bricks.core.task;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

/**
 * 基于zookeeper的多线程任务处理器<br>
 * 工作模式：<br>
 * 1、生成基础数据<br>
 * 2、从数据节点拿到若干（具体数量可由派生子类指定，默认1）待处理数据，交给独立线程进行处理。<br>
 * 3、重复步骤2，若拿不到待处理数据，则任务结束。<br>
 * <br>
 * 派生子类需实现六个方法：<br>
 * 1、getName 指定任务名称<br>
 * 2、getRootNode 指定私有根节点<br>
 * 3、getBaseData 生成基础数据<br>
 * 4、handleData 处理基础数据<br>
 * 5、getBatchSize 指定批次大小（默认为1）<br>
 * 6、getThreadNum 指定线程池大小（默认为5）<br>
 * 
 * @param <T>
 *            数据节点类型
 * @author bricks <long1795@gmail.com>
 */
public abstract class MutiThreadsZkBasedTask<T> extends ZkBasedTask<T> {

	protected static final int DEFAULT_THREAD_POOL_SIZE = 5;

	// 线程池
	protected ThreadPoolExecutor pool;

	/**
	 * 指定线程数量
	 * 
	 * @return 线程数量
	 */
	protected int getThreadNum() {
		return DEFAULT_THREAD_POOL_SIZE;
	}

	@Override
	@PostConstruct
	public void init() {
		super.init();

		// 初始化线程池
		pool = new ThreadPoolExecutor(getThreadNum(), getThreadNum(), 1, TimeUnit.HOURS,
				new LinkedBlockingQueue<Runnable>());
	}

	@Override
	public void execute() throws Exception {
		log().info("Status report for Threadpool of Task[{}]:[{}]", getName(), pool.toString());
		super.execute();
	}

	@Override
	protected int executeImpl() throws Exception {
		// 从数据节点获取待处理数据，交易给独立线程进行处理
		// 若拿不到待处理数据，则任务结束
		List<T> dataList = getData();
		int i = 0;
		while (dataList != null && !dataList.isEmpty()) {
			i += dataList.size();
			dataList.parallelStream().forEach(t -> {
				pool.execute(() -> {
					try {
						handleData(t);
					} catch (Exception e) {}
				});
			});
			dataList = getData();
		}
		return i;
	}

	/**
	 * 全部执行完成后，输出任务报告
	 */
	protected StringBuffer taskReport(long startTime, int cnt, int all) {
		while (pool.getActiveCount() > 0) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
		}
		return super.taskReport(startTime, cnt, all);
	}
}
