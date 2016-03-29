package com.bricks.core.intercept;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public class MethodLoggerBefore implements MethodBeforeAdvice, LogAble {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.aop.MethodBeforeAdvice#before(java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
	 */
	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		log().info("--------");
		method.invoke(args);
	}

}
