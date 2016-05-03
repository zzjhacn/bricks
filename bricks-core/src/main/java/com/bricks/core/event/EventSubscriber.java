package com.bricks.core.event;

import com.bricks.lang.BaseObject;

/**
 * 事件订阅者
 * 
 * @author bricks <long1795@gmail.com>
 */
public class EventSubscriber extends BaseObject {
	private static final long serialVersionUID = 1L;

	/**
	 * 订阅者ID
	 */
	private String id;

	/**
	 * 所属应用标识
	 */
	private String appName;

	/**
	 * 逐节点通知（分布式环境下，false时仅一个节点被通知）
	 */
	private boolean notifyEachNode;

	/**
	 * 事件处理器
	 */
	private EventHandler handler;

	public EventSubscriber() {}

	/**
	 * @param appName
	 * @param notifyEachNode
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
