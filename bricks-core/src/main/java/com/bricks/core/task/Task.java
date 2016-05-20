package com.bricks.core.task;

/**
 * 功能描述： 基于zookeeper的任务
 * 
 * @author bricks <long1795@gmail.com>
 */
public interface Task {

	void execute() throws Exception;
}
