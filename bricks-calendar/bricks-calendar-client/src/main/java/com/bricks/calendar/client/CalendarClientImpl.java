package com.bricks.calendar.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.bricks.calendar.CalendarHelper;
import com.bricks.calendar.server.CalendarServer;
import com.bricks.core.event.Event;
import com.bricks.core.simpleservice.DubboBasedServiceClient;

/**
 * @author bricks <long1795@gmail.com>
 */
public class CalendarClientImpl extends DubboBasedServiceClient implements CalendarClient {

	@Resource
	private CalendarServer server;

	private static final List<Date> abnormalDateCache = new ArrayList<>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.calendar.client.CalendarClient#isWorkday(java.util.Date)
	 */
	@Override
	public boolean isWorkday(Date d) {
		if (abnormalDateCache.isEmpty()) {
			return server.isWorkday(d);
		}

		return CalendarHelper.isWorkday(d) && !abnormalDateCache.contains(d);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.core.simpleservice.DubboBasedServiceClient#eventType()
	 */
	@Override
	protected String eventType() {
		return CalendarServer.EVENT_TYPE_CACHE_REFRESHED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.core.simpleservice.DubboBasedServiceClient#handleImpl(com.bricks.core.event.Event)
	 */
	@Override
	protected void handleImpl(Event event) {
		try {
			@SuppressWarnings("unchecked")
			List<Date> list = (List<Date>) event.getCtxVal(CalendarServer.EVENT_KEY_CACHE_REFRESHED);
			if (list == null || list.isEmpty()) {
				log().warn("None data got in event context.");
			} else {
				synchronized (abnormalDateCache) {
					abnormalDateCache.clear();
					abnormalDateCache.addAll(list);
				}
			}
		} catch (Throwable e) {
			log().error("Error when handling event.", e);
		}
	}

}
