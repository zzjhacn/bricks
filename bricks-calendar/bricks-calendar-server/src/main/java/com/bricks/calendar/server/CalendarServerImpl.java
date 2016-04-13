package com.bricks.calendar.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.bricks.core.event.Event;
import com.bricks.core.event.EventBus;
import com.bricks.core.event.EventSubscriber;
import com.bricks.core.schedule.ann.Schedulable;
import com.bricks.core.simpleservice.SimpleServiceServer;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
@Schedulable
public class CalendarServerImpl implements CalendarServer, SimpleServiceServer, LogAble {

	@Value("${calendar.server.abnormal_date_file_path}")
	private String abnormalDateFilePath;

	@Value("${calendar.server.refresh_interval}")
	private int interval = 60 * 60;

	private static final List<Date> abnormalDateCache = new ArrayList<>();

	public boolean isWorkday(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		boolean isWorkday = c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
		return isWorkday && !abnormalDateCache.contains(d);
	}

	@Schedulable(name = "refresh-abnormal-date-task")
	public void refreshAndNotify() {
		refresh();
		Event event = new Event(EVENT_TYPE_CACHE_REFRESHED);
		event.addContext(EVENT_KEY_CACHE_REFRESHED, abnormalDateCache);
		EventBus.publishEvent(event);
	}

	private void refresh() {
		File f = new File(abnormalDateFilePath);
		if (abnormalDateFilePath == null) {
			log().warn("[{} is not set!!!", abnormalDateFilePath);
			return;
		}
		if (!f.exists() || !f.isFile()) {
			log().warn("File[{}] not found!!!", f.getAbsolutePath());
			return;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			BufferedReader reader = new BufferedReader(new FileReader(f));
			List<Date> list = new ArrayList<>();
			String line = reader.readLine();
			int lineNo = 1;
			while (line != null) {
				try {
					list.add(sdf.parse(line));
				} catch (Throwable t) {
					log().info("Error when parse line[{}] in file[{}].", lineNo, abnormalDateFilePath);
				}

				line = reader.readLine();
				lineNo++;
			}
			reader.close();
			synchronized (abnormalDateCache) {
				abnormalDateCache.clear();
				abnormalDateCache.addAll(list);
			}
		} catch (Throwable t) {
			log().error("Error when parsing abnormal date file : " + abnormalDateFilePath, t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.lang.log.LogAble#log()
	 */
	@Override
	public Logger log() {
		// TODO Auto-generated method stub
		return SimpleServiceServer.super.log();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.lang.log.LogAble#err(java.lang.Throwable)
	 */
	@Override
	public void err(Throwable t) {
		// TODO Auto-generated method stub
		SimpleServiceServer.super.err(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.core.simpleservice.SimpleServiceServer#registLocalSubscriber(java.lang.String, com.bricks.core.event.EventSubscriber)
	 */
	@Override
	public void registLocalSubscriber(String eventType, EventSubscriber subscriber) {
		// TODO Auto-generated method stub
		SimpleServiceServer.super.registLocalSubscriber(eventType, subscriber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.core.simpleservice.SimpleServiceServer#registRemoteSubscriber(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void registRemoteSubscriber(String eventType, String subscriberClassName, String subscriberUrl) {
		// TODO Auto-generated method stub
		SimpleServiceServer.super.registRemoteSubscriber(eventType, subscriberClassName, subscriberUrl);
	}

}
