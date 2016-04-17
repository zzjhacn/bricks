package com.bricks.core.simpleservice;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.rpc.RpcContext;
import com.bricks.core.SpringCtxHolder;
import com.bricks.core.event.Event;
import com.bricks.core.event.EventHandler;
import com.bricks.core.event.EventSubscriber;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public abstract class DubboBasedServiceClient implements EventHandler, LogAble {

	abstract protected String eventType();

	abstract protected void handleImpl(Event event);

	private DubboBasedServiceServer simpleServiceServer;

	private EventSubscriber subscriber;

	@PostConstruct
	public void init() {
		new Thread(() -> {
			try {
				// 延迟一秒订阅事件（防止事件监听服务尚未启动导致出错）
				Thread.sleep(1000);
				subscriber = new EventSubscriber(SpringCtxHolder.getBean(ApplicationConfig.class).getName(), true);
				subscriber.setId(RpcContext.getContext().getLocalAddressString());
				simpleServiceServer.dubboSubscriber(eventType(), subscriber, SpringCtxHolder.getBean(ProtocolConfig.class).getPort());
			} catch (Throwable e) {
				err(e);
			}
		}).start();
	}

	@PreDestroy
	public void destory() {
		try {
			simpleServiceServer.dubboUnsubscriber(eventType(), subscriber.get__id());
			// 延迟一秒取消订阅事件（等待远端取消）
			Thread.sleep(1000);
		} catch (Throwable e) {
			err(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.core.event.EventSubscriber#handle(com.bricks.core.event.Event)
	 */
	@Override
	public void handle(Event event) {
		if (eventType().equals(event.getEventType())) {
			log().info("Handling event[{}].", event);
			handleImpl(event);
		}
	}

	public void setSimpleServiceServer(DubboBasedServiceServer simpleServiceServer) {
		this.simpleServiceServer = simpleServiceServer;
	}

}
