package com.bricks.calendar.server;

import java.util.Date;

/**
 * @author bricks <long1795@gmail.com>
 */
public interface CalendarServer {
	public static final String EVENT_TYPE_CACHE_REFRESHED = "EVENT_TYPE_CACHE_REFRESHED";
	public static final String EVENT_KEY_CACHE_REFRESHED = "EVENT_KEY_CACHE_REFRESHED";

	boolean isWorkday(Date d);
}
