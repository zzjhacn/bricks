package com.bricks.dal.ann;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author bricks <long1795@gmail.com>
 */
@Target({ FIELD })
@Retention(RUNTIME)
public @interface Cond {
	String target() default "";

	Operator operator() default Operator.EQ;

}
