package com.bricks.calendar.server.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author bricks <long1795@gmail.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/spring/testContext.xml")
public class CalendarServerTest {

	@BeforeClass
	public static void init(){
		System.setProperty("dubbo.application.logger", "jdk");
	}

	@Test
	public void test() throws Exception {
		while (true) {
			Thread.sleep(10 * 1000);
		}
	}
}
