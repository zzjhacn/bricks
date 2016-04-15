package com.bricks.calendar.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.bricks.calendar.CalendarHelper;
import com.bricks.core.event.Event;
import com.bricks.core.event.EventBus;
import com.bricks.core.schedule.ann.Schedulable;
import com.bricks.core.simpleservice.DubboBasedServiceServer;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
@Schedulable
public class CalendarServerImpl implements CalendarServer, DubboBasedServiceServer, LogAble {

	@Value("${calendar.server.abnormal_date_file_path}")
	private String abnormalDateFilePath;

	private static final List<Date> abnormalDateCache = new ArrayList<>();

	public boolean isWorkday(Date d) {
		return CalendarHelper.isWorkday(d) && !abnormalDateCache.contains(d);
	}

	@Schedulable(name = "refresh-abnormal-date-task")
	public void refreshAndNotify() {
		refresh();
		Event event = new Event(EVENT_TYPE_CACHE_REFRESHED);
		event.addContext(EVENT_KEY_CACHE_REFRESHED, abnormalDateCache);
		EventBus.broadcast(event);
	}

	private void refresh() {
		File f = new File(abnormalDateFilePath);
		if (abnormalDateFilePath == null) {
			log().warn("[abnormalDateFilePath] is not set!!!");
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
}
