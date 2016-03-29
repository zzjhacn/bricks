package com.bricks.core.interrept;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public class MethodLogger implements MethodInterceptor, LogAble {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	@Override
	public Object invoke(MethodInvocation point) throws Throwable {
		long start = System.currentTimeMillis();
		Object result = point.proceed();
		log().info(">>>>>>>>>>>>>>>>>>#%s(%s): %s in %[msec]s", point.getMethod().getName(), point.getMethod(), result,
				System.currentTimeMillis() - start);
		return result;
	}
}
