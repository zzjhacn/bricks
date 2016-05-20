package com.bricks.core.mutex;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.util.Assert;

import com.bricks.core.RunnableAndReturn;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public class MutexRunnerWithZK implements MutexRunner, LogAble {

	@Resource
	protected CuratorFramework curator;

	@Override
	public void mutexRun(final String lockNode, final Runnable r) throws Exception {
		mutexRunAndReturn(lockNode, new RunnableAndReturn<Void>() {
			@Override
			public Void run() {
				r.run();
				return null;
			}
		});
	}

	@Override
	public <T> T mutexRunAndReturn(String lockNode, RunnableAndReturn<T> r) throws Exception {
		final Result<T> R = new Result<>();
		Assert.notNull(curator, "ZooKeeperClient can't be null.");
		// 启动zk-client框架
		if (CuratorFrameworkState.STARTED != curator.getState()) {
			synchronized (curator) {
				if (CuratorFrameworkState.STARTED != curator.getState()) {
					curator.start();
				}
			}
		}

		// 初始化锁
		final InterProcessLock lock = new InterProcessMutex(curator, lockNode);

		log().debug("Acquire the lock: {}", lockNode);
		if (!lock.acquire(30, TimeUnit.SECONDS)) {
			throw new IllegalStateException("Could not acquire the lock: " + lockNode);
		}

		try {
			R.t = r.run();
		} finally {
			log().debug("Releasing the lock: {}", lockNode);
			lock.release();
		}
		return R.t;
	}

	class Result<T> {
		T t;
	}

}
