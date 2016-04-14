package com.bricks.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author bricks <long1795@gmail.com>
 */
public class SpringCtxHolder implements ApplicationContextAware {

	private static ApplicationContext ctx;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (ctx == null) {
			synchronized (this) {
				if (ctx == null) {
					ctx = applicationContext;
				}
			}
		}
	}

	public static ApplicationContext getCtx() {
		return ctx;
	}

	public static <T> T getBean(Class<T> clazz) {
		return ctx.getBean(clazz);
	}

}
