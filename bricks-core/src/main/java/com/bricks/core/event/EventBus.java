package com.bricks.core.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.bricks.lang.log.LogAble.slog;

/**
 * @author bricks <long1795@gmail.com>
 */
public class EventBus {
	private EventBus() {}

	public final static ConcurrentHashMap<String, List<EventSubscriber>> register = new ConcurrentHashMap<>();

	public static final void unsubscribe(final String eventType, final String subscriberUid) {
		if (register.get(eventType) == null) {
			slog().warn("No subscribe exists for event type[{}].", eventType);
			return;
		}
		for (EventSubscriber es : register.get(eventType)) {
			if (es.get__id().equals(subscriberUid)) {
				register.remove(es);
				slog().info("Subscriber[{}] unsubscribe for event[{}].", subscriberUid, eventType);
			}
		}
	}

	public static final void subscribe(final String eventType, final EventSubscriber subscriber) {
		if (!register.containsKey(eventType)) {
			register.put(eventType, new ArrayList<>());
		}
		slog().info("Subscriber[{}] is listening for evnet[{}]", subscriber, eventType);
		register.get(eventType).add(subscriber);
	}

	public static final void broadcast(final Event event) {
		if (register.containsKey(event.getEventType())) {
			register.get(event.getEventType()).forEach(subscriber -> {
				subscriber.handle(event);
			});
		}
	}
}
