package com.bricks.calendar.client.test;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bricks.calendar.client.CalendarClient;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/spring/applicationContext.xml")
public class CalendarClientTest implements LogAble {

	@Resource
	private CalendarClient client;

	String pattern = "yyyy-MM-dd";

	@Test
	public void test() throws Exception {
		Date d = DateUtils.parseDate("2016-04-12", new String[] { pattern });
		log().info("[{}} is workday? [{}]", DateFormatUtils.format(d, pattern), client.isWorkday(d));
		while (true) {
			Thread.sleep(10 * 1000);
		}
	}
}
