package com.bricks.core.schedule;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author bricks <long1795@gmail.com>
 */
public interface SimpleScheduler {
	final static ScheduledExecutorService pool = new ScheduledThreadPoolExecutor(10);

	default void schedule(Runnable command, long period) {
		pool.scheduleAtFixedRate(command, 0, period, TimeUnit.SECONDS);
	}

}
