package com.bricks.core.simpleservice;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.rpc.ProxyFactory;
import com.alibaba.dubbo.rpc.RpcContext;
import com.bricks.core.event.EventBus;
import com.bricks.core.event.EventHandler;
import com.bricks.core.event.EventSubscriber;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public interface DubboBasedServiceServer extends LogAble {

	final static Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
	final static ProxyFactory proxy = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();

	default void dubboUnsubscriber(final String eventType, final String subscriberUid) {
		// TODO server端cluster环境下，各个节点之间的取消订阅同步 - 待实现
		log().info("Unsubscribering event[{}].", eventType);
		EventBus.unsubscribe(eventType, subscriberUid);
	}

	default void dubboSubscriber(final String eventType, final EventSubscriber subscriber, final int port) {
		// TODO server端cluster环境下，各个节点之间的订阅同步 - 待实现
		EventHandler handler = proxy
				.getProxy(protocol.refer(EventHandler.class, URL.valueOf("dubbo://" + RpcContext.getContext().getRemoteHost() + ":" + port
						+ "/" + EventHandler.class.getName() + "?codec=exchange")));
		subscriber.setHandler(handler);
		EventBus.subscribe(eventType, subscriber);
	}

}
