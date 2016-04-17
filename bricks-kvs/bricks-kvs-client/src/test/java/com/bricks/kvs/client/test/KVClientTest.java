package com.bricks.kvs.client.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bricks.kvs.client.KVClient;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/spring/applicationContext.xml")
public class KVClientTest implements LogAble {
	@Resource
	private KVClient client;

	@Test
	public void test() throws Exception {
		log().info("Test is [{}]", client.get("test"));
		Thread.sleep(100 * 1000);
	}
}
