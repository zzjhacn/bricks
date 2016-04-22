package com.bricks.core.dubbo;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.rpc.ProxyFactory;

/**
 * @author bricks <long1795@gmail.com>
 */
public class DubboReferHelper {
	private DubboReferHelper() {}

	final static Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
	final static ProxyFactory proxy = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();

	public static <T> T refer(Class<T> clazz, String registyAddr) {
		return proxy.getProxy(protocol.refer(clazz, URL.valueOf(registyAddr)));
	}
}
