package com.bricks.calendar.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.bricks.core.schedule.SimpleScheduler;
import com.bricks.core.simpleservice.SimpleServiceServer;

/**
 * @author bricks <long1795@gmail.com>
 */
@Service
public class CalendarServerImpl implements SimpleServiceServer, CalendarServer, SimpleScheduler {

	private String abnormalDateFilePath;

	private static final List<Date> abnormalDateCache = new ArrayList<>();

	public boolean isWorkday(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		boolean isWorkday = c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
		return isWorkday && !abnormalDateCache.contains(d);
	}

	@PostConstruct
	public void init() {
		schedule(() -> refresh(), 60 * 60);
	}

	private void refresh() {
		File f = new File(abnormalDateFilePath);
		if (abnormalDateFilePath == null) {
			log().warn("[{} is not set!!!", abnormalDateFilePath);
			return;
		}
		if (!f.exists() || !f.isFile()) {
			log().warn("File[{}] not found!!!", abnormalDateFilePath);
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
