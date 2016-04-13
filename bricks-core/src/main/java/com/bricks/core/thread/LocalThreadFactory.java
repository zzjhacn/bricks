package com.bricks.core.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author bricks <long1795@gmail.com>
 */
public class LocalThreadFactory implements ThreadFactory {
	private final AtomicInteger poolNumber;
	private final ThreadGroup group;
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private final String namePrefix;

	public LocalThreadFactory(final String namePrefix) {
		poolNumber = new AtomicInteger(1);
		SecurityManager s = System.getSecurityManager();
		group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		this.namePrefix = namePrefix + poolNumber.getAndIncrement();
	}

	public Thread newThread(final Runnable r) {
		Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
		if (t.isDaemon())
			t.setDaemon(false);
		if (t.getPriority() != Thread.NORM_PRIORITY)
			t.setPriority(Thread.NORM_PRIORITY);
		return t;
	}
}
