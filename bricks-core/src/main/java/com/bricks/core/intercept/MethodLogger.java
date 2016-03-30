package com.bricks.core.intercept;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.bricks.lang.log.IgnoreLog;
import com.bricks.lang.log.LogAble;
import com.bricks.utils.StringUtil;

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
		Method m = point.getMethod();
		if (m.isAnnotationPresent(IgnoreLog.class)) {
			log().info("Method[{}] is configed as ignore log.", m);
			return point.proceed();
		}
		long start = System.currentTimeMillis();
		try {
			Object result = point.proceed();
			log().info("#Method[{}] called with args=[{}], return[{}], cost [{}]ms", m, StringUtil.parseArgsAsString(point.getArguments()), result,
					System.currentTimeMillis() - start);
			return result;
		} catch (Throwable t) {
			log().info("#Method[{}] called with args=[{}], throws[{}], cost [{}]ms", m, StringUtil.parseArgsAsString(point.getArguments()), t, System.currentTimeMillis() - start);
			throw t;
		}
	}

}
