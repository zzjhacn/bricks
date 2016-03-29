package com.bricks.facade;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
//@Component
//@Aspect
public class MethodLoggerInterceptor implements LogAble {
//	@Around("execution(* com.bricks.**..*.*())")
	public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
		long start = System.currentTimeMillis();
		Object result = pjp.proceed();
		log().info(">>>>>>>>>>>>>>>>>>#{}({}): {} in {}", MethodSignature.class.cast(pjp.getSignature()).getMethod().getName(),
				pjp.getArgs(), result,
				System.currentTimeMillis() - start);
		return result;
	}
}
