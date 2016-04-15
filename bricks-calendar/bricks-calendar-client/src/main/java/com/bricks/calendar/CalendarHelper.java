package com.bricks.calendar;

import java.util.Calendar;
import java.util.Date;

/**
 * @author bricks <long1795@gmail.com>
 */
public class CalendarHelper {
	private CalendarHelper() {}

	public static boolean isWorkday(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		boolean isHoliday = c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
		return !isHoliday;
	}
}
