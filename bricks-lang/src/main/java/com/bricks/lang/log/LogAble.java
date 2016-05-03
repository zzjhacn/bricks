package com.bricks.lang.log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * log基础接口类，实现此类，使日志框架对使用方透明<br/>
 * TODO 可通过AOP动态代理实现log框架的透明切换
 * 
 * @author bricks <long1795@gmail.com>
 */
public interface LogAble {

	static Map<String, Logger> loggers = new ConcurrentHashMap<String, Logger>();

	public static Logger slog() {
		String clazz = Thread.currentThread().getStackTrace()[2].getClassName();
		return getLogger(clazz);
	}

	static Logger getLogger(String name) {
		if (!loggers.containsKey(name)) {
			loggers.put(name, LoggerFactory.getLogger(name));
		}
		return loggers.get(name);
	}

	default Logger log() {
		return getLogger(this.getClass().getName());
	}

	default void err(Throwable t) {
		log().error(t.getMessage(), t);
	}
}
