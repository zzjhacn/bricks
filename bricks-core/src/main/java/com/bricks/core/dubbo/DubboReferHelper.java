package com.bricks.core.dubbo;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.rpc.ProxyFactory;

/**
 * DUBBO引用助手类
 * 
 * @author bricks <long1795@gmail.com>
 */
public final class DubboReferHelper {
	private DubboReferHelper() {}

	private final static Protocol PROTOCOL = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
	private final static ProxyFactory PROXY = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();

	/**
	 * 基于当前运行环境，产生远程引用代理
	 * 
	 * @param clazz
	 *            要被代理的目标类
	 * @param registyAddr
	 *            远端地址
	 * @return 目标代理
	 */
	public static <T> T refer(Class<T> clazz, String registyAddr) {
		return PROXY.getProxy(PROTOCOL.refer(clazz, URL.valueOf(registyAddr)));
	}
}
