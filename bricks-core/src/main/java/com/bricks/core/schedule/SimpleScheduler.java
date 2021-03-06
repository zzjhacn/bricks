package com.bricks.core.schedule;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bricks.core.schedule.ann.Schedulable;
import com.bricks.core.thread.LocalThreadFactory;
import com.bricks.lang.log.LogAble;

/**
 * 基于spring的简单调度器（单节点调度，不支持分布式）<br/>
 * 自动调度标记了 {@Schedulable} 的bean
 * 
 * @author bricks <long1795@gmail.com>
 */
public class SimpleScheduler implements LogAble, ApplicationContextAware {
	final static ScheduledExecutorService POOL = Executors.newScheduledThreadPool(10, new LocalThreadFactory("simple-schedule-thread-"));

	void regist(String name, Runnable runnable, int interval, TimeUnit unit) {
		synchronized (POOL) {
			POOL.scheduleWithFixedDelay(() -> {
				log().info("Starting scheduled task[{}].", name);
				try {
					runnable.run();
				} catch (Throwable t) {
					err(t);
				}
				log().info("Task[{}] scheduled completly.", name);
			} , 0, interval, unit);
			log().info("Scheduler[{}] started with interval {} {}...", name, interval, unit);
		}
	}

	@PreDestroy
	public void destory() {
		log().warn("Shutingdown executors...[{}]", POOL.toString());
		POOL.shutdown();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Schedulable.class);
		log().info("[{}] beans is annotation by Schedulable.class, Start to configuration schedule for these beans...", beans.size());

		beans.forEach((k, v) -> {
			Class<?> clazz = v.getClass();
			Arrays.asList(clazz.getDeclaredMethods()).forEach(m -> {
				if (m.isAnnotationPresent(Schedulable.class)) {
					Schedulable schedulable = m.getAnnotation(Schedulable.class);
					String name = "".equals(schedulable.name()) ? m.toString() : schedulable.name();
					int interval = schedulable.interval();
					String intervalStr = System.getProperty("simple.schedule." + name + ".interval");
					try {
						if (intervalStr != null) {
							interval = Integer.valueOf(intervalStr);
						}
					} catch (Throwable e) {}
					TimeUnit timeUnit = schedulable.timeUnit();
					regist(name, () -> {
						try {
							m.invoke(v);
						} catch (Throwable e) {
							log().error("Error when invoking method : " + m.toString(), e);
						}
					} , interval, timeUnit);
				}
			});
		});
	}

}
