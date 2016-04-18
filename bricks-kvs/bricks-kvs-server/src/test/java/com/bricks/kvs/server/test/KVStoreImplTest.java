package com.bricks.kvs.server.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/spring/testContext.xml")
public class KVStoreImplTest implements LogAble {

	@Test
	public void test() throws Exception {
		while (true) {
			Thread.sleep(10 * 1000);
		}
	}
}
