package com.bricks.calendar.server;

import java.util.Date;

/**
 * @author bricks <long1795@gmail.com>
 */
public interface CalendarServer {
	public static final String EVENT_TYPE_CACHE_REFRESHED = "EVENT_TYPE_CACHE_REFRESHED_CALENDAR";
	public static final String EVENT_KEY_CACHE_REFRESHED = "EVENT_KEY_ABNORMAL_DATES";

	boolean isWorkday(Date d);
}
