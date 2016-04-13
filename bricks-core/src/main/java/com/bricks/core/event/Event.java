package com.bricks.core.event;

import java.util.HashMap;
import java.util.Map;

import com.bricks.lang.BaseObject;

/**
 * @author bricks <long1795@gmail.com>
 */
public class Event extends BaseObject {
	private static final long serialVersionUID = 1L;

	protected String eventType;

	protected Map<String, Object> eventContext;

	public Event() {}

	public Event(String eventType) {
		this.eventType = eventType;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Map<String, Object> getEventContext() {
		return eventContext;
	}

	public Object getCtxVal(String key) {
		if (eventContext == null) {
			return null;
		}
		return eventContext.get(key);
	}

	public void setEventContext(Map<String, Object> eventContext) {
		this.eventContext = eventContext;
	}

	public void addContext(String key, Object val) {
		if (eventContext == null) {
			eventContext = new HashMap<>();
		}
		eventContext.put(key, val);
	}
}
