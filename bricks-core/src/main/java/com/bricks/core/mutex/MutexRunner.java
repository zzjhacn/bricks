package com.bricks.core.mutex;

import com.bricks.core.RunnableAndReturn;

/**
 * @author bricks <long1795@gmail.com>
 */
public interface MutexRunner {

	<T> T mutexRunAndReturn(String lock, RunnableAndReturn<T> r) throws Exception;

	void mutexRun(String lock, Runnable r) throws Exception;
}
