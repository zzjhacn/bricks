package com.bricks.core.simpleservice;

import com.alibaba.dubbo.rpc.RpcContext;
import com.bricks.core.dubbo.DubboReferHelper;
import com.bricks.core.event.EventBus;
import com.bricks.core.event.EventHandler;
import com.bricks.core.event.EventSubscriber;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public interface DubboBasedServiceServer extends LogAble {

	default void dubboUnsubscriber(final String eventType, final String subscriberUid) {
		// TODO server端cluster环境下，各个节点之间的取消订阅同步 - 待实现
		log().info("Unsubscribering event[{}].", eventType);
		EventBus.unsubscribe(eventType, subscriberUid);
	}

	default void dubboSubscriber(final String eventType, final EventSubscriber subscriber, final int port) {
		// TODO server端cluster环境下，各个节点之间的订阅同步 - 待实现
		EventHandler handler = DubboReferHelper.refer(EventHandler.class, "dubbo://" + RpcContext.getContext().getRemoteHost() + ":" + port
				+ "/" + EventHandler.class.getName() + "?codec=exchange");
		subscriber.setHandler(handler);
		EventBus.subscribe(eventType, subscriber);
	}

}
