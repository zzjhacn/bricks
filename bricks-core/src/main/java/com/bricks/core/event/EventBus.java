package com.bricks.core.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.bricks.lang.log.LogAble.slog;

/**
 * 事件总线
 * 
 * @author bricks <long1795@gmail.com>
 */
public class EventBus {
	private EventBus() {}

	public final static ConcurrentHashMap<String, List<EventSubscriber>> register = new ConcurrentHashMap<>();

	/**
	 * 解除订阅
	 * 
	 * @param eventType
	 * @param subscriberUid
	 */
	public static final void unsubscribe(final String eventType, final String subscriberUid) {
		if (register.get(eventType) == null) {
			slog().warn("No subscribe exists for event type[{}].", eventType);
			return;
		}
		Iterator<EventSubscriber> it = register.get(eventType).iterator();
		while (it.hasNext()) {
			if (subscriberUid.equals(it.next().get__id())) {
				it.remove();
				slog().info("Subscriber[{}] unsubscribe for event[{}].", subscriberUid, eventType);
				return;
			}
		}
	}

	/**
	 * 订阅事件
	 * 
	 * @param eventType
	 * @param subscriber
	 */
	public static final void subscribe(final String eventType, final EventSubscriber subscriber) {
		if (!register.containsKey(eventType)) {
			register.put(eventType, new ArrayList<>());
		}
		slog().info("Subscriber[{}] is listening for evnet[{}]", subscriber, eventType);
		register.get(eventType).add(subscriber);
	}

	/**
	 * 广播
	 * 
	 * @param event
	 */
	public static final void broadcast(final Event event) {
		final ConcurrentMap<String, String> report = new ConcurrentHashMap<>();
		if (register.containsKey(event.getEventType())) {
			register.get(event.getEventType()).forEach(subscriber -> {
				boolean notify = false;
				if (subscriber.isNotifyEachNode()) {
					subscriber.handle(event);
					notify = true;
				} else {
					if (!report.containsValue(subscriber.getAppName())) {
						subscriber.handle(event);
						notify = true;
					}
				}
				report.put(subscriber.getId() + "--" + subscriber.get__id() + "--" + notify, subscriber.getAppName());
			});
		}
		if (report.isEmpty()) {
			slog().info("None subscribers for event[{}]", event.toString());
			return;
		}
		slog().info("---------- Event broadcast report (Event=[{}]) ----------", event.toString());
		report.forEach((k, v) -> {
			slog().info("Notified appname[{}] client[{}].", v, k);
		});
		slog().info("---------- end report ----------");
	}
}
