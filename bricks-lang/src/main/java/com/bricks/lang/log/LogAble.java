package com.bricks.lang.log;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bricks <long1795@gmail.com>
 */
public interface LogAble {

	static Map<String, Logger> loggers = new HashMap<String, Logger>();

	public static Logger slog() {
		String clazz = Thread.currentThread().getStackTrace()[2].getClassName();
		return getLogger(clazz);
	}

	static Logger getLogger(String name) {
		synchronized (loggers) {
			if (!loggers.containsKey(name)) {
				loggers.put(name, LoggerFactory.getLogger(name));
			}
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
