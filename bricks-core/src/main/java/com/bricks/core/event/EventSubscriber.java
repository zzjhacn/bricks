package com.bricks.core.event;

/**
 * @author bricks <long1795@gmail.com>
 */
public interface EventSubscriber {

	void handle(Event event);
}
