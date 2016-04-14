package com.bricks.core.simpleservice;

import java.util.Arrays;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.bricks.core.SpringCtxHolder;
import com.bricks.core.event.Event;
import com.bricks.core.event.EventBus;
import com.bricks.core.event.EventSubscriber;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public interface SimpleServiceServer extends LogAble {

	default void registLocalSubscriber(final String eventType, final EventSubscriber subscriber) {
		EventBus.subscribe(eventType, subscriber);
	}

	default void registDubboSubscriber(final String eventType, final ApplicationConfig dubboApplication, final RegistryConfig dubboRegistry) {
		log().warn("Registing dubbo subscriber for event type[{}].", eventType);
		ReferenceBean<EventSubscriber> ref = new ReferenceBean<>();
		ref.setApplication(dubboApplication);
		ref.setRegistry(dubboRegistry);
		ref.setInterface(EventSubscriber.class);
		try {
			log().info("[{}]", ref.getObject());
			EventBus.subscribe(eventType, (EventSubscriber) ref.getObject());
		} catch (Exception e) {
			err(e);
		}
	}

	default void registRemoteSubscriber(final String eventType, final String subscriberClassName, final String subscriberUrl) {
		String[] beans;
		try {
			beans = SpringCtxHolder.getCtx().getBeanNamesForType(Class.forName(subscriberClassName));
			if (beans == null || beans.length == 0) {
				log().info("No bean of type[{}] found.", subscriberClassName);
			} else {
				Arrays.asList(beans).forEach(b -> {
					Object bean = SpringCtxHolder.getCtx().getBean(b);
					if (bean instanceof EventSubscriber) {
						EventBus.subscribe(eventType, (EventSubscriber) bean);
					} else {
						log().warn("Bean of name[{}] is not instance of EventSubscriber.", b);
						EventBus.subscribe(eventType, new EventSubscriber() {
							@Override
							public void handle(Event event) {
								// TODO implement proxy
							}
						});
					}
				});
			}
		} catch (Exception e) {
			log().error("Unable to subscribe event of type[{}]. Subscriber class name = [{}], subscriber url = [{}]", eventType, subscriberClassName, subscriberUrl);
			log().error("Cause by : ", e);
		}
	}
}
