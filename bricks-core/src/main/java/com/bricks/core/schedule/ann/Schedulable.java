package com.bricks.core.schedule.ann;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 简单调度器标记类
 * 
 * @author bricks <long1795@gmail.com>
 */
@Target({ TYPE, METHOD })
@Retention(RUNTIME)
public @interface Schedulable {

	/**
	 * 名称
	 * 
	 * @return
	 */
	String name() default "";

	/**
	 * 周期
	 * 
	 * @return
	 */
	int interval() default 60;

	/**
	 * 时间单位（默认：秒）
	 * 
	 * @return
	 */
	TimeUnit timeUnit() default TimeUnit.SECONDS;
}
