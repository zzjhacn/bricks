package com.bricks.core.schedule.ann;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author bricks <long1795@gmail.com>
 */
@Target({ TYPE, METHOD })
@Retention(RUNTIME)
public @interface Schedulable {

	String name() default "";

	int interval() default 60;

	TimeUnit timeUnit() default TimeUnit.SECONDS;
}
