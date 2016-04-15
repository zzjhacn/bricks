package com.bricks.core.event;

import com.bricks.lang.BaseObject;

/**
 * @author bricks <long1795@gmail.com>
 */
public class EventSubscriber extends BaseObject {
	private static final long serialVersionUID = 1L;

	private String appName;
	private boolean notifyEachNode;
	private EventHandler handler;

	public EventSubscriber() {}

	/**
	 * @param appName
	 * @param notifyEachNode
	 * @param handler
	 */
	public EventSubscriber(String appName, boolean notifyEachNode) {
		super();
		this.appName = appName;
		this.notifyEachNode = notifyEachNode;
	}

	public void handle(Event event) {
		handler.handle(event);
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public boolean isNotifyEachNode() {
		return notifyEachNode;
	}

	public void setNotifyEachNode(boolean notifyEachNode) {
		this.notifyEachNode = notifyEachNode;
	}

	public EventHandler getHandler() {
		return handler;
	}

	public void setHandler(EventHandler handler) {
		this.handler = handler;
	}
}
