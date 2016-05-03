package com.bricks.lang.log;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * log 标记类
 * 
 * @author bricks <long1795@gmail.com>
 */
@Target({ FIELD })
@Retention(RUNTIME)
public @interface IgnoreLog {

	/**
	 * 在log中不出现
	 * 
	 * @return
	 */
	boolean ignore() default true;

	/**
	 * 在log中以特定格式出现（ignore=false时有效）
	 * 
	 * @return
	 */
	String pattern() default "";
}
